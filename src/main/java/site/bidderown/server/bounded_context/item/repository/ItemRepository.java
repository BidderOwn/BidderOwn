package site.bidderown.server.bounded_context.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByMemberAndDeletedIsFalse(Member member);

    Optional<Item> findById(Long id);

    Optional<Item> findByIdAndDeletedIsFalse(Long id);

    Page<Item> findAll(Pageable pageable);

    List<Item> findByTitle(String title);
}
