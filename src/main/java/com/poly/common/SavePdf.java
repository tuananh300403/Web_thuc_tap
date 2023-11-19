package com.poly.common;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class SavePdf<T> {
    public void savePdf(HttpServletResponse response, List<T> listData, List<String> header) throws IOException {
        response.setContentType("application/pdf");
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(response.getOutputStream()));
        Document doc = new Document(pdfDoc, new PageSize(595, 842));
        doc.setMargins(55, 15, 35, 15);
        ILineDrawer line = new SolidLine(2);
        line.setColor(ColorConstants.LIGHT_GRAY);

        LineSeparator tableEndSeparator = new LineSeparator(line);
        tableEndSeparator.setMarginTop(10);

        doc.add(createTable(listData, header));
        doc.add(tableEndSeparator);
        doc.close();
    }

    public Table createTable(List<T> listData, List<String> header) throws IOException {
        Table table = new Table(header.size()).useAllAvailableWidth();
        for (T temp : listData) {
            List<Object> listCell = new ArrayList<>();
            Class<?> tempClass = temp.getClass();
            Field[] fields = tempClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Cho phép truy cập các thuộc tính private
                Object value = null;
                try {
                    value = field.get(temp);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                listCell.add(value);
            }
            listCell.remove(listCell.size() - 1);
            addRow(listCell, table);
        }
        addHeader(header, table);
        return table;
    }

    public void addHeader(List<String> header, Table table) throws IOException {
        for (String columnHeader : header) {
            Paragraph headerParagraph = new Paragraph(columnHeader)
                    .setFont(PdfFontFactory.createFont(StandardFonts
                            .HELVETICA_BOLD))
                    .setFontSize(10);

            Cell headerCell = new Cell()
                    .add(headerParagraph)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                    .setPadding(8);

            table.addHeaderCell(headerCell);
        }
    }

    public void addRow(List<Object> content, Table table) throws IOException {
        for (Object text : content) {
            Paragraph paragraph = new Paragraph(String.valueOf(text))
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(10);
            Cell cell = new Cell()
                    .add(paragraph)
                    .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1))
                    .setPaddingLeft(5)
                    .setPaddingTop(5)
                    .setPaddingRight(5)
                    .setPaddingBottom(5);
            table.addCell(cell);
        }
    }
}

