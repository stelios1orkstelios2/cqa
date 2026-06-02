package gr.uom.cqa.logic;

import gr.uom.cqa.model.Issue;
import gr.uom.cqa.model.Report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    /**
     * Αποθηκεύει τα αποτελέσματα της αναφοράς σε ένα αρχείο .txt.
     *
     * @param report Η αναφορά (Report) που περιέχει το score και τα issues.
     * @param destFile Το αρχείο προορισμού όπου θα αποθηκευτούν τα δεδομένα.
     * @throws IOException Αν υπάρξει πρόβλημα κατά την εγγραφή στο αρχείο.
     */
    public void saveReport(Report report, File destFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destFile))) {
            writer.write("=== Code Quality Analyzer - Report ===\n");
            writer.write("Τελικό Score: " + report.getFinalScore() + "/100\n\n");
            writer.write("Λεπτομέρειες Σφαλμάτων:\n");

            if (report.getIssues().isEmpty()) {
                writer.write("Εξαιρετικά! Δεν βρέθηκαν προβλήματα ποιότητας κώδικα.\n");
            } else {
                for (Issue issue : report.getIssues()) {
                    writer.write(issue.toString() + "\n");
                }
            }
        }
    }
}