package hcmus.adp.damproject.statement;

public interface StatementBuilderIface {
    public StatementBuilderIface from(Class<?> dataclass);
    public StatementBuilderIface where(CondStmtIface cond);
}
