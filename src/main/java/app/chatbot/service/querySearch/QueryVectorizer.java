package app.chatbot.service.querySearch;

import app.chatbot.commons.Lemmatizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;

import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by willemchua on 5/8/17.
 */
public class QueryVectorizer {

    private ArrayList<Pair<String, Integer>> query = new ArrayList<>();
    private ArrayList<String> dictionary = new ArrayList<>();
    private ArrayList<Integer> docFreq = new ArrayList<>();
    private Lemmatizer lemmatizer;
    private Vector result;

    public Vector getResult() throws Exception {
        return result;
    }

    public void vectorize(String queries) throws Exception {
        lemmatizer = new Lemmatizer();
        loadFiles();
        splitQuery(queries);

        //Initialize Arrays for Processing
        double termFreq[] = new double[dictionary.size()];
        double IDF[] = new double[dictionary.size()];
        double tfidf[] = new double[dictionary.size()];

        Arrays.fill(termFreq, 0);
        Arrays.fill(IDF, 0);
        Arrays.fill(tfidf, 0);

        // Calculate Term Frequency
        for(Pair<String, Integer> i: query) {
            if(i.getSecond() != -1)
                termFreq[i.getSecond()] += 1.0;
        }

        // Calculate Document Frequency
        for(Pair<String, Integer> i: query) {
            if(i.getSecond() != -1) {
                IDF[i.getSecond()] = Math.log(((double) docFreq.get(0)/(double) (docFreq.get(i.getSecond()+1) + 1)) * Math.E);
            }
        }

        // Calculate TF-IDF
        for(int i = 0; i<tfidf.length; i++) {
            tfidf[i] = Math.sqrt(termFreq[i]) * IDF[i];
        }

        result = new RandomAccessSparseVector(dictionary.size());
        result.assign(tfidf);

        System.out.println(result.toString());
    }

    private void loadFiles() throws Exception {

        //Open Dictionary
        SequenceFileIterable<Writable, Writable> dictIterable = new SequenceFileIterable<>(
                new Path("output/dictionary.file-0"), new Configuration());

        for(Pair<Writable, Writable> iteration: dictIterable) {
            dictionary.add(iteration.getFirst().toString());
        }

        //Open Document Frequency
        SequenceFileIterable<Writable, Writable> docFreqIterable = new SequenceFileIterable<>(
                new Path("output/service/df-count/part-r-00000"), new Configuration());

        for(Pair<Writable, Writable> iteration: docFreqIterable) {
            docFreq.add(Integer.parseInt(iteration.getSecond().toString()));
        }

    }

    public void splitQuery(String queries) throws Exception {
        for(String i: queries.split(" ")) {
            System.out.println(lemmatizer.lemmatize(i));
            query.add(new Pair<>(lemmatizer.lemmatize(i), dictionary.indexOf(lemmatizer.lemmatize(i))));
        }
        System.out.println(query.toString());
    }


}
