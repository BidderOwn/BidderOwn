package site.bidderown.server.base.redis.buffer;

/**
 * Redis buffer에 들어갈 데이터 타입
 */

public interface BufferTask {
    Long getId();
    BufferTaskType getType();
}
