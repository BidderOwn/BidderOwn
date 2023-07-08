package site.bidderown.server.bounded_context.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.bidderown.server.base.util.ImageUtils;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.image.repository.ImageRepository;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageUtils imageUtils;

    /**
     * 실제 이미지 파일을 저장하고 데이터베이스에 저장
     * @param item Item entity
     * @param images ItemRequest에서 넘어온 MultipartFile
     * @return ThumbnailImageFileName
     */
    @Transactional
    public String create(List<MultipartFile> images, Item item) {
        List<String> fileNames = imageUtils.uploadMulti(images, "item");
        imageRepository.saveAll(
                fileNames.stream()
                        .map(fileName -> Image.of(item, fileName))
                        .collect(Collectors.toList())
        );
        return fileNames.get(0);
    }

    /**
     * @description 파일 업로드 로직 제외, 더미 데이터 or 테스트 전용
     * */
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
