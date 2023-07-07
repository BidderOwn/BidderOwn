package site.bidderown.server.base.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.BidEndEvent;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExpirationListener implements MessageListener {

    @Value("${custom.redis.item.bidding.info-key}")
    private String itemQueueKey;

    private final ApplicationEventPublisher publisher;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody());
        if (key.contains(itemQueueKey)) {
            publisher.publishEvent(BidEndEvent.of(resolveKeyToItemId(key)));
        }
    }

    private Long resolveKeyToItemId(String key) {
        return Long.parseLong(key.split(":")[1]);
    }
}
