package app.chatbot.model;

import lombok.Getter;
import lombok.Setter;
import org.javafunk.excelparser.annotations.ExcelField;
import org.javafunk.excelparser.annotations.ExcelObject;
import org.javafunk.excelparser.annotations.ParseType;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by willemchua on 6/12/17.
 */
@Entity
@Table(name="employee")
@Setter
@Getter
@ExcelObject(parseType = ParseType.ROW, start = 2, end = 600)
public class Employee {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer rowID;
    @ExcelField(position = 1)
    private String stsrc;
    @ExcelField(position = 2)
    private Date dateChange;
    @ExcelField(position = 3)
    private String employeeId;
    @ExcelField(position = 4)
    private String NIP;
    @ExcelField(position = 5)
    private String employeeName;
    @ExcelField(position = 6)
    private String branchID;
    @ExcelField(position = 7)
    private String divisionID;
    @ExcelField(position = 8)
    private String regionID;
    @ExcelField(position = 9)
    private String jobTitleID;
    @ExcelField(position = 10)
    private String CEK;
    @ExcelField(position = 11)
    private Date birthDate;
    @ExcelField(position = 12)
    private String homeTelp;
    @ExcelField(position = 13)
    private String handphoneTelp;
    @ExcelField(position = 14)
    private String officeTelp;
    @ExcelField(position = 15)
    private String officeExt;

}
