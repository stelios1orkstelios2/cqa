package gr.uom.cqa.ui;

import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;

public class MainUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Code Quality Analyzer (CQA)");

        Label titleLabel = new Label("Εισαγωγή Κώδικα Java προς Ανάλυση:");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        TextArea codeArea = new TextArea();
        codeArea.setPromptText("Κάνε επικόλληση τον κώδικα εδώ ή πάτα 'Ανέβασμα Αρχείου'...");
        codeArea.setPrefHeight(300);

        Button uploadBtn = new Button("Ανέβασμα Αρχείου (.java)");
        Button analyzeBtn = new Button("Ανάλυση Κώδικα");
        analyzeBtn.setStyle("-fx-background-color: #005088; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, uploadBtn, analyzeBtn);

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
                    resultArea.setText("Το αρχείο φορτώθηκε επιτυχώς! Πάτα 'Ανάλυση Κώδικα'.");
                } catch (Exception ex) {
                    resultArea.setText("Σφάλμα κατά την ανάγνωση του αρχείου: " + ex.getMessage());
                }
            }
        });

        analyzeBtn.setOnAction(e -> {
            String code = codeArea.getText();
            if (code.trim().isEmpty()) {
                resultArea.setText("Προσοχή: Δεν υπάρχει κώδικας για ανάλυση!");
                return;
            }

            // TODO: Εδώ βάζουμε "εικονικά" (dummy) δεδομένα προσωρινά.
            Report dummyReport = new Report();
            dummyReport.addIssue(new Issue(12, "Παράδειγμα: Το όνομα της μεταβλητής είναι πολύ μικρό."));
            dummyReport.addIssue(new Issue(25, "Παράδειγμα: Λείπουν τα σχόλια από τη μέθοδο."));
            dummyReport.calculateScore(50);

            StringBuilder output = new StringBuilder();
            output.append("=== ΤΕΛΙΚΟ SCORE: ").append(dummyReport.getFinalScore()).append("/100 ===\n\n");
            output.append("Προβλήματα που εντοπίστηκαν:\n");
            for (Issue issue : dummyReport.getIssues()) {
                output.append("- ").append(issue.toString()).append("\n");
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

}