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

        if (cu == null) return issues; // Αν ο κώδικας έχει συντακτικό λάθος, δεν μπορούμε να ψάξουμε

        // Η μαγεία του JavaParser: Βρίσκει ΟΛΑ τα catch blocks του αρχείου αμέσως!
        for (CatchClause catchClause : cu.findAll(CatchClause.class)) {
            // Αν το block {} δεν έχει καμία εντολή μέσα
            if (catchClause.getBody().getStatements().isEmpty()) {
                // Βρες τη γραμμή (αν υπάρχει)
                int line = catchClause.getBegin().isPresent() ? catchClause.getBegin().get().line : 0;
                issues.add(new Issue(line, "Βρέθηκε άδειο catch block (Swallowed Exception). Πρέπει να γίνεται χειρισμός του σφάλματος.", Severity.CRITICAL));
            }
        }
        return issues;
    }
}