package com.project.jeeproject_springboot.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.project.jeeproject_springboot.model.Course;
import com.project.jeeproject_springboot.model.Result;
import com.project.jeeproject_springboot.model.Student;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TranscriptService {

    public static void generate(HttpServletResponse response, Student student, Map<Course, List<Result>> resultsByCourse, Map<Course, Double> averageByCourse, String comment) throws IOException {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.addNewPage();
        Document document = new Document(pdfDoc);

        // Ajouter le contenu au PDF
        document.add(new Paragraph("Relevé de Notes")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Nom : " + student.getLastName()));
        document.add(new Paragraph("Prénom : " + student.getFirstName()));

        // Tableau des matières et moyennes
        document.add(new Paragraph("\nMoyennes par Matière :").setBold());

        // Création du tableau avec deux colonnes
        float[] columnWidths = {50f,250f, 100f, 100f}; // Largeur des colonnes (matière, moyenne, résultat)
        Table table = new Table(columnWidths);
        table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Matière").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Moyenne").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Résultat").setBold()));

        double averageSum = 0;
        double coursesNum = 0;
        for (Map.Entry<Course, Double> entry : averageByCourse.entrySet()) {
            Course course = entry.getKey();
            Double average = entry.getValue();

            // Ajouter une ligne pour chaque matière et sa moyenne
            table.addCell(new Cell().add(new Paragraph(Integer.toString(course.getId()))));
            table.addCell(new Cell().add(new Paragraph(course.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f / 20", average))));
            table.addCell(new Cell().add(new Paragraph(average>=10? "ADM": "AJ")));

            averageSum += average;
            coursesNum++;
        }

        // moyenne générale
        double globalAverage = averageSum/coursesNum;
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph("Moyenne générale").setBold()));
        table.addCell(new Cell().add(new Paragraph(Double.toString(globalAverage)).setBold()));
        table.addCell(new Cell().add(new Paragraph(globalAverage>=10? "ADM": "AJ").setBold()));

        // Ajouter le tableau au document
        document.add(table);

        // apréciation
        document.add(new Paragraph("\nAppréciation :").setBold());
        document.add(new Paragraph(comment));

        // information
        document.add(new Paragraph("\n\nSignification des codes résultats :"));
        document.add(new Paragraph("AJ : Ajourné  ADM : Admis"));
        document.close();
    }
}
