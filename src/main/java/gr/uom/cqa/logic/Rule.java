package gr.uom.cqa.logic;

import com.github.javaparser.ast.CompilationUnit;
import gr.uom.cqa.model.Issue;
import java.util.List;

public interface Rule {
    List<Issue> evaluate(CompilationUnit cu, String[] codeLines);
}