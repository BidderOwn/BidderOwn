package site.bidderown.server.bounded_context.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //이름, 판매자, 내용
    List<Item> findAllByTitleContaining(String title, Pageable pageable);

    List<Item> findAllByMemberId(Long memberId);

    List<Item> findAllByDescription(String description);

    List<Item> findAll();

    Optional<Item> findById(Long id);

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
