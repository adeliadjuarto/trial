package app.chatbot.service.querySearch;

import app.chatbot.model.Content;
import app.chatbot.repository.ContentRepository;
import app.chatbot.commons.Hasher;
import app.chatbot.service.TFIDF;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileIterable;
import org.apache.mahout.math.*;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.Vector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by willemchua on 03/05/17.
 */

@Service
public class QuerySearch {

    private TFIDF tfidf;
    private QueryVectorizer vectorizer;
    private Hasher hasher;

    private List<Vector> result;
    private List<Integer> docIndex;
    private List<String> stemmedDocuments = new ArrayList<>();

    private Path tfidfPath = new Path("output/service");
    private String docPath = "./assets/skbca.txt";

    private SequenceFileIterable<Writable, VectorWritable> iterable;

    @Autowired
    private ContentRepository contentRepository;

    public void prepare() throws Exception {
        tfidf = new TFIDF();
        hasher = new Hasher();
        vectorizer = new QueryVectorizer();

        String hashPath = docPath.replace("txt", "hash");
        String hashValue = hasher.getHash(hashPath);
        loadStemmedDocumentsTo(stemmedDocuments);

        if(!hashValue.isEmpty() && hashValue.equals(hasher.hash(docPath))) {
            System.out.print("TF-IDF File Exists" + '\n');
        }
        else {
            tfidf.createDocs(stemmedDocuments);
            tfidf.calculateTfIdf();
            hasher.createHashFile(docPath);
        }

        iterable = new SequenceFileIterable<> (
                new Path(tfidfPath + "/tfidf-vectors/part-r-00000"), new Configuration());

    }

    public ArrayList<Integer> searchAll(String query) throws Exception{

        prepare();

        result = new ArrayList<>();
        docIndex = new ArrayList<>();
        ArrayList<Double> docScores = new ArrayList<>();
        ArrayList<Integer> highestDocIndexes = new ArrayList<>();

        for (Pair<Writable, VectorWritable> pair : iterable) {
            Vector y = pair.getSecond().get();
            Integer index = Integer.parseInt(pair.getFirst().toString());

            result.add(y);
            docIndex.add(index);
        }

        vectorizer.vectorize(query);
        Vector queryVector = vectorizer.getResult();

        int selectedIndex = -1;

        for(int i = 0; i< result.size(); i++) {
            double score;

            score = queryVector.dot(result.get(i)) /
                    (Math.sqrt(queryVector.getLengthSquared()) *
                            Math.sqrt(result.get(i).getLengthSquared()));
            docScores.add(score);
        }

        //tambah sorting disini


        for(int i = 0; i < 4; i++){
            highestDocIndexes.add(docIndex.get(i));
        }

        if(selectedIndex == -1)
            return null;

        return highestDocIndexes;
    }

    public Integer searchSpecific(String query, Integer subcategoryIndex) throws Exception{
        prepare();

        vectorizer.vectorize(query);
        Vector queryVector = vectorizer.getResult();

        result = new ArrayList<>();
        docIndex = new ArrayList<>();

        List<Integer> contentIndexes = new ArrayList<>();

        for(Content c: contentRepository.findAllBySubcategoryID(subcategoryIndex)) {
            contentIndexes.add(c.getId());
        }

        for (Pair<Writable, VectorWritable> pair : iterable) {
            Vector y = pair.getSecond().get();
            Integer index = Integer.parseInt(pair.getFirst().toString());

            if(contentIndexes.contains(index)) {
                result.add(y);
                docIndex.add(index);
            }
        }

        double maxScore = 0.0;
        int selectedIndex = -1;

        for(int i = 0; i < result.size(); i++) {
            double docScore;

            docScore = queryVector.dot(result.get(i)) /
                    (Math.sqrt(queryVector.getLengthSquared()) *
                            Math.sqrt(result.get(i).getLengthSquared()));

            if(docScore > maxScore) {
                maxScore = docScore;
                selectedIndex = i;
            }
        }

        return docIndex.get(selectedIndex);
    }

    private void loadStemmedDocumentsTo(List<String> documents) throws Exception {
        documents.clear();
        System.out.println(contentRepository == null);
        for(Content i: contentRepository.findAll()) {
            documents.add(i.getStemmed());
        }
    }

}
