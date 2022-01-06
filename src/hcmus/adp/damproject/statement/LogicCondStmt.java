package hcmus.adp.damproject.statement;

import hcmus.adp.damproject.enums.OP_LOGIC;

public class LogicCondStmt implements CondStmtIface {
    private CondStmtIface cond1;
    private CondStmtIface cond2;
    private OP_LOGIC op;

    public LogicCondStmt(CondStmtIface cond1, OP_LOGIC op, CondStmtIface cond2) {
        this.cond1 = cond1;
        this.cond2 = cond2;
        this.op = op;
    }

    public String toString() {
        return String.format(
            "(%s %s %s)",
            this.cond1.toString(), this.op.toString(), this.cond2.toString());
    }
}
