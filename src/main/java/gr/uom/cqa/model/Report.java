package gr.uom.cqa.model;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private final List<Issue> issues;
    private int finalScore;
    private int linesOfCode;
    private int numberOfClasses;
    private int numberOfMethods;

    public Report() {
        this.issues = new ArrayList<>();
        this.finalScore = 100;
    }

    public void addIssue(Issue issue) {
        if (issue != null) {
            issues.add(issue);
        }
    }

    public List<Issue> getIssues() { return new ArrayList<>(issues); }
    public int getFinalScore() { return finalScore; }

    public void setMetrics(int loc, int noc, int nom) {
        this.linesOfCode = loc;
        this.numberOfClasses = noc;
        this.numberOfMethods = nom;
    }

    public int getLinesOfCode() { return linesOfCode; }
    public int getNumberOfClasses() { return numberOfClasses; }
    public int getNumberOfMethods() { return numberOfMethods; }

    public void calculateScore(int totalLines) {
        int penaltyPerIssue = 5;
        int totalPenalty = issues.size() * penaltyPerIssue;
        finalScore = Math.max(0, 100 - totalPenalty);
    }
}