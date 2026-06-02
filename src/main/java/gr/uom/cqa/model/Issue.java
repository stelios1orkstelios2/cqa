package gr.uom.cqa.model;

public class Issue {
    private final int lineNumber;
    private final String description;

    public Issue(int lineNumber, String description) {
        this.lineNumber = lineNumber;
        this.description = description;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Γραμμή " + lineNumber + ": " + description;
    }
}