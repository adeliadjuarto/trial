package app.chatbot.controller;

import app.chatbot.model.Contact;
import app.chatbot.model.Hospital;
import app.chatbot.repository.*;
import app.chatbot.model.Message;
import app.chatbot.service.ChatService;
import app.chatbot.service.FileParse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    private ContactRepository contactRepository;
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

    @RequestMapping("/get-all-contacts")
    public @ResponseBody  Iterable<Contact> getContact() throws Exception{
        return contactRepository.findAllByOrderByNameAsc();
    }

    @RequestMapping("/find-contact")
    public @ResponseBody  Contact findEmployee(@RequestParam("id") String id) throws Exception{
        return contactRepository.findOne(id);
    }

    @RequestMapping("/search-contacts")
    public @ResponseBody  Iterable<Contact> searchEmployeeByName(@RequestParam("name") String name) throws Exception{
        return contactRepository.findByNameContaining(name);
    }


    @RequestMapping("/find-hospital")
    public @ResponseBody  Hospital findHospital(@RequestParam("id") Integer id) throws Exception{
        return hospitalRepository.findOne(id);
    }

    @RequestMapping("/search-hospitals")
    public @ResponseBody  Iterable<Hospital> searchHospitals(@RequestParam("city") String city, @RequestParam("provider") String provider, @RequestParam("bpjs") String bpjs) throws Exception{
        return hospitalRepository.findByCityAndProviderAndBpjs(city, provider, bpjs);
    }

}
