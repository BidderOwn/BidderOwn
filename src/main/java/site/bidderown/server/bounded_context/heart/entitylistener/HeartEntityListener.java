package site.bidderown.server.bounded_context.heart.entitylistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

/**
 * Heart Entity 추가 이벤트 리스너
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class HeartEntityListener {

    private final ItemCountBuffer itemCountBuffer;

    @Value("${custom.buffer-task.type.heart}")
    private String type;

    @PostPersist
    public void postPersist(Heart heart) {
        log.info("heart post persist {}", heart.getId());
        itemCountBuffer.push(HeartCountTask.of(type, heart.getItem().getId()));
    }
}
