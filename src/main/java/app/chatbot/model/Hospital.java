package app.chatbot.model;

import org.javafunk.excelparser.annotations.ExcelField;
import org.javafunk.excelparser.annotations.ExcelObject;
import org.javafunk.excelparser.annotations.ParseType;

import javax.persistence.*;

/**
 * Created by adeliadjuarto on 5/26/17.
 */
@Entity
@Table(name="hospital")
@ExcelObject(parseType = ParseType.ROW, start = 2, end = 600)
public class Hospital {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @ExcelField(position = 2)
    private String city;
    @ExcelField(position = 3)
    private String provider;
    @ExcelField(position = 4)
    private String bpjs;
    @ExcelField(position = 5)
    private String address;
    @ExcelField(position = 6)
    private String telephone;
    @ExcelField(position = 7)
    private String fax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBpjs() {
        return bpjs;
    }

    public void setBpjs(String bpjs) {
        this.bpjs = bpjs;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}

