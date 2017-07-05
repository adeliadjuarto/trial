package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;
import org.javafunk.excelparser.annotations.ExcelField;
import org.javafunk.excelparser.annotations.ExcelObject;
import org.javafunk.excelparser.annotations.ParseType;

import javax.persistence.*;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
@Entity
@Table(name="provider")
@Setter
@Getter
public class Provider {
    public Provider(){}
    public Provider(String city, String name, String bpjs, String address, String telephone, String fax, Integer providerTypeId, Integer serviceTypeId, Integer insuranceTypeId){
        this.city = city;
        this.name = name;
        this.bpjs = bpjs;
        this.address = address;
        this.telephone = telephone;
        this.fax = fax;
        this.providerTypeId = providerTypeId;
        this.serviceTypeId = serviceTypeId;
        this.insuranceTypeId = insuranceTypeId;
    }
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String city;
    private String name;
    private String bpjs;
    private String address;
    private String telephone;
    private String fax;
    @Column(name = "provider_type_id")
    private Integer providerTypeId;
    @Column(name = "service_type_id")
    private Integer serviceTypeId;
    @Column(name = "insurance_type_id")
    private Integer insuranceTypeId;
}
