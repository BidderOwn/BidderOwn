package site.bidderown.server.base.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExpirationListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String key = new String(message.getBody());
        log.info("expired key: {}", key);
    }
}
