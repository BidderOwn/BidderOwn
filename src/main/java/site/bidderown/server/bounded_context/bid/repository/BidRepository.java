package site.bidderown.server.bounded_context.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByItem(Item item);

    List<Bid> findByItemOrderByUpdatedAtDesc(Item item);

    Optional<Bid> findByItemAndBidder(Item item, Member bidder);

    @Query("SELECT MAX(b.price) FROM Bid b where b.item = :item")
    Integer findMaxPrice(@Param("item") Item item);

    @Query("SELECT MIN (b.price) FROM Bid b where b.item = :item")
    Integer findMinPrice(@Param("item") Item item);

    @Query("SELECT AVG (b.price) FROM Bid b where b.item = :item")
    Integer findAvgPrice(@Param("item") Item item);

    List<Bid> findBidsByCreatedAtAfter(LocalDateTime createdAt);

    Integer countByItemId(Long itemId);
}
