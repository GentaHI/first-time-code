package id.kawahedukasi.controller;

import com.oracle.svm.core.annotate.Inject;
import id.kawahedukasi.model.MailService;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path("/mail")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MailController {

    @Inject
    MailService mailService;

    @GET
    @Blocking
    public Response sendEmail(Map<String, Object> request) throws IOException {
        mailService.sendEmail(request.get("email").toString());
        return Response.ok(new HashMap<>()).build();
    }
}
