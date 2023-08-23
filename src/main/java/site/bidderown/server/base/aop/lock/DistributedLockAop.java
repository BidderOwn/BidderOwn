package site.bidderown.server.base.aop.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.annotation.lock.DistributedLock;
import site.bidderown.server.base.aop.AopForTransaction;
import site.bidderown.server.base.parser.CustomSpringELParser;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {
    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(site.bidderown.server.base.annotation.lock.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 호출된 메서드 정보
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(), // 파라미터 변수 이름
                joinPoint.getArgs(), // 파라미터 변수 실제 값
                distributedLock.key() // @DistributedLock(key="키정보")
        ); // 넘어온 메서드 정보로 실제 key 값을 파싱

        RLock rLock = redissonClient.getLock(key); // lock 획득

        try {
            boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!available) return false;

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock {} {}",
                        method.getName(), key
                );
            }
        }
    }
}
