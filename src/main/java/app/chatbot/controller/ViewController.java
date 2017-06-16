package app.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by adeliadjuarto on 6/16/17.
 */
@Controller
public class ViewController {
    @RequestMapping("/")
    public String dashboard() throws Exception{
        return "dashboard";
    }
    @RequestMapping("/import-contact")
    public String importContact() throws Exception{
        return "importContact";
    }
    @RequestMapping("/import-hospital")
    public String importHospital() throws Exception{
        return "importHospital";
    }
}
