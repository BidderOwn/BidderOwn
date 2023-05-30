package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.item.Controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private ItemRepository itemRepository;

    public List<Item> findAllByDescription(String description) {
        List<Item> itemList = itemRepository.findAllByDescription(description);
        return itemList;
    }

    public List<Item> findAllByTitle(String title, Pageable pageable) {
        List<Item> itemList = itemRepository.findAllByTitleContaining(title, pageable);
        return itemList;
    }

//    public ItemResponse findById(Long id) {
//        Assert.isTrue(id >0, "userId must be positive");
//        return ItemResponse.from(itemRepository.findById(id)
//                .orElseThrow(()-> null));
//    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void delete(Item item) {
        //null 체크
        itemRepository.delete(item);
    }

    public Item create(Item item) {
        Item createdItem = itemRepository.save(Item.createItem(item));
        return createdItem;
    }
}
