package id.kawahedukasi.controller;

import id.kawahedukasi.model.ExportService;
import id.kawahedukasi.model.Peserta;
import net.sf.jasperreports.engine.JRException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/peserta")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PesertaController {

    @Inject
    ExportService exportService;
    @GET
    public Response get(@PathParam("nama") String nama){
        return Response.status(Response.Status.OK).entity(Peserta.findAll().list()).build();
    }
    @GET
    @Path("/export")
    @Produces("application/pdf")
    public Response export() throws JRException {
        return exportService.exportPeserta();
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
