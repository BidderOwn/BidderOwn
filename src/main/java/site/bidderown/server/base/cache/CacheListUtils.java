package site.bidderown.server.base.cache;

import java.util.List;

public interface CacheListUtils {
    List<?> get(String key);

    void set(String key, List<?> objs, long ttl);

    void add(String key, Object obj, boolean right);

    void evict(String key);
}
