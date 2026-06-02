package gr.uom.cqa.logic;

import gr.uom.cqa.model.Issue;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    private final List<Rule> activeRules;

    public RuleEngine() {
        activeRules = new ArrayList<>();
        activeRules.add(new LineLengthRule());
        activeRules.add(new NamingRule());
    }

    public List<Issue> evaluateAll(String[] codeLines) {
        List<Issue> allIssues = new ArrayList<>();

        for (Rule rule : activeRules) {
            allIssues.addAll(rule.evaluate(codeLines));
        }

        return allIssues;
    }
}