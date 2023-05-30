package site.bidderown.server.bounded_context.item.Controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.item.Controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemControllerTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Rollback(value = false)
    @Test
    void t01() {
        Item item = Item.builder().title("aaa").description("bbb").minimumPrice(10000).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();

        Item result = itemRepository.save(item);

        assertThat(result.getId()).isEqualTo(item.getId());
    }

    @Rollback(value = false)
    @Test
    @Transactional
    void t02() {

//        Item item = new Item();
//        item.creat


    }



}