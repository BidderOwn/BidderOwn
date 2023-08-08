package site.bidderown.server.boundedcontext.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
