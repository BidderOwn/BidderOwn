package site.bidderown.server.base.aop.cache;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.annotation.cache.CacheEvictByKey;
import site.bidderown.server.base.cache.RedisCacheListUtils;
import site.bidderown.server.base.parser.CustomSpringELParser;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class CacheAop {
    private final RedisCacheListUtils redisCacheListUtils;

    @Around("@annotation(site.bidderown.server.base.annotation.cache.CacheEvictByKey)")
    public Object cacheEvictByKeyPattern(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 메서드 정보
        Method method = signature.getMethod();
        CacheEvictByKey cacheEvict = method.getAnnotation(CacheEvictByKey.class); // 어노테이션 정보

        String keyPattern = cacheEvict.pattern(); // 넘어온 key
        redisCacheListUtils.removeKeyPattern(keyPattern);

        if (StringUtils.isNotEmpty(cacheEvict.value())) {
            String key = String.valueOf(
                    CustomSpringELParser.getDynamicValue(
                            signature.getParameterNames(),
                            joinPoint.getArgs(),
                            cacheEvict.key()
                    )
            );
            System.out.println(resolveKey(cacheEvict.value(), key));

            redisCacheListUtils.removeKey(resolveKey(cacheEvict.value(), key));
        }

        return joinPoint.proceed();
    }

    private String resolveKey(String value, String key) {
        return value + "::" + key;
    }
}
