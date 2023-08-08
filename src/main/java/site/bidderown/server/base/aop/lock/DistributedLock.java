package site.bidderown.server.base.aop.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    // 락 이름
    String key();

    // 락 시간 단위
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    // 락을 기다리는 시간
    long waitTime() default 3L;

    // 락을 획득한 이후 leaseTime 이 지나면 락을 해제
    long leaseTime() default 2L;
}
