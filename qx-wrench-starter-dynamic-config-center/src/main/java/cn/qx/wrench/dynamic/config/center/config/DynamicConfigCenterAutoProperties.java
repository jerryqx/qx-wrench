package cn.qx.wrench.dynamic.config.center.config;

import cn.qx.wrench.dynamic.config.center.types.common.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "qx.wrench.config", ignoreInvalidFields = true)
public class DynamicConfigCenterAutoProperties {

    /**
     * 系统名称
     */
    private String system;


    public String getKey(String attributeName){
        return this.system + Constants.LINE + attributeName;

    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }
}
