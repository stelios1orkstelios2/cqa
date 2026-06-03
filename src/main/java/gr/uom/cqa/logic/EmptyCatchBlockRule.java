package gr.uom.cqa.logic;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.CatchClause;
import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Severity;

import java.util.List;

public class EmptyCatchBlockRule extends AbstractRule {

    public EmptyCatchBlockRule() {
        super("Έλεγχος Άδειων Catch Blocks");
    }

    @Override
    public List<Issue> evaluate(CompilationUnit cu, String[] codeLines) {
        List<Issue> issues = createEmptyIssueList();
        if (cu == null) return issues; 
        for (CatchClause catchClause : cu.findAll(CatchClause.class)) {
            if (catchClause.getBody().getStatements().isEmpty()) {
                int line = catchClause.getBegin().isPresent() ? catchClause.getBegin().get().line : 0;
                issues.add(new Issue(line, "Βρέθηκε άδειο catch block (Swallowed Exception). Πρέπει να γίνεται χειρισμός του σφάλματος.", Severity.CRITICAL));
            }
        }
        return issues;
    }
}