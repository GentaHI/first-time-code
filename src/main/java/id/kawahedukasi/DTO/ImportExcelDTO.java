package id.kawahedukasi.DTO;

import javax.ws.rs.FormParam;

public class ImportExcelDTO {
    @FormParam("file")
    public byte[] file;
}
