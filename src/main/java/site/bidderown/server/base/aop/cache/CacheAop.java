package site.bidderown.server.base.aop.cache;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.annotation.CacheEvictByKeyPattern;
import site.bidderown.server.base.parser.CustomSpringELParser;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class CacheAop {
    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(site.bidderown.server.base.annotation.CacheEvictByKeyPattern)")
    public Object cacheEvictByKeyPattern(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 메서드 정보
        Method method = signature.getMethod();
        CacheEvictByKeyPattern cacheEvict = method.getAnnotation(CacheEvictByKeyPattern.class); // 어노테이션 정보
        String keyPattern = cacheEvict.keyPattern(); // 넘어온 key
        String id = cacheEvict.id();

        if (!StringUtils.isEmpty(id)) {
            id = String.valueOf( // 구체적으로 지울 key id
                    CustomSpringELParser.getDynamicValue(
                            signature.getParameterNames(),
                            joinPoint.getArgs(),
                            cacheEvict.id()
                    ));
        }

        removeKeyPattern(keyPattern + id);
        return joinPoint.proceed();
    }

    private void removeKeyPattern(String keyPattern) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(keyPattern + "*").build());
            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                redisTemplate.delete(new String(key));
            }
            cursor.close();
            return null;
        });
    }
}
