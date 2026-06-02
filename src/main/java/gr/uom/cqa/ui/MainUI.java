package gr.uom.cqa.ui;

import gr.uom.cqa.logic.CodeAnalyzer;
import gr.uom.cqa.logic.FileManager;
import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;
import gr.uom.cqa.model.Severity;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainUI extends Application {
    private final CodeAnalyzer analyzer = new CodeAnalyzer();
    private final FileManager fileManager = new FileManager();

    private Report currentReport;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Code Quality Analyzer (CQA)");

        Label titleLabel = new Label("Εισαγωγή Κώδικα Java προς Ανάλυση:");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextArea codeArea = new TextArea();
        codeArea.setPromptText("Κάνε επικόλληση τον κώδικα εδώ...");
        codeArea.setPrefHeight(300);

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


        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Επιλογή Αρχείου Java");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java Files", "*.java"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                try {
                    String content = Files.readString(selectedFile.toPath());
                    codeArea.setText(content);
                    resultArea.setText("Το αρχείο φορτώθηκε.");
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

            for (Issue issue : currentReport.getIssues()) {
                output.append(issue.toString()).append("\n");
            }
            resultArea.setText(output.toString());

            saveBtn.setDisable(false);
        });

        saveBtn.setOnAction(e -> {
            StringBuilder output = new StringBuilder();
            output.append("=== ΤΕΛΙΚΟ SCORE: ").append(currentReport.getFinalScore()).append("/100 ===\n\n");
            output.append("--- ΜΕΤΡΙΚΕΣ ΠΟΙΟΤΗΤΑΣ (Metrics) ---\n");
            output.append("Lines of Code (LoC): ").append(currentReport.getLinesOfCode()).append("\n");
            output.append("Number of Classes (NOC): ").append(currentReport.getNumberOfClasses()).append("\n");
            output.append("Number of Methods (NOM): ").append(currentReport.getNumberOfMethods()).append("\n\n");

            output.append("--- ΠΡΟΒΛΗΜΑΤΑ ΠΟΥ ΕΝΤΟΠΙΣΤΗΚΑΝ ---\n");
            if (currentReport.getIssues().isEmpty()) {
                output.append("Τέλειος κώδικας! Δεν βρέθηκαν σφάλματα.\n");
            } else {
                for (Issue issue : currentReport.getIssues()) {
                    output.append(issue.toString()).append("\n");
                }
            }

            resultArea.setText(output.toString());
        });

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(titleLabel, codeArea, buttonBox, resultLabel, resultArea);

        Scene scene = new Scene(mainLayout, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}