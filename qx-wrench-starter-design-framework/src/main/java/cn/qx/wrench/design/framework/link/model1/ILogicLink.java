package cn.qx.wrench.design.framework.link.model1;

public interface ILogicLink<T,D,R>  extends ILogicChainArmory<T, D, R>{

    R apply(T requestParameter,D dynamicContext) throws Exception ;

 }
