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
import com.project.jeeproject_springboot.util.MathUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceReportService {

    public static void generate(HttpServletResponse response,
                                Student student,
                                Map<Course, List<Result>> resultsByCourse,
                                Map<Course, Double> studentAveragesByCourse,
                                Map<Course, Double> courseAverages,
                                Map<Course, Integer> studentRanksByCourse,
                                Map<Course, Integer> studentCountByCourse,
                                double studentGeneralAverage) throws IOException {

        // Generate PDF
        downloadPdf(
                response,
                student,
                studentAveragesByCourse,
                courseAverages,
                studentRanksByCourse,
                studentCountByCourse,
                studentGeneralAverage
        );
    }

    private static void downloadPdf(
        HttpServletResponse response,
        Student student,
        Map<Course, Double> studentAveragesByCourse,
        Map<Course, Double> courseAverages,
        Map<Course, Integer> studentRanksByCourse,
        Map<Course, Integer> studentCountByCourse,
        double studentGeneralAverage
    ) throws IOException {
        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        // title
        document.add(new Paragraph("Rapport de Performance Étudiant")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        // student details
        document.add(new Paragraph("Nom : " + student.getLastName()));
        document.add(new Paragraph("Prénom : " + student.getFirstName()));
        document.add(new Paragraph("ID : " + student.getId()));
        // course details list
        float[] columnWidths = {40f,250f, 80f, 50f, 50f}; // Largeur des colonnes (matière, moyenne, résultat)
        Table table = new Table(columnWidths);
        table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Matière").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Moyenne des étudiants").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Rang").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Moyenne de l'étudiant").setBold()));
        document.add(new Paragraph("\nDétails par cours :").setBold());
        for (Course course : courseAverages.keySet()) {
            table.addCell(new Cell().add(new Paragraph(Integer.toString(course.getId()))));
            table.addCell(new Cell().add(new Paragraph(course.getName())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f / 20", courseAverages.get(course)))));
            table.addCell(new Cell().add(new Paragraph(String.format("%d / %d", studentRanksByCourse.get(course), studentCountByCourse.get(course)))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f / 20", studentAveragesByCourse.get(course))).setBold()));
        }
        // General average
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph("Moyenne générale").setBold()));
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph("N/A")));
        table.addCell(new Cell().add(new Paragraph(Double.toString(studentGeneralAverage)).setBold()));

        document.add(table);
        document.close();
    }




}
