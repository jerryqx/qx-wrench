package cn.qx.wrench.dynamic.config.center.config;

import cn.qx.wrench.dynamic.config.center.domian.service.IDynamicConfigCenterService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class DynamicConfigCenterAutoConfig implements BeanPostProcessor {

    private final IDynamicConfigCenterService dynamicConfigCenterService;

    public DynamicConfigCenterAutoConfig(IDynamicConfigCenterService dynamicConfigCenterService) {
        this.dynamicConfigCenterService = dynamicConfigCenterService;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return dynamicConfigCenterService.proxyObject(bean);
    }
}
