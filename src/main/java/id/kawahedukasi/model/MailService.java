package id.kawahedukasi.model;

import com.oracle.svm.core.annotate.Inject;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MailService {

    @Inject
    Mailer mailer;

    public void sendEmail(String email){
        mailer.send(
                Mail.withHtml(email,
                        "CRUD API Quarkus Batch 6",
                        "<h1>Hello,</h1> this is Quarkus Peserta-Service"));
    }
}
