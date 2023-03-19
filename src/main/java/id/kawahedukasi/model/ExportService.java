package id.kawahedukasi.model;

import com.opencsv.CSVWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ExportService {
    public Response exportPDF() throws JRException {

        // load template jasper yang sudah dibuat
        File file = new File("/Users/Genta/Kawah6/first-time-code/first-time-code/src/main/resources/testin.jrxml");

        // create datasource jasper from table Peserta
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Peserta.listAll());

        // build jasper report dari template yg telah di load
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
//
//        Map<String, Object> param = new HashMap<>();
//        param.put("DATASOURCE", jrBeanCollectionDataSource);

        // create jasperPrint object
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), jrBeanCollectionDataSource);

        // export jasperPrint to byte array
        byte[] jasperResult = JasperExportManager.exportReportToPdf(jasperPrint);

        return Response.ok().type("application/pdf").entity(jasperResult).build();
    }
    public Response exportExcel() throws IOException {

        ByteArrayOutputStream outputStream = resultOutputExcel();

        return Response.ok()
                .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Dispotition","attachment; filename=\"peserta_list_excel.xlsx\"")
                .entity(outputStream.toByteArray()).build();
    }

    public ByteArrayOutputStream resultOutputExcel () throws IOException {
        List<Peserta> pesertaList = Peserta.listAll();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("data_peserta");

        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("nama");
        row.createCell(2).setCellValue("email");
        row.createCell(3).setCellValue("phone_number");

        for (Peserta peserta : pesertaList){
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue(peserta.id);
            row.createCell(1).setCellValue(peserta.nama);
            row.createCell(2).setCellValue(peserta.email);
            row.createCell(3).setCellValue(peserta.phoneNum);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream;
    }
    public Response exportCSV() throws IOException {

        // load template jasper yang sudah dibuat
        File file = File.createTempFile("temp","");

        FileWriter fileWriter = new FileWriter(file);

        CSVWriter writer = new CSVWriter(fileWriter);

        String[] headers = {"id","name","email","phone_number"};
        writer.writeNext(headers);

        List<Peserta> pesertaList = Peserta.listAll();
        for(Peserta peserta : pesertaList){
            String[] dataRow = {
                    peserta.id.toString(),
                    peserta.nama,
                    peserta.email,
                    peserta.phoneNum
            };
            writer.writeNext(dataRow);
        }

        return Response.ok()
                .type("text/csv")
                .header("Content-Dispotition","attachment; filename=\"peserta_list_csv.csv\"")
                .entity(file).build();
    }
}
