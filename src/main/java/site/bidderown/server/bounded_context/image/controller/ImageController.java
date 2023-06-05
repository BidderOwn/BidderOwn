package site.bidderown.server.bounded_context.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.base.util.ImageUtils;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageUtils imageUtils;

    @GetMapping(value = "/{kind}/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImageFile(
            @PathVariable String kind,
            @PathVariable String filename) {
        try {
            return imageUtils.download(filename, kind);
        } catch (IOException e) {
            throw new NotFoundException(kind + "/" + filename);
        }
    }
}
