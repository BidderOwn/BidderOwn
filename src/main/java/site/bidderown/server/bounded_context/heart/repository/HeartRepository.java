package site.bidderown.server.bounded_context.heart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    List<Heart> findByMemberId(Long memberId);
    Optional<Heart> findByItemIdAndMemberId(Long itemId, Long memberId);
}
