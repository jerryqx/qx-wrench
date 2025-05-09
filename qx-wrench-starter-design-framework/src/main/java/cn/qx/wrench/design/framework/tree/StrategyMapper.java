package cn.qx.wrench.design.framework.tree;


public interface StrategyMapper<T,D,R> {

    /**
     * 获取待执行策略
     * @param requestParameter 入参
     * @param dynamicContext 上下文
     * @return 返参
     * @throws Exception
     */
    StrategyHandler<T,D,R> get(T requestParameter,D dynamicContext) throws Exception;

}
