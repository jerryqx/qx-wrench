package cn.qx.wrench.rate.limiter.config;

import cn.qx.wrench.rate.limiter.aop.RateLimiterAOP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterAutoConfig {

    @Bean
    public RateLimiterAOP rateLimiterAOP()
    {
        return new RateLimiterAOP();
    }
}
