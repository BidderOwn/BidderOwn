package site.bidderown.server.boundedcontext.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

import java.util.List;
import java.util.Optional;


public interface BidRepository extends JpaRepository<Bid, Long> {

    Optional<Bid> findByItemAndBidder(Item item, Member bidder);

    @Query("SELECT MAX(b.price) FROM Bid b where b.item = :item")
    Integer findMaxPrice(@Param("item") Item item);
}
