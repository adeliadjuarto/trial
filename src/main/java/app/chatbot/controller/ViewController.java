package app.chatbot.controller;

import app.chatbot.repository.EmployeeRepository;
import app.chatbot.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by adeliadjuarto on 6/16/17.
 */
@Controller
public class ViewController {
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
    @RequestMapping("/contact")
    public String contact(Model model) throws Exception{
        model.addAttribute("employees", employeeRepository.findAll());
        return "contact";
    }
    @RequestMapping("contact/import-contact")
    public String importContact() throws Exception{
        return "importContact";
    }
    @RequestMapping("hospital/import-hospital")
    public String importHospital() throws Exception{
        return "importHospital";
    }
}
