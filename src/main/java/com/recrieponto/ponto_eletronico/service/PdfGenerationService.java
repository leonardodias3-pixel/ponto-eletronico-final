package com.recrieponto.ponto_eletronico.service;

import com.lowagie.text.*; // Certifique-se que é com.lowagie.text
import com.lowagie.text.Font; // Import específico para Font
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.recrieponto.ponto_eletronico.model.RegistroPonto;
import org.springframework.stereotype.Service;

import java.awt.Color; // Certifique-se que é java.awt.Color
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfGenerationService {

    public ByteArrayInputStream generatePdfReport(List<RegistroPonto> registros, String username) {
        // Usa ByteArrayOutputStream para criar o PDF em memória
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Cria o documento PDF (tamanho A4)
        Document document = new Document(PageSize.A4);
        try {
            // Associa o escritor de PDF ao stream de saída
            PdfWriter.getInstance(document, out);

            // Abre o documento para edição
            document.open();

            // Adiciona o Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Paragraph title = new Paragraph("Relatório Mensal de Ponto", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10); // Espaço depois do título
            document.add(title);

            // Adiciona o Subtítulo (Nome do Coordenador)
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Paragraph subtitle = new Paragraph("Coordenador: " + username, subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20); // Espaço maior antes da tabela
            document.add(subtitle);

            // Cria a Tabela com 4 colunas
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100); // Tabela ocupa toda a largura
            // Define a largura relativa das colunas (opcional, ajusta conforme necessário)
            table.setWidths(new float[]{3f, 2f, 2f, 3f});

            // Adiciona o Cabeçalho da Tabela
            addTableHeader(table);

            // Adiciona as Linhas de Dados
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

            for (RegistroPonto registro : registros) {
                table.addCell(new Phrase(registro.getDataHoraEntrada().format(dateFormatter), cellFont));
                table.addCell(new Phrase(registro.getDataHoraEntrada().format(timeFormatter), cellFont));

                String saidaStr = (registro.getDataHoraSaida() != null)
                        ? registro.getDataHoraSaida().format(timeFormatter)
                        : "--";
                table.addCell(new Phrase(saidaStr, cellFont));

                table.addCell(new Phrase(registro.getHorasTrabalhadas(), cellFont));
            }

            // Adiciona a tabela ao documento
            document.add(table);

        } catch (DocumentException e) {
            // Em uma aplicação real, trataríamos melhor este erro (log, etc.)
            e.printStackTrace();
        } finally {
            // Fecha o documento SEMPRE (mesmo se der erro)
            document.close();
        }

        // Retorna o PDF gerado como um stream de bytes
        return new ByteArrayInputStream(out.toByteArray());
    }

    // Método auxiliar para criar o cabeçalho da tabela
    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Data", "Entrada", "Saída", "Total de Horas"};
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.WHITE);

        for (String headerTitle : headers) {
            PdfPCell header = new PdfPCell();
            // Cor de fundo do cabeçalho (um azul escuro)
            header.setBackgroundColor(new Color(50, 50, 100));
            header.setBorderWidth(1);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.setPhrase(new Phrase(headerTitle, headerFont));
            header.setPadding(8); // Adiciona um respiro
            table.addCell(header);
        }
    }
}