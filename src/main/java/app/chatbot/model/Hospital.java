package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;
import org.javafunk.excelparser.annotations.ExcelField;
import org.javafunk.excelparser.annotations.ExcelObject;
import org.javafunk.excelparser.annotations.ParseType;

import javax.persistence.*;

/**
 * Created by adeliadjuarto on 5/26/17.
 */
@Entity
@Table(name="hospital")
@Setter
@Getter
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
}

