package hcmus.adp.damproject.entity;

public class EntityField implements Cloneable {
    public String name;
    public boolean isPrimary;
    public Object value;        
    public EntityField(String name, boolean isPrimary, Object value) {
        this.name = name;
        this.isPrimary = isPrimary;
        this.value = value;
    }

    // Deep-copy support
    public EntityField clone() {
        return new EntityField(this.name, this.isPrimary, this.value);
    }
}
