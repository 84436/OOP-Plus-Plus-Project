package hcmus.adp.damproject.statement;

import hcmus.adp.damproject.enums.OP_RELATION;

public class CondStmt implements CondStmtIface {
    private String column;
    private OP_RELATION operator;
    private Object value;

    public CondStmt(String column, OP_RELATION operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public String toString() {
        String valueStr = "";
        switch (this.value.getClass().getName()) {
            case "java.lang.String":
                valueStr = String.format("'%s'", this.value.toString());
                break;
            default:
                valueStr = this.value.toString();
                break;
        }
        return String.format(
            "%s %s %s",
            this.column, this.operator.toString(), valueStr
            );
    }
}
