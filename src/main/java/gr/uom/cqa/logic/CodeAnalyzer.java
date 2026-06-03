package gr.uom.cqa.logic;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.*;
import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;
import gr.uom.cqa.model.Severity;

import java.util.List;

public class CodeAnalyzer {

    private final RuleEngine ruleEngine;

    public CodeAnalyzer() {
        this.ruleEngine = new RuleEngine();
    }

    public Report runAnalysis(String codeContent) {
        Report report = new Report();
        String[] codeLines = codeContent.split("\\r?\\n");
        int loc = codeLines.length;

        CompilationUnit cu = null;

        try {
            cu = StaticJavaParser.parse(codeContent);
            int noc = cu.findAll(ClassOrInterfaceDeclaration.class).size();
            int nom = cu.findAll(MethodDeclaration.class).size();
            int totalCc = calculateCyclomaticComplexity(cu);

            report.setMetrics(loc, noc, nom, totalCc);

        } catch (Exception e) {
            report.setSyntaxError(true);

            report.addIssue(new Issue(1, "Συντακτικό Σφάλμα Java: Ο κώδικας δεν μπορούσε να αναλυθεί πλήρως.", Severity.CRITICAL));
            report.setMetrics(loc, 0, 0, 0);
        }

        List<Issue> foundIssues = ruleEngine.evaluateAll(cu, codeLines);
        for (Issue issue : foundIssues) {
            report.addIssue(issue);
        }

        report.calculateScore(loc);
        return report;
    }

    /**
     * Υπολογίζει την Κυκλοματική Πολυπλοκότητα βρίσκοντας όλα τα μονοπάτια ροής (if, for, while κλπ).
     */
    private int calculateCyclomaticComplexity(CompilationUnit cu) {
        int totalCc = 0;

        for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
            int methodCc = 1;

            methodCc += method.findAll(IfStmt.class).size();
            methodCc += method.findAll(ForStmt.class).size();
            methodCc += method.findAll(ForEachStmt.class).size();
            methodCc += method.findAll(WhileStmt.class).size();
            methodCc += method.findAll(DoStmt.class).size();
            methodCc += method.findAll(CatchClause.class).size();
            methodCc += method.findAll(ConditionalExpr.class).size();

            methodCc += method.findAll(BinaryExpr.class).stream()
                    .filter(expr -> expr.getOperator() == BinaryExpr.Operator.AND || expr.getOperator() == BinaryExpr.Operator.OR)
                    .count();

            totalCc += methodCc;
        }
        return totalCc;
    }
}