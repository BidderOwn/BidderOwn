package site.bidderown.server.bounded_context.heart.entitylistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import site.bidderown.server.base.util.BeanUtils;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

/**
 * Heart Entity 추가 이벤트 리스너
 */

@Slf4j
@NoArgsConstructor
public class HeartEntityListener {

    @Value("${custom.buffer-task.type.heart}")
    private String type;

    @PostPersist
    public void postPersist(Heart heart) {
        ItemCountBuffer itemCountBuffer = BeanUtils.getBean(ItemCountBuffer.class);
        itemCountBuffer.push(HeartCountTask.of(type, heart.getItem().getId()));
    }
}
