package site.bidderown.server.base.util;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ImageUtils {
    public String upload(MultipartFile file, String kind) {
        try {
            String originalFileName = file.getOriginalFilename();
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            long nowDate = System.currentTimeMillis();
            long timeStamp = new Timestamp(nowDate).getTime();
            String fileName = kind + "_" + timeStamp + ext;

            Path path = Paths.get("src", "main", "resources", "images", kind, fileName);
            String filePath = new UrlResource(path.toUri()).getURI().getPath();

            file.transferTo(new File(filePath));

            return filePath;
        } catch (Exception e) {

        }
        return null;
    }

    public List<String> uploadMulti(List<MultipartFile> files, String kind) {
        return files.stream()
                .map(file -> upload(file, kind))
                .collect(Collectors.toList());
    }

    public Resource download(String fileName, String kind) throws IOException {
        Path path = Paths.get("src", "main", "resources", "images", kind, fileName);
        return new InputStreamResource(Files.newInputStream(path));
    }

    public void delete(String fileName, String kind) throws IOException {
        Path path = Paths.get("src", "main", "resources", "images", kind, fileName);
        String filePath = new UrlResource(path.toUri()).getURI().getPath();

        File file = new File(filePath);

        if (file.exists()) {
            file.delete();
        }
    }

}
