package site.bidderown.server.base.annotation.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvictByKey {
    String pattern(); // 패턴으로 삭제
    String value() default "";
    String key() default ""; // 패턴이 아닌 구체적으로 지우고 싶은 키
}
