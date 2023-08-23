package site.bidderown.server.base.aop.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.annotation.cache.CacheEvictByKeyPattern;
import site.bidderown.server.base.cache.RedisCacheListUtils;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class CacheAop {
    private final RedisCacheListUtils redisCacheListUtils;

    @Around("@annotation(site.bidderown.server.base.annotation.cache.CacheEvictByKeyPattern)")
    public Object cacheEvictByKeyPattern(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 메서드 정보
        Method method = signature.getMethod();
        CacheEvictByKeyPattern cacheEvict = method.getAnnotation(CacheEvictByKeyPattern.class); // 어노테이션 정보
        String keyPattern = cacheEvict.pattern(); // 넘어온 key
        redisCacheListUtils.removeKeyPattern(keyPattern);
        return joinPoint.proceed();
    }
}
