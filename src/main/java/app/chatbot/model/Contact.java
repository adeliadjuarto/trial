package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by adeliadjuarto on 6/19/17.
 */
@Entity
@Table(name="contact")
@Setter
@Getter
public class Contact {
    protected Contact () {}
    public Contact (String stsrc, Date dateChange, String employeeId, String nip, String name, String branchId, String divisionId, String regionId, String jobtitleId, String cek, Date birthdate) {
        this.stsrc = stsrc;
        this.dateChange = dateChange;
        this.employeeId = employeeId;
        this.nip = nip;
        this.name = name;
        this.branchId = branchId;
        this.divisionId = divisionId;
        this.regionId = regionId;
        this.jobtitleId = jobtitleId;
        this.cek = cek;
        this.birthdate = birthdate;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(
            name = "system-uuid",
            strategy = "uuid2")
    @Column(name="id")
    private String id;
    @Column(name="stsrc")
    private String stsrc;
    @Column(name="date_change")
    private Date dateChange;
    @Column(name="employee_id")
    private String employeeId;
    @Column(name="nip")
    private String nip;
    @Column(name="name")
    private String name;
    @Column(name="branch_id")
    private String branchId;
    @Column(name="division_id")
    private String divisionId;
    @Column(name="region_id")
    private String regionId;
    @Column(name="jobtitle_id")
    private String jobtitleId;
    @Column(name="cek")
    private String cek;
    @Column(name="birthdate")
    private Date birthdate;
//    @OneToMany
//    private PhoneNumber[] phoneNumber;
}
