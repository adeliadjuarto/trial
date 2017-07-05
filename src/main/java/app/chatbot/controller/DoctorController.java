package app.chatbot.controller;

import app.chatbot.model.Doctor;
import app.chatbot.repository.DoctorRepository;
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
 * Created by adeliadjuarto on 7/5/17.
 */
@Controller
public class DoctorController {
    @Value("${excel-directory-path}")
    private String directoryPath;
    @Autowired
    private DoctorRepository doctorRepository;

    @RequestMapping("/doctor")
    public String doctor(Model model) throws Exception{
        model.addAttribute("doctors", doctorRepository.findAll());
        return "doctor/index";
    }
    @RequestMapping("doctor/import-doctor")
    public String importDoctor() throws Exception{
        return "doctor/import";
    }
    @RequestMapping("doctor/create")
    public String createDoctor(Model model) throws Exception{
        model.addAttribute("doctor", new Doctor());
        return "doctor/create";
    }
    @RequestMapping(value = "doctor/add", method = RequestMethod.POST)
    public String addDoctor(@ModelAttribute Doctor doctor) {
        doctorRepository.save(doctor);
        return "redirect:/doctor";
    }
    @RequestMapping("doctor/edit/{id}")
    public String editDoctor(Model model, @PathVariable(value="id") int id) throws Exception{
        model.addAttribute("doctor", doctorRepository.findOne(id));
        return "doctor/edit";
    }
    @RequestMapping(value = "doctor/update", method = RequestMethod.POST)
    public String updateDoctor(@ModelAttribute Doctor doctor) {
        doctorRepository.save(doctor);
        return "redirect:/doctor";
    }
    @RequestMapping(value = "doctor/delete/{id}")
    public String deleteDoctor(@PathVariable(value="id") int id) {
        doctorRepository.delete(id);
        return "redirect:/doctor";
    }
    @RequestMapping(value = "/save-excel-doctor", method = RequestMethod.POST)
    public String saveExcelDoctor(@RequestParam("file") MultipartFile file) throws Exception{
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
        doctorRepository.deleteAll();

        // insert data
        InputStream in = new FileSystemResource(directoryPath + file.getOriginalFilename()).getInputStream();
        String sheetName = "RAWAT JALAN";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> entityList = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: entityList) {
            if(i.getName() != null){
                doctorRepository.save(i);
            }
        }

        return "redirect:/doctor";
    }
}
