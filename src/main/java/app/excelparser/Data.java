package app.excelparser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.javafunk.excelparser.annotations.ExcelField;
import org.javafunk.excelparser.annotations.ExcelObject;
import org.javafunk.excelparser.annotations.ParseType;

/**
 * Created by willemchua on 5/23/17.
 */

@ExcelObject(parseType = ParseType.ROW, start = 2, end = 600)
public class Data {

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

    public String getCity() { return city;}
    public String getProvider() { return provider; }
    public String getBpjs() { return bpjs; }
    public String getAddress() { return address; }
    public String getTelephone() { return telephone; }
    public String getFax() {return fax;}

//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getBlog() {
//        return blog;
//    }
//
//    public void setBlog(String blog) {
//        this.blog = blog;
//    }
//
//    @Override
//    public String toString() {
//        return "Data [name=" + name + ", blog=" + blog + "]";
//    }

}
