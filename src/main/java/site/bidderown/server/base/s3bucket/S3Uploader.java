package site.bidderown.server.base.s3bucket;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {
    
    private final AmazonS3Client amazonS3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile 변환 실패"));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadFile.getName();
    }

    private void putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(
                        bucket,
                        fileName,
                        uploadFile
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        );
        amazonS3Client.getUrl(bucket, fileName);
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("The file is deleted...");
        } else {
            log.info("The file is not deleted..");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
