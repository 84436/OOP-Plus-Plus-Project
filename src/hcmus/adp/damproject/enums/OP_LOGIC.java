package hcmus.adp.damproject.enums;

public enum OP_LOGIC {
    AND("AND"),
    OR("OR"),
    ;

    private final String text;
    OP_LOGIC(final String text) {
        this.text = text;
    }
    public String toString() {
        return this.text;
    }
}
