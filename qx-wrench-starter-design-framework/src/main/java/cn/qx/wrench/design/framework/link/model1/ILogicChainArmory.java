package cn.qx.wrench.design.framework.link.model1;

public interface ILogicChainArmory<T,D,R> {
    ILogicLink<T,D,R> next();

    ILogicLink<T,D,R> appendNext(ILogicLink<T,D,R> next);



}
