package hcmus.adp.damproject.statement;

import hcmus.adp.damproject.annotations.Row;

public class SelectStatementBuilder implements StatementBuilderIface {
    private Class<?> _from;
    private CondStmtIface _where;

    public SelectStatementBuilder from(Class<?> dataclass) {
        this._from = dataclass;
        return this;
    }

    public SelectStatementBuilder where(CondStmtIface cond) {
        this._where = cond;
        return this;
    }

    private String getTableName() {
        String tableName = _from.getAnnotation(Row.class).tableName().toLowerCase();
        if (tableName.equals("")) {
            tableName = _from.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    public SelectStatement getResult() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * ");
        sb.append(String.format("FROM %s ", getTableName()));

        if (this._where != null) {
            sb.append(String.format("WHERE %s ", this._where.toString()));
        }

        return new SelectStatement(this._from, sb.toString());
    }
}
