package app.chatbot.commons;

import java.io.*;
import java.util.*;

import jsastrawi.morphology.DefaultLemmatizer;

/**
 * Created by willemchua on 5/5/17.
 */
public class Lemmatizer {

    private static DefaultLemmatizer lemmatizer;

    List<String> stopWords = new ArrayList<>();

    public Lemmatizer() throws Exception{
        Set<String> dictionary = new HashSet<>();
        InputStream in = this.getClass().getResourceAsStream("/assets/root-words.txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        lemmatizer = new DefaultLemmatizer(dictionary);

        br = new BufferedReader(new FileReader("./assets/stopwords.txt"));

        while(br.ready()) {
            stopWords.add(br.readLine());
        }
    }

    public String lemmatize(String str) {
        if(stopWords.contains(str))
            return "";
        return lemmatizer.lemmatize(str);
    }
}
