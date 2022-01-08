package hcmus.adp.damproject.statement;

import hcmus.adp.damproject.annotations.Row;
import hcmus.adp.damproject.enums.AGGREGATE;

public class SelectStatementBuilder implements StatementBuilderIface {
    private Class<?> _from;
    private CondStmtIface _where;
    private String _groupby;
    private CondStmtIface _having;
    private AGGREGATE _aggregate;
    private String _aggregateCol;

    public SelectStatementBuilder from(Class<?> dataclass) {
        this._from = dataclass;
        return this;
    }

    public SelectStatementBuilder where(CondStmtIface cond) {
        this._where = cond;
        return this;
    }

    public SelectStatementBuilder groupby(String row) {
        this._groupby = row;
        return this;
    }

    public SelectStatementBuilder having(CondStmtIface cond) {
        this._having = cond;
        return this;
    }

    public SelectStatementBuilder setAggregate(AGGREGATE aggregate) {
        this._aggregate = aggregate;
        return this;
    }

    public SelectStatementBuilder setAggregateCol(String col) {
        this._aggregateCol = col;
        return this;
    }

    private String getTableName() {
        String tableName = _from.getAnnotation(Row.class).tableName().toLowerCase();
        if (tableName.equals("")) {
            tableName = _from.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    public String getAggregateColName() {
        return this._aggregate + this._aggregateCol;
    }

    public SelectStatement getResult() {
        StringBuilder sb = new StringBuilder();
        // if not group
        if (this._aggregate == null) {
            sb.append("SELECT * ");
            sb.append(String.format("FROM %s ", getTableName()));

            if (this._where != null) {
                sb.append(String.format("WHERE %s ", this._where.toString()));
            }

            return new SelectStatement(this._from, sb.toString());
        } else {
            sb.append(String.format("SELECT %s(%s) as %s,* FROM %s ", this._aggregate, this._aggregateCol,
                    this.getAggregateColName(), getTableName()));

            if (this._where != null) {
                sb.append(String.format("WHERE %s ", this._where.toString()));
            }
            if (this._groupby != null) {
                sb.append(String.format("GROUP BY %s ", this._groupby));
            }
            if (this._having != null) {
                sb.append(String.format("HAVING %s", this._having));
            }

            return new SelectStatement(this._from, sb.toString()).setAggregateColName(this.getAggregateColName());
        }
    }
}
