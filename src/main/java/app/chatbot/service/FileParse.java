package app.chatbot.service;

import app.chatbot.repository.CategoryRepository;
import app.chatbot.repository.ContentRepository;
import app.chatbot.model.*;
import app.chatbot.commons.Hasher;
import app.chatbot.commons.Lemmatizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by willemchua on 5/4/17.
 */
@Controller
public class FileParse {

    Hasher hasher;
    public List<String> results;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ContentRepository contentRepository;

    public List<String> parse(String path) throws Exception{
        hasher = new Hasher();

        results = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(path));

        String currString = reader.readLine();

        Integer categoryIndex = 0;
        Integer subcategoryIndex = 0;

        while(reader.ready())
        {
            if(currString.startsWith("SYARAT DAN KETENTUAN")) {
                String categoryName = currString.substring(20, currString.indexOf("PT B")).trim();

                Category category = categoryRepository.save(new Category(categoryName, 0));
                categoryIndex = category.getId();

                currString = reader.readLine();
                continue;
            }

            if(Pattern.matches("[0-9]+\\.\\s[a-zA-Z\\s\\)\\(\\-\\,\\&\"]+", currString)) {
                String subcategoryName = currString.substring(currString.indexOf(' ')).trim();
                Category subCategory = categoryRepository.save(new Category(subcategoryName, categoryIndex));
                subcategoryIndex = subCategory.getId();

                currString = reader.readLine();
                continue;
            }

            currString = currString.trim();

            if(!currString.isEmpty()) {
                String stemmedString = stemDocs(currString);
                contentRepository.save(new Content(currString, stemmedString, subcategoryIndex));
            }
            currString = reader.readLine();
        }

        return results;
    }

    public static String stemDocs(String document) throws Exception{
        Lemmatizer lemma = new Lemmatizer();

        String stemmedWords = "";
        for(String x: document.split("[ ]|[\\-]")) {
            String lemmatizedWord = lemma.lemmatize(x.replace('-', ' ').replaceAll("[^A-Za-z0-9]+", ""));
            stemmedWords = stemmedWords.concat(lemmatizedWord);
            if(!lemmatizedWord.isEmpty())
                stemmedWords = stemmedWords.concat(" ");
        }

        return stemmedWords;
    }

}
