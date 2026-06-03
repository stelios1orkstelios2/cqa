package gr.uom.cqa.model;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private final List<Issue> issues;
    private int finalScore;
    private int linesOfCode;
    private int numberOfClasses;
    private int numberOfMethods;
    private int cyclomaticComplexity;

    private boolean hasSyntaxError = false;

    public Report() {
        this.issues = new ArrayList<>();
        this.finalScore = 100;
    }

    public void setSyntaxError(boolean hasSyntaxError) {
        this.hasSyntaxError = hasSyntaxError;
    }

    public void addIssue(Issue issue) {
        if (issue != null) { issues.add(issue); }
    }

    public List<Issue> getIssues() { return new ArrayList<>(issues); }
    public int getFinalScore() { return finalScore; }

    public void setMetrics(int loc, int noc, int nom, int cc) {
        this.linesOfCode = loc;
        this.numberOfClasses = noc;
        this.numberOfMethods = nom;
        this.cyclomaticComplexity = cc;
    }

    public int getLinesOfCode() { return linesOfCode; }
    public int getNumberOfClasses() { return numberOfClasses; }
    public int getNumberOfMethods() { return numberOfMethods; }
    public int getCyclomaticComplexity() { return cyclomaticComplexity; }

    public void calculateScore(int totalLines) {
        if (hasSyntaxError) {
            this.finalScore = 0;
            return; 
        }

        double totalPenalty = 0;

        for (Issue issue : issues) {
            switch (issue.getSeverity()) {
                case CRITICAL: 
                    totalPenalty += 10.0; 
                    break;
                case WARNING:  
                    totalPenalty += 5.0; 
                    break;
                case INFO:     
                    totalPenalty += 2.0; 
                    break;
            }
        }

        if (numberOfMethods > 0) {
            double avgCc = (double) cyclomaticComplexity / numberOfMethods;
            if (avgCc > 10.0) { 
                totalPenalty += 15.0;
            } else if (avgCc > 5.0) {
                totalPenalty += 5.0;
            }
        }

        if (totalLines > 0) {
            double sizeFactor = Math.log10(totalLines); 
            if (sizeFactor > 1) {
                totalPenalty = totalPenalty / (sizeFactor * 0.8);
            }
        }

        finalScore = (int) Math.max(0, 100 - Math.round(totalPenalty));
    }
}