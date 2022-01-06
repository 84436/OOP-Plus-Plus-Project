package hcmus.adp.damproject.statement;

public class SelectStatement {
    private Class<?> dataclass;
    private String finalString;

    public SelectStatement(Class<?> dataclass, String finalString) {
        this.dataclass = dataclass;
        this.finalString = finalString;
    }

    public Class<?> getDataClass() {
        return this.dataclass;
    }

    public String toString() {
        return this.finalString;
    }
}
