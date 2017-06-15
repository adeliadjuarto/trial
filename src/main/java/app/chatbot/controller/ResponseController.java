package app.chatbot.controller;

import app.chatbot.model.Employee;
import app.chatbot.model.Hospital;
import app.chatbot.repository.CategoryRepository;
import app.chatbot.repository.ContentRepository;
import app.chatbot.repository.EmployeeRepository;
import app.chatbot.repository.HospitalRepository;
import app.chatbot.model.Message;
import app.chatbot.service.ChatService;
import app.chatbot.service.FileParse;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javafunk.excelparser.SheetParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willemchua on 5/10/17.
 */
@RestController
public class ResponseController{

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private FileParse fileParse;
    @Autowired
    private ChatService chatService;

    @RequestMapping(value = "/chat",
                    method = RequestMethod.POST)
    public ArrayList<Message> response(
            @RequestBody JsonNode json
            ) throws Exception{
        String body = json.get("body").asText();
        String from = json.get("from").asText();

        chatService.chat(body, from);

        return chatService.messages;
    }

    @RequestMapping("/start")
    public Message response() throws Exception{

        System.out.println(categoryRepository.toString());
        fileParse.parse("./assets/skbca.txt");

        return new Message("Hi", "Hai juga");
    }

    @RequestMapping("/save-excel-employee")
    public @ResponseBody  String saveExcelEmployee() throws Exception{
        // clean table
        employeeRepository.deleteAll();

        // insert data
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("contacts-trial.xlsx");
        String sheetName = "Employee List";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Employee> entityList = parser.createEntity(sheet, sheetName, Employee.class);

        for(Employee i: entityList) {
            employeeRepository.save(i);
        }

        return "Save successful";
    }

    @RequestMapping("/get-all-contacts")
    public @ResponseBody  Iterable<Employee> getEmployee() throws Exception{
        return employeeRepository.findAllByOrderByEmployeeNameAsc();
    }

    @RequestMapping("/find-contact")
    public @ResponseBody  Employee findEmployee(@RequestParam("id") Integer id) throws Exception{
        return employeeRepository.findByRowID(id);
    }

    @RequestMapping("/search-contacts")
    public @ResponseBody  Iterable<Employee> searchEmployeeByName(@RequestParam("name") String name) throws Exception{
        return employeeRepository.findByEmployeeNameContaining(name);
    }

    @RequestMapping("/save-excel-hospital")
    public @ResponseBody  String saveExcelHospital() throws Exception{
        // clean table
        hospitalRepository.deleteAll();

        // insert data
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("PROVIDER_BCA_MARET_2017_CAR_revisi.xlsx");
        String sheetName = "RAWAT INAP";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Hospital> entityList = parser.createEntity(sheet, sheetName, Hospital.class);

        for(Hospital i: entityList) {
            hospitalRepository.save(i);
        }

        return "Save successful";
    }
}
