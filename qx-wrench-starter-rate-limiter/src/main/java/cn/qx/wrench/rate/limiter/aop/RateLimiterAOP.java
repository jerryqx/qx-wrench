package cn.qx.wrench.rate.limiter.aop;

import cn.qx.wrench.dynamic.config.center.types.annotations.DCCValue;
import cn.qx.wrench.rate.limiter.types.annotations.RateLimiterAccessInterceptor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 */
@Aspect
public class RateLimiterAOP {

    private final Logger log = LoggerFactory.getLogger(RateLimiterAOP.class);

    @DCCValue("rateLimiterSwitch:open")
    private String rateLimiterSwitch;

    // 个人限频记录1分钟
    private final Cache<String, RateLimiter> loginRecord = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    // 个人限频黑名单24h - 分布式业务场景，可以记录到 Redis 中
    private final Cache<String, Long> blacklist = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    @Pointcut("@annotation(cn.qx.wrench.rate.limiter.types.annotations.RateLimiterAccessInterceptor)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(rateLimiterAccessInterceptor)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimiterAccessInterceptor rateLimiterAccessInterceptor) throws Throwable {
        // 0. 限流开关【open 开启、close 关闭】关闭后，不会走限流策略
        if (StringUtils.equals(rateLimiterSwitch,"close")){
            return joinPoint.proceed();
        }
        String key = rateLimiterAccessInterceptor.key();
        if (StringUtils.isBlank(key)){
            throw new RuntimeException("限流key不能为空");
        }
        // 获取拦截字段值
        String keyAttr = getAttrValue(key, joinPoint.getArgs());
        log.info("aop attr {}", keyAttr);

        // 黑名单拦截
        if (!"all".equals(keyAttr) && rateLimiterAccessInterceptor.blacklistCount() != 0 && null != blacklist.getIfPresent(keyAttr) && blacklist.getIfPresent(keyAttr) > rateLimiterAccessInterceptor.blacklistCount()) {
             log.info("限流-黑名单拦截(24h)：{}", keyAttr);
            // 调用降级方法
            return fallbackMethodResult(joinPoint, rateLimiterAccessInterceptor.fallbackMethod());
        }
        // 获取限流 -> Guava 缓存1分钟
        RateLimiter rateLimiter = loginRecord.getIfPresent(keyAttr);
        if (null == rateLimiter){
            rateLimiter = RateLimiter.create(rateLimiterAccessInterceptor.permitsPerSecond());
            loginRecord.put(keyAttr, rateLimiter);
        }


        // 限流拦截
        if (!rateLimiter.tryAcquire()){
            if (rateLimiterAccessInterceptor.blacklistCount()!=0){
                // 黑名单拦截逻辑
                if (null == blacklist.getIfPresent(keyAttr)){
                    blacklist.put(keyAttr, 1L);
                }else {
                    blacklist.put(keyAttr, blacklist.getIfPresent(keyAttr) + 1);
                }
            }
            log.info("限流-拦截：{}", keyAttr);
            return fallbackMethodResult(joinPoint, rateLimiterAccessInterceptor.fallbackMethod());
        }
        // 返回结果
        return joinPoint.proceed();
    }

    /**
     * 调用降级方法
     */
    private Object fallbackMethodResult(ProceedingJoinPoint joinPoint, String fallbackMethod) throws NoSuchMethodException,
                                                                                                     InvocationTargetException,
                                                                                                     IllegalAccessException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = joinPoint.getTarget().getClass().getMethod(fallbackMethod, methodSignature.getParameterTypes());
        return method.invoke(joinPoint.getTarget(), joinPoint.getArgs());
    }


    /**
     * 实际根据自身业务调整，主要是为了获取通过某个值做拦截
     */
    public String getAttrValue(String attr, Object[] args){
        if (args[0] instanceof String){
            return args[0].toString();
        }

        String fieldValue = null;
        for (Object arg : args) {
            try{
                if (StringUtils.isNotBlank(fieldValue)){
                    break;
                }
                fieldValue = String.valueOf(this.getValueByName(arg , attr));
            }catch (Exception e){
                log.error("获取路由属性值失败 attr：{}", attr, e);
            }
        }
        return fieldValue;
    }

    private Object getValueByName(Object item, String name){
        try{
            Field field = getFieldByName(item, name);
            if (field == null){
                return null;
            }
            field.setAccessible(true);
            Object o = field.get(item);
            field.setAccessible(false);
            return o;
        }catch (Exception e){
            return name;
        }
    }


    private Field getFieldByName(Object item, String name) {
        Class<?> clazz = item.getClass();
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                // 继续查找父类
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }

}


