package cn.qx.wrench.rate.limiter.types.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface RateLimiterAccessInterceptor {

    /** 用哪个字段作为拦截标识，未配置则默认走全部 **/
    String key() default "all";

    /** 限制频次 (每秒请求次数)**/
    double permitsPerSecond();

    /** 黑名单拦截(多少次限制后加入给名单) 0 不限制**/
    double blacklistCount() default 0;

    /** 拦截后的执行方法**/
    String fallbackMethod() ;
}
