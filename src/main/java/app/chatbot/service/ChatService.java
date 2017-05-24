package app.chatbot.service;

import app.chatbot.repository.CategoryRepository;
import app.chatbot.repository.ContentRepository;
import app.chatbot.model.*;
import app.chatbot.service.querySearch.QuerySearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
/**
 * Created by willemchua on 5/22/17.
 */
@Service
public class ChatService {

    public String searchResultTitle;
    public String searchResultContent;
    private Integer resultIndex;
    private Integer resultIndexFromSubcategory;

    public ArrayList<Message> messages;

    @Autowired
        private CategoryRepository categoryRepository;
    @Autowired
        private ContentRepository contentRepository;
    @Autowired
        private QuerySearch querySearch;

    Map <String, Integer> context = new HashMap<>();

    Integer categoryIndex = -1;
    Integer subcategoryIndex = -1;

    String NOT_FOUND = "Not Found";
    String CONTENT_NOT_FOUND = "Sorry, but we still can't understand what you're looking for. Let's try this again. Say \"Hi\".";

    public void chat(String query, String from) throws Exception {
        messages = new ArrayList<>();

        if(!context.containsKey(from)){
            context.put(from, 0);
        }

        if(context.get(from) == 0)
            levelZero(query, from);
        else if(context.get(from) == 1)
            levelOne(query, from);
        else if(context.get(from) == 2)
            levelTwo(query, from);
        else if(context.get(from) == 3)
            levelThree(query, from);
        else if(context.get(from) == 4)
            levelFour(query, from);
    }

    public void levelZero(String query, String from) throws Exception {
        if(query.equals("Hi") || query.equals("Halo")) {
            context.put(from, 1);
            searchResultTitle = "Intro Message";
            String temp = "";
            for(Category i: categoryRepository.findByParentId(0)) {
                temp = temp.concat(i.getName());
                temp = temp.concat("\n");
            }
            searchResultContent = temp;
        }
        else if(query.equals("Search")) {
            context.put(from, 3);
        }
        else
            resetChat(from);

        messages.add(new Message(searchResultTitle, searchResultContent));
    }

    public void levelOne(String query, String from) throws Exception {
        if(isCategory(query)) {
            context.put(from, 2);
            categoryIndex = getCategoryIndex(query);
            searchResultTitle = "Subcategory in " + query;
            String temp = "";
            for(Category i: categoryRepository.findByParentId(categoryIndex)) {
                temp = temp.concat(i.getName());
                temp = temp.concat("\n");
            }
            searchResultContent = temp;
        }
        else
            resetChat(from);

        messages.add(new Message(searchResultTitle, searchResultContent));
    }


    public void levelTwo(String query, String from) throws Exception {
        if(isSubcategory(query, categoryIndex)) {
            context.put(from, 3);
            searchResultTitle = "Query Search";
            searchResultContent = "Enter your query in " + query + " category";
            subcategoryIndex = getSubcategoryIndex(query, categoryIndex);
        }
        else
            resetChat(from);

        messages.add(new Message(searchResultTitle, searchResultContent));
    }

    public void levelThree(String query, String from) throws Exception {
        resultIndex = querySearch.searchAll(query);
        resultIndexFromSubcategory = querySearch.searchSpecific(query, subcategoryIndex);

        if(resultIndex != -1) {
            searchResultTitle = "Search Result";
            Integer resultSubcategoryIndex = getSubcategoryByContentIndex(resultIndex);

            if(resultSubcategoryIndex == subcategoryIndex)
                searchResultContent = contentRepository.findOne(resultIndex).getOriginal();
            else {
                searchResultTitle = "Search Result From its Subcategory";
                searchResultContent = contentRepository.findOne(resultIndexFromSubcategory).getOriginal();
                messages.add(new Message(searchResultTitle, searchResultContent));

                searchResultTitle = "Search Result From All Document";
                searchResultContent = "We don't find the best match of what you're looking for at " + getSubcategoryValue(subcategoryIndex);
                searchResultContent += " However, we found a document on " + getSubcategoryValue(resultSubcategoryIndex);
                searchResultContent += " on the category " + getCategoryValue(categoryRepository.findOne(resultSubcategoryIndex).getParentId());
                searchResultContent += " that might be of interest.\n";
                searchResultContent += contentRepository.findOne(resultIndex).getOriginal();
            }

            context.put(from, 4);
        }
        else
            resetChat(from);

        messages.add(new Message(searchResultTitle, searchResultContent));
        messages.add(new Message("Question", "Do you want to ask another question? [Yes/No]"));
    }

    public void levelFour(String query, String from) throws Exception {
        if(query.equals("Yes")) {
            context.put(from, 3);
            searchResultTitle = "Query Search";
            searchResultContent = "Enter your query in " + getSubcategoryValue(subcategoryIndex) + " category";
        }else if(query.equals("No")) {
            reChat(from);
            searchResultTitle = "Reply";
            searchResultContent = "Thank you for using";
        }else
            resetChat(from);

        messages.add(new Message(searchResultTitle, searchResultContent));
    }

    private void reChat(String from) {
        context.put(from, 0);
    }

    private boolean isCategory(String query) throws Exception {
        if(categoryRepository.findFirstByNameAndParentId(query, 0) == null)
            return false;

        return true;
    }

    private boolean isSubcategory(String query, Integer categoryIndex) throws Exception {
        if(categoryRepository.findFirstByNameAndParentId(query, categoryIndex) == null)
            return false;

        return true;
    }

    private Integer getCategoryIndex(String query) {
        return categoryRepository.findFirstByNameAndParentId(query, 0).getId();
    }

    private Integer getSubcategoryIndex(String query, Integer categoryIndex) {
        Category category = categoryRepository.findFirstByNameAndParentId(query, categoryIndex);
        if(category != null){
            return category.getId();
        }
        return -1;
    }

    private String getCategoryValue(Integer index) {
        return categoryRepository.findOne(index).getName();
    }

    private String getSubcategoryValue(Integer index) {
        return categoryRepository.findOne(index).getName();
    }

    public Integer getSubcategoryByContentIndex(Integer contentIndex) throws Exception {
        return contentRepository.findOne(contentIndex).getSubcategoryId();
    }

    public void resetChat(String from) throws Exception {
        context.put(from, 0);
        searchResultTitle = NOT_FOUND;
        searchResultContent = CONTENT_NOT_FOUND;
    }

}