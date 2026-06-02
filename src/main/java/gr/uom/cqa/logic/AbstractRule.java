package gr.uom.cqa.logic;

import gr.uom.cqa.model.Issue;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRule implements Rule {
    protected String ruleName;

    public AbstractRule(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }

    protected List<Issue> createEmptyIssueList() {
        return new ArrayList<>();
    }
}