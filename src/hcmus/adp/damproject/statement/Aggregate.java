package hcmus.adp.damproject.statement;

import hcmus.adp.damproject.enums.AGGREGATE;

public class Aggregate implements AggregateIface {
    private AGGREGATE _aggregate;
    private String _field;

    public Aggregate(AGGREGATE aggregate, String field) {
        this._aggregate = aggregate;
        this._field = field;
    }

    public String toString() {
        return String.format(
                "%s(%s)",
                this._aggregate, this._field);
    }
}
