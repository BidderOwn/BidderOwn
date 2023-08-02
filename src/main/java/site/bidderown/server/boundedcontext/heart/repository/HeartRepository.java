package site.bidderown.server.boundedcontext.heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.heart.entity.Heart;

import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByMemberId(Long memberId);
    Optional<Heart> findByItemIdAndMemberId(Long itemId, Long memberId);
}
