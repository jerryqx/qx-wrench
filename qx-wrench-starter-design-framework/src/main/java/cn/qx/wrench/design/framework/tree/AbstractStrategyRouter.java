package cn.qx.wrench.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractStrategyRouter<T,D,R> implements StrategyHandler<T,D,R>, StrategyMapper<T,D,R> {

    @Getter
    @Setter
    protected StrategyHandler<T,D,R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter,D dynamicContext) throws Exception{
        StrategyHandler<T,D,R> strategyHandler = get(requestParameter,dynamicContext);
        if(strategyHandler == null){
            return defaultStrategyHandler.apply(requestParameter,dynamicContext);
        }
        return strategyHandler.apply(requestParameter,dynamicContext);
    }


}
