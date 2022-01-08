package hcmus.adp.damproject.statement;

public class SelectStatement {
    private Class<?> dataclass;
    private String finalString;
    private String aggregateColName;

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
    public SelectStatement setAggregateColName(String aggregateColName){
        this.aggregateColName = aggregateColName;
        return this;
    }
    public String getAggregateColName(){
        return this.aggregateColName;
    }
}
