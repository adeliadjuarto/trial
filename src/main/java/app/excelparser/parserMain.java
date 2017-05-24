package app.excelparser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javafunk.excelparser.SheetParser;
import java.io.InputStream;
import java.util.List;

/**
 * Created by willemchua on 5/24/17.
 */
public class parserMain {

    public parserMain() throws Exception{
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("test.xlsx");
        String sheetName = "RAWAT INAP";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Data> entityList = parser.createEntity(sheet, sheetName, Data.class);

        for(Data i: entityList) {
            if(i.getProvider() != null)
                System.out.println(i.getProvider());
        }

    }

    public static void main(String[] args) throws Exception{

        new parserMain();

    }
}
