package app.chatbot.controller;

import app.chatbot.model.Doctor;
import app.chatbot.model.Hospital;
import app.chatbot.model.Provider;
import app.chatbot.model.ProviderType;
import app.chatbot.repository.InsuranceTypeRepository;
import app.chatbot.repository.ProviderRepository;
import app.chatbot.repository.ProviderTypeRepository;
import app.chatbot.repository.ServiceTypeRepository;
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
public class ProviderController {
    @Value("${excel-directory-path}")
    private String directoryPath;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ProviderTypeRepository providerTypeRepository;
    @Autowired
    private ServiceTypeRepository serviceTypeRepository;
    @Autowired
    private InsuranceTypeRepository insuranceTypeRepository;

    @RequestMapping("/provider")
    public String provider(Model model) throws Exception{
        model.addAttribute("providers", providerRepository.findAll());
        return "provider/index";
    }
    @RequestMapping("provider/import-provider")
    public String importHospital() throws Exception{
        return "provider/import";
    }
    @RequestMapping(value = "/save-excel-provider", method = RequestMethod.POST)
    public String saveExcelDoctor(@RequestParam("file-car") MultipartFile fileCar, @RequestParam("file-tmc") MultipartFile fileTmc, @RequestParam("file-chubb") MultipartFile fileChubb) throws Exception{
        // clean table
        providerRepository.deleteAll();
        try {
            // Get the file and save it somewhere
            byte[] bytes = fileCar.getBytes();
            Path path = Paths.get(directoryPath + fileCar.getOriginalFilename());
            Files.write(path, bytes);

            bytes = fileTmc.getBytes();
            path = Paths.get(directoryPath + fileTmc.getOriginalFilename());
            Files.write(path, bytes);

            bytes = fileChubb.getBytes();
            path = Paths.get(directoryPath + fileChubb.getOriginalFilename());
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // insert data CAR
        InputStream in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        String sheetName = "RAWAT INAP";
        SheetParser parser = new SheetParser();

        Sheet sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Hospital> carRawatInapLists = parser.createEntity(sheet, sheetName, Hospital.class);

        for(Hospital i: carRawatInapLists) {
            if(i.getProvider() != null){
                Provider provider = new Provider(i.getCity(), i.getProvider(), i.getBpjs(), i.getAddress(), i.getTelephone(), i.getFax(), providerTypeRepository.findOne(1), serviceTypeRepository.findOne(1), insuranceTypeRepository.findOne(1));
                providerRepository.save(provider);
            }
        }

        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "RAWAT JALAN";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> carRawatJalanLists = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: carRawatJalanLists) {
            if(i.getName() != null){
                ProviderType providerType = providerTypeRepository.findFirstByName(i.getType());
                if(providerType == null){
                    providerType = providerTypeRepository.save(new ProviderType(i.getType()));
                }
                Provider provider = new Provider(i.getCity(), i.getName(), "", i.getAddress(), i.getTelephone(), "", providerType, serviceTypeRepository.findOne(2), insuranceTypeRepository.findOne(1));
                providerRepository.save(provider);
            }
        }

        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "MCU";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> carMCULists = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: carMCULists) {
            if(i.getName() != null){
                ProviderType providerType = providerTypeRepository.findFirstByName(i.getType());
                if(providerType == null){
                    providerType = providerTypeRepository.save(new ProviderType(i.getType()));
                }
                Provider provider = new Provider(i.getCity(), i.getName(), "", i.getAddress(), i.getTelephone(), "", providerType, serviceTypeRepository.findOne(3), insuranceTypeRepository.findOne(1));
                providerRepository.save(provider);
            }
        }

        // insert data TMC
        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "RAWAT INAP";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Hospital> tmcRawatInapLists = parser.createEntity(sheet, sheetName, Hospital.class);

        for(Hospital i: tmcRawatInapLists) {
            if(i.getProvider() != null){
                Provider provider = new Provider(i.getCity(), i.getProvider(), i.getBpjs(), i.getAddress(), i.getTelephone(), i.getFax(), providerTypeRepository.findOne(1), serviceTypeRepository.findOne(1), insuranceTypeRepository.findOne(2));
                providerRepository.save(provider);
            }
        }

        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "RAWAT JALAN";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> tmcRawatJalanLists = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: tmcRawatJalanLists) {
            if(i.getName() != null){
                ProviderType providerType = providerTypeRepository.findFirstByName(i.getType());
                if(providerType == null){
                    providerType = providerTypeRepository.save(new ProviderType(i.getType()));
                }
                Provider provider = new Provider(i.getCity(), i.getName(), "", i.getAddress(), i.getTelephone(), "", providerType, serviceTypeRepository.findOne(2), insuranceTypeRepository.findOne(2));
                providerRepository.save(provider);
            }
        }

        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "MCU";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> tmcMCULists = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: tmcMCULists) {
            if(i.getName() != null){
                ProviderType providerType = providerTypeRepository.findFirstByName(i.getType());
                if(providerType == null){
                    providerType = providerTypeRepository.save(new ProviderType(i.getType()));
                }
                Provider provider = new Provider(i.getCity(), i.getName(), "", i.getAddress(), i.getTelephone(), "", providerType, serviceTypeRepository.findOne(3), insuranceTypeRepository.findOne(2));
                providerRepository.save(provider);
            }
        }

        // insert data Chubb
        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "RAWAT INAP";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Hospital> chubbRawatInapLists = parser.createEntity(sheet, sheetName, Hospital.class);

        for(Hospital i: chubbRawatInapLists) {
            if(i.getProvider() != null){
                Provider provider = new Provider(i.getCity(), i.getProvider(), i.getBpjs(), i.getAddress(), i.getTelephone(), i.getFax(), providerTypeRepository.findOne(1), serviceTypeRepository.findOne(1), insuranceTypeRepository.findOne(3));
                providerRepository.save(provider);
            }
        }

        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "RAWAT JALAN";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> chubbRawatJalanLists = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: chubbRawatJalanLists) {
            if(i.getName() != null){
                ProviderType providerType = providerTypeRepository.findFirstByName(i.getType());
                if(providerType == null){
                    providerType = providerTypeRepository.save(new ProviderType(i.getType()));
                }
                Provider provider = new Provider(i.getCity(), i.getName(), "", i.getAddress(), i.getTelephone(), "", providerType, serviceTypeRepository.findOne(2), insuranceTypeRepository.findOne(3));
                providerRepository.save(provider);
            }
        }

        in = new FileSystemResource(directoryPath + fileCar.getOriginalFilename()).getInputStream();

        sheetName = "MCU";
        parser = new SheetParser();

        sheet = new XSSFWorkbook(in).getSheet(sheetName);

        List<Doctor> chubbMCULists = parser.createEntity(sheet, sheetName, Doctor.class);

        for(Doctor i: chubbMCULists) {
            if(i.getName() != null){
                ProviderType providerType = providerTypeRepository.findFirstByName(i.getType());
                if(providerType == null){
                    providerType = providerTypeRepository.save(new ProviderType(i.getType()));
                }
                Provider provider = new Provider(i.getCity(), i.getName(), "", i.getAddress(), i.getTelephone(), "", providerType, serviceTypeRepository.findOne(2), insuranceTypeRepository.findOne(3));
                providerRepository.save(provider);
            }
        }

        return "redirect:/doctor";
    }
}
