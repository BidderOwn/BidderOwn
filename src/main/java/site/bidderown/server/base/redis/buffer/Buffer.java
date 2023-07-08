package site.bidderown.server.base.redis.buffer;

import java.util.List;

public interface Buffer {
    void push(BufferTask bufferTask);
    BufferTask pop();
    List<BufferTask> popAll();
    long size();
}
