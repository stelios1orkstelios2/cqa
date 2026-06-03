package gr.uom.cqa.logic;

import com.github.javaparser.ast.CompilationUnit;
import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Severity;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;

public class NamingRule extends AbstractRule {

    public NamingRule() {
        super("Έλεγχος Ονοματολογίας");
    }

    @Override
    public List<Issue> evaluate(CompilationUnit cu, String[] codeLines) {
        List<Issue> issues = createEmptyIssueList();

        for (int i = 0; i < codeLines.length; i++) {
            String line = codeLines[i].trim();
            if (line.matches(".*\\b(int|String|double|float)\\s+[a-zA-Z]\\s*[;=].*")) {
                issues.add(new Issue(i + 1, "Απέφυγε ονόματα μεταβλητών με ένα μόνο γράμμα. Βάλε κάτι περιγραφικό.", Severity.INFO));
            }
        }
        return issues;
    }
}