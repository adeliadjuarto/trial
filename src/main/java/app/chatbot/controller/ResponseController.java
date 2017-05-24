package app.chatbot.controller;

import app.chatbot.repository.CategoryRepository;
import app.chatbot.repository.ContentRepository;
import app.chatbot.model.Message;
import app.chatbot.service.ChatService;
import app.chatbot.service.FileParse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private FileParse fileParse;
    @Autowired
    private ChatService chatService;

    @RequestMapping(value = "/chat",
                    method = RequestMethod.POST)
    public Message response(
            @RequestBody JsonNode json
            ) throws Exception{
        String body = json.get("body").asText();
        String from = json.get("from").asText();

        chatService.chat(body, from);

        String title = chatService.searchResultTitle;
        String content = chatService.searchResultContent;

        return new Message(title, content);
    }

    @RequestMapping("/start")
    public Message response() throws Exception{

        System.out.println(categoryRepository.toString());
        fileParse.parse("./skbca.txt");

        return new Message("Hi", "Hai juga");
    }

}
