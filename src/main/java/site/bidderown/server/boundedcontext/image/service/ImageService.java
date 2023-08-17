package site.bidderown.server.boundedcontext.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.bidderown.server.base.s3bucket.S3Uploader;
import site.bidderown.server.boundedcontext.image.entity.Image;
import site.bidderown.server.boundedcontext.image.repository.ImageRepository;
import site.bidderown.server.boundedcontext.item.entity.Item;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    /**
     * 실제 이미지 파일을 저장하고 데이터베이스에 저장
     *
     * @param item   Item entity
     * @param images ItemRequest에서 넘어온 MultipartFile
     * @return ThumbnailImageFileName
     */
    @Transactional
    public String create(List<MultipartFile> images, Item item) {
        List<Image> imageEntities = images.stream().map(image -> {
                    try {
                        return s3Uploader.upload(image, "item", item.getId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).map(filename -> Image.of(item, filename))
                .toList();

        imageRepository.saveAll(imageEntities);
        return imageEntities.get(0).getFileName();
    }

    /**
     * @description 파일 업로드 로직 제외, 더미 데이터 or 테스트 전용
     */
    @Transactional
    public String create(Item item, List<String> fileNames) {
        imageRepository.saveAll(
                fileNames.stream()
                        .map(fileName -> Image.of(item, fileName))
                        .collect(Collectors.toList())
        );
        return fileNames.get(0);
    }
}
