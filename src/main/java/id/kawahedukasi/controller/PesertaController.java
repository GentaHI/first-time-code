package id.kawahedukasi.controller;

import com.opencsv.exceptions.CsvValidationException;
import id.kawahedukasi.DTO.ImportExcelDTO;
import id.kawahedukasi.model.ExportService;
import id.kawahedukasi.model.ImportService;
import id.kawahedukasi.model.Peserta;
import net.sf.jasperreports.engine.JRException;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/peserta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PesertaController {

    @Inject
    ExportService exportService;

    @Inject
    ImportService importService;

    @GET
    public Response get(@PathParam("nama") String nama){
        return Response.status(Response.Status.OK).entity(Peserta.findAll().list()).build();
    }
    @GET
    @Path("/export/pdf")
    @Produces("application/pdf")
    public Response exportPDF() throws JRException {
        return exportService.exportPDF();
    }

    @GET
    @Path("/export/excel")
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportExcel() throws IOException {
        return exportService.exportExcel();
    }

    @GET
    @Path("/export/csv")
    @Produces("text/csv")
    public Response exportCSV() throws  IOException {
        return exportService.exportCSV();
    }

    @POST
    @Transactional
    public Response post(Map<String, Object> request){
        Peserta peserta = new Peserta();
        peserta.nama = request.get("nama").toString();
        peserta.email = request.get("email").toString();
        peserta.phoneNum = request.get("phoneNum").toString();

        // fungsi untuk melabel bahwa objek peserta siap di save ke database
        peserta.persist();
        return Response.status(Response.Status.CREATED).entity(new HashMap<>()).build();
    }

    @POST
    @Path("/import/excel")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importExcel(@MultipartForm ImportExcelDTO file) throws IOException {
        return importService.ImportExcel(file);
    }

    @POST
    @Path("/import/csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importCSV(@MultipartForm ImportExcelDTO file) throws IOException, CsvValidationException {
        return importService.ImportCSV(file);
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response put(@PathParam("id") Long id, Map<String, Object> request){
        Peserta peserta = Peserta.findById(id);
        if(peserta==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        peserta.nama = request.get("nama").toString();
        peserta.email = request.get("email").toString();
        peserta.phoneNum = request.get("phoneNum").toString();

        // fungsi untuk simpan data ke database
        peserta.persist();
        return Response.status(Response.Status.CREATED).entity(new HashMap<>()).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response del(@PathParam("id") Long id){
        Peserta peserta = Peserta.findById(id);
        if(peserta==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        peserta.delete();
        return Response.status(Response.Status.OK).entity(new HashMap<>()).build();
    }
}
