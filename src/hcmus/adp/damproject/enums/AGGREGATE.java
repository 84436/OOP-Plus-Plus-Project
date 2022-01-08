package hcmus.adp.damproject.enums;

public enum AGGREGATE {
    COUNT("COUNT"),
    AVG("AVG"),
    MAX("MAX"),
    MIN("MIN"),
    SUM("SUM")
    ;

    private final String text;
    AGGREGATE(final String text) {
        this.text = text;
    }
    public String toString() {
        return this.text;
    }
}