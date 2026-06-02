package gr.uom.cqa.logic;

import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeAnalyzer {

    private final RuleEngine ruleEngine;

    public CodeAnalyzer() {
        this.ruleEngine = new RuleEngine();
    }

    public Report runAnalysis(String codeContent) {
        Report report = new Report();
        String[] codeLines = codeContent.split("\\r?\\n");

        int loc = codeLines.length;
        int noc = 0;
        int nom = 0;

        Pattern classPattern = Pattern.compile("class\\s+[A-Za-z0-9_]+");
        Pattern methodPattern = Pattern.compile("(public|protected|private|static|\\s) +[\\w\\<\\>\\[\\]]+\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*\\(");

        for (String line : codeLines) {
            if (classPattern.matcher(line).find()) noc++;
            if (methodPattern.matcher(line).find()) nom++;
        }

        report.setMetrics(loc, noc, nom);

        List<Issue> foundIssues = ruleEngine.evaluateAll(codeLines);
        for (Issue issue : foundIssues) {
            report.addIssue(issue);
        }

        report.calculateScore(loc);
        return report;
    }
}