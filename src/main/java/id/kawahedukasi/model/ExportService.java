package id.kawahedukasi.model;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ExportService {
    public Response exportPeserta() throws JRException {

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
}
