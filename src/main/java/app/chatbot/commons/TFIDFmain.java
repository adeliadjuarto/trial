package app.chatbot.commons;

import app.chatbot.service.TFIDF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willemchua on 5/4/17.
 */
public class TFIDFmain {

    static final String DOC_PATH = "./skbca.txt";
    static String query;

    public static void main(String[] args) throws Exception{

        final double start = System.nanoTime();

        query = "M-BCA";

//        new QuerySearch().search(query);

//        new FileParse().parse(DOC_PATH);

        BufferedReader reader = new BufferedReader(new FileReader(DOC_PATH));

        List<String> documents = new ArrayList<>();

        while(reader.ready()) {
            documents.add(reader.readLine());
        }

        TFIDF tfidf = new TFIDF();

        tfidf.createDocs(documents);
        tfidf.calculateTfIdf();

        final double duration = (System.nanoTime() - start)/1000000000;

        System.out.print('\n');
        System.out.println(duration);


    }
}
