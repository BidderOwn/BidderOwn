package site.bidderown.server.bounded_context.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.image.repository.ImageRepository;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void create(Item item, List<String> fileName) {
        imageRepository.saveAll(
                fileName.stream()
                        .map(_fileName -> Image.of(item, _fileName))
                        .collect(Collectors.toList())
        );
    }
}
