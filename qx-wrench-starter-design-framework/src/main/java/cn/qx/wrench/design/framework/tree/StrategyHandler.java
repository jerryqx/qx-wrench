package cn.qx.wrench.design.framework.tree;

public interface StrategyHandler<T, D, R> {

    StrategyHandler DEFAULT = (T, D) -> null;

   R apply(T requestParameter, D dynamicContext) throws Exception;
}
