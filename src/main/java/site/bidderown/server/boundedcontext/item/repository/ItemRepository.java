package site.bidderown.server.boundedcontext.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByMemberAndDeletedIsFalse(Member member);

    Optional<Item> findById(Long id);

    Optional<Item> findByIdAndDeletedIsFalse(Long id);

    List<Item> findByTitle(String title);
}
