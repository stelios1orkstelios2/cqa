package gr.uom.cqa.logic;

import com.github.javaparser.ast.CompilationUnit;
import gr.uom.cqa.model.Issue;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {
    private final List<Rule> activeRules;

    public RuleEngine() {
        activeRules = new ArrayList<>();
        activeRules.add(new LineLengthRule());
        activeRules.add(new NamingRule());
        activeRules.add(new EmptyCatchBlockRule());
    }

    public List<Issue> evaluateAll(CompilationUnit cu, String[] codeLines) {
        List<Issue> allIssues = new ArrayList<>();

        for (Rule rule : activeRules) {
            allIssues.addAll(rule.evaluate(cu, codeLines));
        }

        return allIssues;
    }
}