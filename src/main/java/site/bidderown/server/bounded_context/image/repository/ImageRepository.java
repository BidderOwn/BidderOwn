package site.bidderown.server.bounded_context.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
