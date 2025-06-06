package cn.qx.wrench.test.design.framework.biz.tree.node;


import cn.qx.wrench.design.framework.tree.StrategyHandler;
import cn.qx.wrench.test.design.framework.biz.tree.AbstractXxxSupport;
import cn.qx.wrench.test.design.framework.biz.tree.factory.DefaultStrategyFactory;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberLevel2Node extends AbstractXxxSupport {

    @Override
    protected String doApply(String requestParameter, DefaultStrategyFactory.DynamicContext dynamicContext) throws Exception {
        log.info("【级别节点-2】规则决策树 userId:{}", requestParameter);
        return "level2" + JSON.toJSONString(dynamicContext);
    }

    @Override
    public StrategyHandler<String, DefaultStrategyFactory.DynamicContext, String> get(String requestParameter, DefaultStrategyFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

}
