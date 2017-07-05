package app.chatbot.controller;

import app.chatbot.model.InsuranceType;
import app.chatbot.repository.InsuranceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
@Controller
public class InsuranceTypeController {
    @Value("${excel-directory-path}")
    private String directoryPath;
    @Autowired
    private InsuranceTypeRepository insuranceTypeRepository;

    @RequestMapping("/insurance-type")
    public String InsuranceType(Model model) throws Exception{
        model.addAttribute("insuranceTypes", insuranceTypeRepository.findAll());
        return "insurance-type/index";
    }
    @RequestMapping("insurance-type/create")
    public String createInsuranceType(Model model) throws Exception{
        model.addAttribute("insuranceType", new InsuranceType());
        return "insurance-type/create";
    }
    @RequestMapping(value = "insurance-type/add", method = RequestMethod.POST)
    public String addInsuranceType(@ModelAttribute InsuranceType insuranceType) {
        insuranceTypeRepository.save(insuranceType);
        return "redirect:/insurance-type";
    }
    @RequestMapping("insurance-type/edit/{id}")
    public String editInsuranceType(Model model, @PathVariable(value="id") int id) throws Exception{
        model.addAttribute("insuranceType", insuranceTypeRepository.findOne(id));
        return "insurance-type/edit";
    }
    @RequestMapping(value = "insurance-type/update", method = RequestMethod.POST)
    public String updateInsuranceType(@ModelAttribute InsuranceType insuranceType) {
        insuranceTypeRepository.save(insuranceType);
        return "redirect:/insurance-type";
    }
    @RequestMapping(value = "insurance-type/delete/{id}")
    public String deleteInsuranceType(@PathVariable(value="id") int id) {
        insuranceTypeRepository.delete(id);
        return "redirect:/insurance-type";
    }
}
