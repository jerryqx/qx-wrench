package cn.qx.wrench.dynamic.config.center.domian.service;

import cn.qx.wrench.dynamic.config.center.domian.model.valobj.AttributeVO;

public interface IDynamicConfigCenterService {
    Object proxyObject(Object bean);
    /**
     * 调整属性值
     */
    void adjustAttributeValue(AttributeVO attributeVO);
}
