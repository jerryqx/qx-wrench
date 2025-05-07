package cn.qx.wrench.dynamic.config.center.listener;

import cn.qx.wrench.dynamic.config.center.domian.model.valobj.AttributeVO;
import cn.qx.wrench.dynamic.config.center.domian.service.IDynamicConfigCenterService;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicConfigCenterAdjustListener implements MessageListener<AttributeVO> {

    private final Logger log = LoggerFactory.getLogger(DynamicConfigCenterAdjustListener.class);


    private final IDynamicConfigCenterService dynamicConfigCenterService;

    public DynamicConfigCenterAdjustListener(IDynamicConfigCenterService dynamicConfigCenterService) {
        this.dynamicConfigCenterService = dynamicConfigCenterService;
    }

    @Override
    public void onMessage(CharSequence charSequence, AttributeVO attributeVO) {
        try{
            log.info("qx-wrench dcc config attribute:{} value:{}", attributeVO.getAttribute(), attributeVO.getValue());
            dynamicConfigCenterService.adjustAttributeValue(attributeVO);

        }catch (Exception e) {
            log.error("qx-wrench dcc config attribute:{} value:{}", attributeVO.getAttribute(), attributeVO.getValue()
                    , e);
        }
    }
}
