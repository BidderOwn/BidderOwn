package site.bidderown.server.base.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import site.bidderown.server.base.resolver.PathResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUtils {

    private final PathResolver pathResolver;

    public String upload(MultipartFile file, String kind) {
        try {
            if(!isImageFile(file.getContentType())) return null;
            String originalFileName = file.getOriginalFilename();
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            long nowDate = System.currentTimeMillis();
            long timeStamp = new Timestamp(nowDate).getTime();
            String fileName = kind + "_" + timeStamp + ext;

            String filePath = pathResolver.getImagePathString(kind, fileName);
            file.transferTo(new File(filePath));

            return fileName;
        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }

    public List<String> uploadMulti(List<MultipartFile> files, String kind) {
        return files.stream()
                .map(file -> upload(file, kind))
                .collect(Collectors.toList());
    }

    public Resource download(String kind, String fileName) throws IOException {
        Path path = pathResolver.resolve("images", kind, fileName);
        return new InputStreamResource(Files.newInputStream(path));
    }

    public void delete(String kind, String fileName) throws IOException {
        String filePath = pathResolver.getImagePathString(kind, fileName);
        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }
    }

    //확장자가 이미지 파일인지 확인
    public boolean isImageFile(String contentType) {
        return contentType.contains("image/jpeg")
                || contentType.contains("image/png")
                || contentType.contains("image/gif");
    }
}
