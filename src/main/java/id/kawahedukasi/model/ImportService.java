package id.kawahedukasi.model;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import id.kawahedukasi.DTO.ImportExcelDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ImportService {
    @Transactional
    public Response ImportExcel(ImportExcelDTO request) throws IOException {
        //create object array input
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.file);

        // membuat file excel / workbook dalam memori
        XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream);

        // mengambil sheet ke 0 dari semua sheet yg ada di workbook
        XSSFSheet sheet = workbook.getSheetAt(0);

        // menghapus baris ke 0 dari sheet 0
        sheet.removeRow(sheet.getRow(0));

        // membuat list
        List<Peserta> toPersiste = new ArrayList<>();

        for (Row row : sheet) {
            Peserta peserta = new Peserta();
            peserta.nama = row.getCell(0).getStringCellValue();
            peserta.email = row.getCell(1).getStringCellValue();
            peserta.phoneNum = row.getCell(2).getStringCellValue();
            toPersiste.add(peserta);
        }

        Peserta.persist(toPersiste);
        return Response.status(Response.Status.CREATED).entity(new HashMap<>()).build();
    }

    @Transactional
    public Response ImportCSV(ImportExcelDTO request) throws IOException, CsvValidationException {

        // membuat tipe data file temporal untuk dibaca oleh dependency opencsv
        File file = File.createTempFile("temp","");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(request.file);

        // deklarasi CSVReader
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextline;
        // skip baris 1 karena baris 1 merupakan label
        reader.skip(1);

        List<Peserta> toPersiste = new ArrayList<>();
        while ((nextline = reader.readNext()) != null) {
            Peserta peserta = new Peserta();
            peserta.nama = nextline[0].trim();
            peserta.email = nextline[1].trim();
            peserta.phoneNum = nextline[2].trim();
            toPersiste.add(peserta);
        }

        Peserta.persist(toPersiste);
        return Response.status(Response.Status.CREATED).entity(new HashMap<>()).build();
    }
}
