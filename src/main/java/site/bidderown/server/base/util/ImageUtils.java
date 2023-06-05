package site.bidderown.server.base.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
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

@Component
public class ImageUtils {

    @Value("${custom.file.path}")
    private String filePath;

    public String upload(MultipartFile file, String kind) {
        try {
            String originalFileName = file.getOriginalFilename();
            String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
            long nowDate = System.currentTimeMillis();
            long timeStamp = new Timestamp(nowDate).getTime();
            String fileName = kind + "_" + timeStamp + ext;
            file.transferTo(new File(filePath+fileName));
            return fileName;
        } catch (Exception e){

        }
        return null;
    }

    public List<String> uploadMulti(List<MultipartFile> files, String kind) {
        return files.stream()
                .map(file -> upload(file, kind))
                .collect(Collectors.toList());
    }


    public Resource download(String fileName, String kind) throws IOException {
        Path path = Paths.get(filePath + kind + "/" + fileName);
        return new InputStreamResource(Files.newInputStream(path));
    }

    public void delete(String fileName, String kind) {
        File file = new File(filePath + kind + "/" + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

}
