package gr.uom.cqa.model;

public class Issue {
    private final int lineNumber;
    private final String description;
    private final Severity severity;

    public Issue(int lineNumber, String description, Severity severity) {
        this.lineNumber = lineNumber;
        this.description = description;
        this.severity = severity;
    }

    public int getLineNumber() { return lineNumber; }
    public String getDescription() { return description; }
    public Severity getSeverity() { return severity; }

    @Override
    public String toString() {
        return "[" + severity + "] Γραμμή " + lineNumber + ": " + description;
    }
}