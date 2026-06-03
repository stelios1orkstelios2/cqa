package gr.uom.cqa.logic;

import com.github.javaparser.ast.CompilationUnit;
import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Severity;
import java.util.List;

public class LineLengthRule extends AbstractRule {

    public LineLengthRule() {
        super("Έλεγχος Μήκους Γραμμής");
    }

    @Override
    public List<Issue> evaluate(CompilationUnit cu, String[] codeLines) {
        List<Issue> issues = createEmptyIssueList();

        for (int i = 0; i < codeLines.length; i++) {
            if (codeLines[i].length() > 100) {
                issues.add(new Issue(i + 1, "Η γραμμή ξεπερνά τους 100 χαρακτήρες. Κάνε την πιο ευανάγνωστη.", Severity.WARNING));
            }
        }
        return issues;
    }
}