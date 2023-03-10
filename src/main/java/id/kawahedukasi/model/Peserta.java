package id.kawahedukasi.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;

@Entity
@Table(name = "peserta")
public class Peserta extends PanacheEntityBase {
    //@Id untuk menandakan variable dibawah merupakan primary key
    @Id
    //@SequenceGenerator digunakan sebagai pembuatan value otomatis sekuensial seperti id numerik
    @SequenceGenerator(name = "idPesertaSeq", sequenceName = "peserta_sequence", initialValue = 1, allocationSize = 1)
    //@GeneratedValue adalah initiator pembuat nilainya
    @GeneratedValue(generator = "idPesertaSeq")
    @Column(name ="id", nullable = false)
    public Long id;
    @Column(name = "nama")
    public String nama;
    @Column(name = "email")
    public String email;
    @Column(name = "phone_number")
    public String phoneNum;
}
