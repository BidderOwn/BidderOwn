package site.bidderown.server.base.redis.buffer;

import java.util.List;

public interface CountBuffer {
    void push(CountTask bufferTask);
    List<CountTask> popAll();
    long size();
}
