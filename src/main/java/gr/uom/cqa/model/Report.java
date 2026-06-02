package gr.uom.cqa.model;

import java.util.ArrayList;
import java.util.List;


public class Report {
    private final List<Issue> issues;
    private int finalScore;

    public Report() {
        this.issues = new ArrayList<>();
        this.finalScore = 100;
    }

    public void addIssue(Issue issue) {
        if (issue != null) {
            issues.add(issue);
        }
    }

    public List<Issue> getIssues() {
        return new ArrayList<>(issues);
    }

    /**
     * Calculates the score based on a simple penalty system.
     * For example, deduct 5 points for every issue found.
     * * @param totalLines The total number of lines analyzed (can be used for more complex scoring later).
     */
    public void calculateScore(int totalLines) {
        int penaltyPerIssue = 5;
        int totalPenalty = issues.size() * penaltyPerIssue;

        finalScore = Math.max(0, 100 - totalPenalty);
    }

    public int getFinalScore() {
        return finalScore;
    }
}