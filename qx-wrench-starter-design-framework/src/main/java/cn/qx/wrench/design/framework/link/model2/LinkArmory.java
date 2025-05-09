package cn.qx.wrench.design.framework.link.model2;

import cn.qx.wrench.design.framework.link.model2.chain.BusinessLinkedList;
import cn.qx.wrench.design.framework.link.model2.handler.ILogicHandler;

public class LinkArmory<T, D, R> {

    private final BusinessLinkedList<T, D, R> logicLink;

    @SafeVarargs
    public LinkArmory(String linkName, ILogicHandler<T,D,R>... logicHanders) {
        logicLink = new BusinessLinkedList<>(linkName);
        for (ILogicHandler<T,D,R> logicHander : logicHanders) {
            logicLink.add(logicHander);
        }
    }

    public BusinessLinkedList<T, D, R> getLogicLink() {
        return logicLink;
    }
}
