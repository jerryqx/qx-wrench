package cn.qx.wrench.dynamic.config.center.domian.model.valobj;

public class AttributeVO {

    /** 键 - 属性 fileName */
    private String attribute;

    /** 值 */
    private String value;

    public AttributeVO(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public AttributeVO() {
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
