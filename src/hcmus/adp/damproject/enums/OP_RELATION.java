package hcmus.adp.damproject.enums;

public enum OP_RELATION {
    EQ("="), EQUAL("="),
    LT("<"), LESS_THAN("<"),
    GT(">"), GREATER_THAN(">"),
    LE("<="), LESS_THAN_OR_EQUAL("<="),
    GE(">="), GREATER_THAN_OR_EQUAL(">="),
    NEQ("!="), NOT_EQUAL("!=")
    ;

    private final String text;
    OP_RELATION(final String text) {
        this.text = text;
    }
    public String toString() {
        return this.text;
    }
}
