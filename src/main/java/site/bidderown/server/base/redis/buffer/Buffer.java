package site.bidderown.server.base.redis.buffer;

import java.util.List;

/**
 * 처리할 작업을 넣을 buffer interface
 */

public interface Buffer {
    void push(BufferTask bufferTask);
    BufferTask pop();
    List<BufferTask> popAll();
    long size();
}
