package site.bidderown.server.bounded_context.bid.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;
import java.util.Optional;


public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByItem(Item item);
}
