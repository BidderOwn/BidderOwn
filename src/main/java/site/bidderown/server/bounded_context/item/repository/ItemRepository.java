package site.bidderown.server.bounded_context.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByMemberIdAndDeletedIsFalse(Long memberId);

    List<Item> findByMemberAndDeletedIsFalse(Member member);

    Optional<Item> findById(Long id);

    Optional<Item> findByIdAndDeletedIsFalse(Long id);

    Page<Item> findAll(Pageable pageable);

    List<Item> findByTitle(String title);

    @Modifying
    @Query("update Item i " +
            "SET i.itemStatus=:itemStatus " +
            "WHERE i.createdAt >= :startDateTime AND i.createdAt < :endDateTime " +
            "AND i.id IN :ids")
    void updateItemStatus(
            @Param("itemStatus") ItemStatus itemStatus,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("ids") List<Long> ids);


}
