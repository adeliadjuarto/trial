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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willemchua on 03/05/17.
 */

@Service
public class QuerySearch {

    private TFIDF tfidf;
    private QueryVectorizer vectorizer;
    private Hasher hasher;

    private List<Vector> result = new ArrayList<>();
    private List<String> docIndex = new ArrayList<>();
    private List<String> stemmedDocuments = new ArrayList<>();

    private Path tfidfPath = new Path("output/service");
    private String docPath = "./skbca.txt";

    @Autowired
    private ContentRepository contentRepository;

    public Integer search(String query) throws Exception{
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

        SequenceFileIterable<Writable, VectorWritable> iterable = new SequenceFileIterable<> (
                new Path(tfidfPath + "/tfidf-vectors/part-r-00000"), new Configuration());

        vectorizer.vectorize(query);
        Vector queryVector = vectorizer.getResult();

        return Integer.parseInt(indexSearchResult(iterable, queryVector));
    }

    public String indexSearchResult(SequenceFileIterable<Writable, VectorWritable> iterable, Vector source){
        for (Pair<Writable, VectorWritable> pair : iterable) {
            Vector y = pair.getSecond().get();
            String x = pair.getFirst().toString();

            result.add(y);
            docIndex.add(x);
        }

        double maxScore = 0.0;
        int selectedIndex = -1;

        for(int i = 0; i<result.size(); i++) {
            double docScore;

            docScore = source.dot(result.get(i)) /
                    (Math.sqrt(source.getLengthSquared()) *
                            Math.sqrt(result.get(i).getLengthSquared()));

            if(docScore > maxScore) {
                maxScore = docScore;
                selectedIndex = i;
            }
        }

        if(selectedIndex == -1)
            return "Not Found";

        return docIndex.get(selectedIndex);
    }

    private void loadStemmedDocumentsTo(List<String> documents) throws Exception {
        documents.clear();
        for(Content i: contentRepository.findAll()) {
            documents.add(i.getStemmed());
        }
    }


}
