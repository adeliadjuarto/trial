package app.chatbot.controller;

import app.chatbot.model.Employee;
import app.chatbot.model.Hospital;
import app.chatbot.repository.EmployeeRepository;
import app.chatbot.repository.HospitalRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javafunk.excelparser.SheetParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by adeliadjuarto on 6/16/17.
 */
@Controller
public class ViewController {
    @Value("${excel-directory-path}")
    private String directoryPath;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private HospitalRepository hospitalRepository;

    @RequestMapping("/")
    public String dashboard() throws Exception{
        return "dashboard";
    }
    @RequestMapping("/hospital")
    public String hospital(Model model) throws Exception{
        model.addAttribute("hospitals", hospitalRepository.findAll());
        return "hospital";
    }
    @RequestMapping("hospital/import-hospital")
    public String importHospital() throws Exception{
        return "importHospital";
    }
    @RequestMapping("hospital/create")
    public String createHospital() throws Exception{
        return "createHospital";
    }
    @RequestMapping(value = "/save-excel-hospital", method = RequestMethod.POST)
    public String saveExcelHospital(@RequestParam("file") MultipartFile file) throws Exception{
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(directoryPath + file.getOriginalFilename());
            System.out.println(file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // clean table
        hospitalRepository.deleteAll();

        // insert data
        InputStream in = new FileSystemResource(directoryPath + file.getOriginalFilename()).getInputStream();
        String sheetName = "RAWAT INAP";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Hospital> entityList = parser.createEntity(sheet, sheetName, Hospital.class);

        for(Hospital i: entityList) {
            if(i.getProvider() != null){
                hospitalRepository.save(i);
            }
        }

        return "redirect:/hospital";
    }

    @RequestMapping("/contact")
    public String contact(Model model) throws Exception{
        model.addAttribute("employees", employeeRepository.findAll());
        return "contact";
    }
    @RequestMapping("contact/import-contact")
    public String importContact() throws Exception{
        return "importContact";
    }
    @RequestMapping(value = "/save-excel-employee", method = RequestMethod.POST)
    public String saveExcelEmployee(@RequestParam("file") MultipartFile file) throws Exception{
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(directoryPath + file.getOriginalFilename());
            System.out.println(file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // clean table
        employeeRepository.deleteAll();

        // insert data
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("contacts-trial.xlsx");
        String sheetName = "Employee List";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Employee> entityList = parser.createEntity(sheet, sheetName, Employee.class);

        for(Employee i: entityList) {
            if(i.getEmployeeName() != null){
                employeeRepository.save(i);
            }
        }

        return "redirect:/contact";
    }
}
