package cn.qx.wrench.test.design.framework.biz.rule01.factory;

import cn.qx.wrench.design.framework.link.model1.ILogicLink;
import cn.qx.wrench.test.design.framework.biz.rule01.logic.RuleLogic101;
import cn.qx.wrench.test.design.framework.biz.rule01.logic.RuleLogic102;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class Rule01TradeRuleFactory {
    @Resource
    private RuleLogic101 ruleLogic101;
    @Resource
    private RuleLogic102 ruleLogic102;

    public ILogicLink<String, DynamicContext, String> openLogicLink() {
        ruleLogic101.appendNext(ruleLogic102);
        return ruleLogic101;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {
        private String age;
    }
}
