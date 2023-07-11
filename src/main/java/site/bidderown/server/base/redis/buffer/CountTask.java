package site.bidderown.server.base.redis.buffer;

public interface CountTask extends BufferTask {
    CounterTaskType getType();
    int getDelta();
}
