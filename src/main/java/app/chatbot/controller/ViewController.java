package app.chatbot.controller;

import app.chatbot.model.Contact;
import app.chatbot.model.Employee;
import app.chatbot.model.Hospital;
import app.chatbot.model.PhoneNumber;
import app.chatbot.repository.ContactRepository;
import app.chatbot.repository.EmployeeRepository;
import app.chatbot.repository.HospitalRepository;
import app.chatbot.repository.PhoneNumberRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.javafunk.excelparser.SheetParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private ContactRepository contactRepository;
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;
    @Autowired
    private HospitalRepository hospitalRepository;

    @RequestMapping("/")
    public String dashboard() throws Exception{
        return "dashboard";
    }
    @RequestMapping("/hospital")
    public String hospital(Model model) throws Exception{
        model.addAttribute("hospitals", hospitalRepository.findAll());
        return "hospital/index";
    }
    @RequestMapping("hospital/import-hospital")
    public String importHospital() throws Exception{
        return "hospital/import";
    }
    @RequestMapping("hospital/create")
    public String createHospital(Model model) throws Exception{
        model.addAttribute("hospital", new Hospital());
        return "hospital/create";
    }
    @RequestMapping(value = "hospital/add", method = RequestMethod.POST)
    public String addCustomer(@ModelAttribute Hospital hospital) {
        hospitalRepository.save(hospital);
        return "redirect:/hospital";
    }
    @RequestMapping("hospital/edit/{id}")
    public String editHospital(Model model, @PathVariable(value="id") int id) throws Exception{
        model.addAttribute("hospital", hospitalRepository.findOne(id));
        return "hospital/edit";
    }
    @RequestMapping(value = "hospital/update", method = RequestMethod.POST)
    public String updateHospital(@ModelAttribute Hospital hospital) {
        hospitalRepository.save(hospital);
        return "redirect:/hospital";
    }
    @RequestMapping(value = "hospital/delete/{id}")
    public String deleteHospital(@PathVariable(value="id") int id) {
        hospitalRepository.delete(id);
        return "redirect:/hospital";
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
        return "contact/index";
    }
    @RequestMapping("contact/import-contact")
    public String importContact() throws Exception{
        return "contact/import";
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
        //employeeRepository.deleteAll();
        contactRepository.deleteAll();

        // insert data
        InputStream in = new FileSystemResource(directoryPath + file.getOriginalFilename()).getInputStream();
        String sheetName = "Employee List";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Employee> entityList = parser.createEntity(sheet, sheetName, Employee.class);
        int test = 0;
        PhoneNumber phoneNumber;
        for(Employee i: entityList) {
            if(i.getEmployeeName() != null){
                String uuid = "test"+test;
                Contact contact = new Contact(uuid, i.getStsrc(), i.getDateChange(), i.getEmployeeId(), i.getNIP(), i.getEmployeeName(), i.getBranchID(), i.getDivisionID(), i.getRegionID(), i.getJobTitleID(), i.getCEK(), i.getBirthDate());
                contactRepository.save(contact);
                phoneNumber = new PhoneNumber(uuid+"1", uuid, "home", i.getHomeTelp(), null);
                phoneNumberRepository.save(phoneNumber);
                phoneNumber = new PhoneNumber(uuid+"2", uuid, "handphone", i.getHandphoneTelp(), null);
                phoneNumberRepository.save(phoneNumber);
                phoneNumber = new PhoneNumber(uuid+"3", uuid, "office", i.getOfficeTelp(), i.getOfficeExt());
                phoneNumberRepository.save(phoneNumber);
                test++;
            }
        }

        return "redirect:/contact";
    }
}
