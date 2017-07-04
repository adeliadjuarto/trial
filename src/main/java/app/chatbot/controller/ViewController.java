package app.chatbot.controller;

import app.chatbot.model.Contact;
import app.chatbot.model.Employee;
import app.chatbot.model.Hospital;
import app.chatbot.model.PhoneNumber;
import app.chatbot.repository.ContactRepository;
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
import java.text.ParsePosition;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Created by adeliadjuarto on 6/16/17.
 */
@Controller
public class ViewController {
    @Value("${excel-directory-path}")
    private String directoryPath;

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
    public String addHospital(@ModelAttribute Hospital hospital) {
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
        model.addAttribute("employees", contactRepository.findAll());
        return "contact/index";
    }
    @RequestMapping("contact/import-contact")
    public String importContact() throws Exception{
        return "contact/import";
    }

    @RequestMapping("contact/create")
    public String createContact(Model model) throws Exception{
        model.addAttribute("contact", new Contact());
        return "contact/create";
    }
    @RequestMapping(value = "contact/add", method = RequestMethod.POST)
    public String addContact(@ModelAttribute Contact contact, @RequestParam("birthDate") String birthDate, @RequestParam("mobile") String mobile, @RequestParam("home") String home, @RequestParam("office") String office, @RequestParam("officeExt") String officeExt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedBirthdate = dateFormat.parse(birthDate, parsePosition);
        contact.setBirthdate(parsedBirthdate);
        contact.setDateChange(new Date());
        Contact c = contactRepository.save(contact);
        PhoneNumber phoneNumber;
        phoneNumber = new PhoneNumber(c.getId(), "home", home, null, "home");
        phoneNumberRepository.save(phoneNumber);
        phoneNumber = new PhoneNumber(c.getId(), "handphone", mobile, null, "mobile");
        phoneNumberRepository.save(phoneNumber);
        phoneNumber = new PhoneNumber(c.getId(), "office", office, officeExt, "building");
        phoneNumberRepository.save(phoneNumber);
        return "redirect:/contact";
    }
    @RequestMapping("contact/edit/{id}")
    public String editContact(Model model, @PathVariable(value="id") String id) throws Exception{
        model.addAttribute("contact", contactRepository.findOne(id));
        model.addAttribute("home", phoneNumberRepository.findFirstByContactIdAndType(id, "home").get(0).getNumber());
        model.addAttribute("mobile", phoneNumberRepository.findFirstByContactIdAndType(id, "handphone").get(0).getNumber());
        model.addAttribute("office", phoneNumberRepository.findFirstByContactIdAndType(id, "office").get(0).getNumber());
        model.addAttribute("officeExt", phoneNumberRepository.findFirstByContactIdAndType(id, "office").get(0).getExtension());
        return "contact/edit";
    }
    @RequestMapping(value = "contact/update", method = RequestMethod.POST)
    public String updateContact(@ModelAttribute Contact contact, @RequestParam("birthDate") String birthDate, @RequestParam("mobile") String mobile, @RequestParam("home") String home, @RequestParam("office") String office, @RequestParam("officeExt") String officeExt) {
        phoneNumberRepository.delete(contactRepository.findOne(contact.getId()).getPhoneNumber());
        contactRepository.delete(contact);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition parsePosition = new ParsePosition(0);
        Date parsedBirthdate = dateFormat.parse(birthDate, parsePosition);
        contact.setBirthdate(parsedBirthdate);
        contact.setDateChange(new Date());
        Contact c = contactRepository.save(contact);
        PhoneNumber phoneNumber;
        phoneNumber = new PhoneNumber(c.getId(), "home", home, null, "home");
        phoneNumberRepository.save(phoneNumber);
        phoneNumber = new PhoneNumber(c.getId(), "handphone", mobile, null, "mobile");
        phoneNumberRepository.save(phoneNumber);
        phoneNumber = new PhoneNumber(c.getId(), "office", office, officeExt, "building");
        phoneNumberRepository.save(phoneNumber);
        return "redirect:/contact";
    }
    @RequestMapping(value = "contact/delete/{id}")
    public String deleteContact(@PathVariable(value="id") String id) {
        phoneNumberRepository.delete(contactRepository.findOne(id).getPhoneNumber());
        contactRepository.delete(id);
        return "redirect:/contact";
    }
    @RequestMapping(value = "/save-excel-contact", method = RequestMethod.POST)
    public String saveExcelContact(@RequestParam("file") MultipartFile file) throws Exception{
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(directoryPath + file.getOriginalFilename());
            System.out.println(file.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("masuk kesini");
        // clean table
        phoneNumberRepository.deleteAll();
        contactRepository.deleteAll();
        // insert data
        InputStream in = new FileSystemResource(directoryPath + file.getOriginalFilename()).getInputStream();
        String sheetName = "Employee List";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Employee> entityList = parser.createEntity(sheet, sheetName, Employee.class);
        PhoneNumber phoneNumber;
        for(Employee i: entityList) {
            if(i.getEmployeeName() != null){
                Contact contact = new Contact(i.getStsrc(), i.getDateChange(), i.getEmployeeId(), i.getNIP(), i.getEmployeeName(), i.getBranchID(), i.getDivisionID(), i.getRegionID(), i.getJobTitleID(), i.getCEK(), i.getBirthDate());
                contact = contactRepository.save(contact);
                phoneNumber = new PhoneNumber(contact.getId(), "home", i.getHomeTelp(), null, "home");
                phoneNumberRepository.save(phoneNumber);
                phoneNumber = new PhoneNumber(contact.getId(), "handphone", i.getHandphoneTelp(), null, "mobile");
                phoneNumberRepository.save(phoneNumber);
                phoneNumber = new PhoneNumber(contact.getId(), "office", i.getOfficeTelp(), i.getOfficeExt(), "building");
                phoneNumberRepository.save(phoneNumber);
            }
        }

        return "redirect:/contact";
    }
}
