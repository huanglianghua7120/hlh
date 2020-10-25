package saaf.common.fmw.message.model.entities;

public class TagBeanInfo {
    private String name;

    public TagBeanInfo() {
        super();
    }

    public TagBeanInfo(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\"}"; //super.toString();
    }
}
