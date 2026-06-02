package gr.uom.cqa.logic;

import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;
import java.util.List;

public class CodeAnalyzer {
    private final RuleEngine ruleEngine;

    public CodeAnalyzer() {
        this.ruleEngine = new RuleEngine();
    }

    /**
     * Η κύρια μέθοδος που καλείται από το UI (MainUI).
     * * @param codeContent Όλος ο πηγαίος κώδικας που έκανε επικόλληση ή ανέβασε ο χρήστης.
     * @return Το τελικό αντικείμενο Report με το σκορ και τα σφάλματα.
     */
    public Report runAnalysis(String codeContent) {
        Report report = new Report();

        String[] codeLines = codeContent.split("\\r?\\n");

        List<Issue> foundIssues = ruleEngine.evaluateAll(codeLines);

        for (Issue issue : foundIssues) {
            report.addIssue(issue);
        }

        report.calculateScore(codeLines.length);

        return report;
    }
}