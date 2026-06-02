package gr.uom.cqa.logic;

import gr.uom.cqa.model.Issue;
import java.util.List;

public interface Rule {
    List<Issue> evaluate(String[] codeLines);
}