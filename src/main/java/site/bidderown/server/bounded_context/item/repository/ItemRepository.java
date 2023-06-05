package site.bidderown.server.bounded_context.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //이름, 판매자, 내용
    List<Item> findAllByTitleContaining(String title, Pageable pageable);

    List<Item> findAllByMemberId(Long memberId);

    List<Item> findAllByDescription(String description);

    List<Item> findAll();

    Optional<Item> findById(Long id);

}
