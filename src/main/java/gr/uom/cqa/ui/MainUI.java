package gr.uom.cqa.ui;

import gr.uom.cqa.logic.CodeAnalyzer;
import gr.uom.cqa.logic.FileManager;
import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainUI extends Application {

    private final CodeAnalyzer analyzer = new CodeAnalyzer();
    private final FileManager fileManager = new FileManager();
    private Report currentReport;

    // --- Regex για το Syntax Highlighting της Java ---
    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Code Quality Analyzer (CQA) - Pro Edition");

        Label titleLabel = new Label("Εισαγωγή Κώδικα Java προς Ανάλυση:");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // --- ΕΔΩ ΜΠΑΙΝΕΙ ΤΟ ΝΕΟ CODE AREA ΑΝΤΙ ΓΙΑ TEXTAREA ---
        CodeArea codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea)); // Αρίθμηση γραμμών!
        VBox.setVgrow(codeArea, Priority.ALWAYS); // Να πιάνει όλο τον διαθέσιμο χώρο

        // Listener για να χρωματίζει τον κώδικα κάθε φορά που πληκτρολογείς ή κάνεις paste
        codeArea.textProperty().addListener((obs, oldText, newText) -> {
            codeArea.setStyleSpans(0, computeHighlighting(newText));
        });

        Button uploadBtn = new Button("Ανέβασμα (.java)");
        Button analyzeBtn = new Button("Ανάλυση Κώδικα");
        analyzeBtn.setStyle("-fx-background-color: #005088; -fx-text-fill: white; -fx-font-weight: bold;");

        Button saveBtn = new Button("Αποθήκευση Αναφοράς");
        saveBtn.setDisable(true);

        HBox buttonBox = new HBox(10, uploadBtn, analyzeBtn, saveBtn);

        Label resultLabel = new Label("Αποτελέσματα Αξιολόγησης:");
        resultLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefHeight(200);

        // --- Events ---
        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                try {
                    String content = Files.readString(selectedFile.toPath());
                    codeArea.replaceText(0, codeArea.getLength(), content); // Σωστός τρόπος για το CodeArea
                    resultArea.setText("Το αρχείο φορτώθηκε επιτυχώς! Πάτα 'Ανάλυση Κώδικα'.");
                } catch (Exception ex) {
                    resultArea.setText("Σφάλμα: " + ex.getMessage());
                }
            }
        });

        analyzeBtn.setOnAction(e -> {
            String code = codeArea.getText();
            if (code.trim().isEmpty()) {
                resultArea.setText("Δεν υπάρχει κώδικας για ανάλυση!");
                return;
            }

            currentReport = analyzer.runAnalysis(code);

            StringBuilder output = new StringBuilder();
            output.append("=== ΤΕΛΙΚΟ SCORE: ").append(currentReport.getFinalScore()).append("/100 ===\n\n");

            output.append("--- ΜΕΤΡΙΚΕΣ ΠΟΙΟΤΗΤΑΣ ---\n");
            output.append("LoC: ").append(currentReport.getLinesOfCode()).append(" | ");
            output.append("NOC: ").append(currentReport.getNumberOfClasses()).append(" | ");
            output.append("NOM: ").append(currentReport.getNumberOfMethods()).append(" | ");
            output.append("CC: ").append(currentReport.getCyclomaticComplexity()).append("\n\n");

            output.append("--- ΠΡΟΒΛΗΜΑΤΑ ---\n");
            if (currentReport.getIssues().isEmpty()) {
                output.append("Εξαιρετικά! Δεν βρέθηκαν σφάλματα.\n");
            } else {
                for (Issue issue : currentReport.getIssues()) {
                    output.append(issue.toString()).append("\n");
                }
            }
            resultArea.setText(output.toString());
            saveBtn.setDisable(false);
        });

        saveBtn.setOnAction(e -> {
            if (currentReport != null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("cqa_report.txt");
                File destFile = fileChooser.showSaveDialog(primaryStage);
                if (destFile != null) {
                    try {
                        fileManager.saveReport(currentReport, destFile);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Η αναφορά αποθηκεύτηκε επιτυχώς!");
                        alert.showAndWait();
                    } catch (IOException ex) {
                        resultArea.appendText("\n[Σφάλμα: " + ex.getMessage() + "]");
                    }
                }
            }
        });

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(titleLabel, codeArea, buttonBox, resultLabel, resultArea);

        Scene scene = new Scene(mainLayout, 850, 700);
        // Εισαγωγή του CSS
        scene.getStylesheets().add(getClass().getResource("/gr/uom/cqa/java-keywords.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Μέθοδος που υπολογίζει τα χρώματα (Syntax Highlighting)
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                            matcher.group("BRACE") != null ? "brace" :
                            matcher.group("BRACKET") != null ? "bracket" :
                            matcher.group("SEMICOLON") != null ? "semicolon" :
                            matcher.group("STRING") != null ? "string" :
                            matcher.group("COMMENT") != null ? "comment" :
                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public static void main(String[] args) {
        launch(args);
    }
}