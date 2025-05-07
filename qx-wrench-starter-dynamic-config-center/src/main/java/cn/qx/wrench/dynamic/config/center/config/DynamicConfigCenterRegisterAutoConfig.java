package cn.qx.wrench.dynamic.config.center.config;

import cn.qx.wrench.dynamic.config.center.domian.model.valobj.AttributeVO;
import cn.qx.wrench.dynamic.config.center.domian.service.DynamicConfigCenterService;
import cn.qx.wrench.dynamic.config.center.domian.service.IDynamicConfigCenterService;
import cn.qx.wrench.dynamic.config.center.listener.DynamicConfigCenterAdjustListener;
import cn.qx.wrench.dynamic.config.center.types.common.Constants;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        DynamicConfigCenterAutoProperties.class,
        DynamicConfigCenterRegisterAutoProperties.class})
public class DynamicConfigCenterRegisterAutoConfig {

    @Bean("qxWrenchRedissonClient")
    public RedissonClient redissonClient(DynamicConfigCenterRegisterAutoProperties properties) {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize());
        return Redisson.create(config);
    }


    @Bean
    public IDynamicConfigCenterService dynamicConfigCenterRegisterService(RedissonClient qxWrenchRedissonClient,
                                                                         DynamicConfigCenterAutoProperties autoProperties) {
        return new DynamicConfigCenterService( autoProperties,qxWrenchRedissonClient);
    }

    @Bean
    public DynamicConfigCenterAdjustListener dynamicConfigCenterAdjustListener(IDynamicConfigCenterService service) {
        return new DynamicConfigCenterAdjustListener(service);
    }


    @Bean(name = "dynamicConfigCenterRedisTopic")
    public RTopic dynamicConfigCenterRedisTopic(DynamicConfigCenterAutoProperties autoProperties,
                                                 RedissonClient client,
                                                 DynamicConfigCenterAdjustListener listener) {
        RTopic topic = client.getTopic(Constants.getTopic(autoProperties.getSystem()));
        topic.addListener(AttributeVO.class, listener);
        return topic;
    }
}
