package app.chatbot.model;

import javax.persistence.*;

/**
 * Created by willemchua on 5/22/17.
 */
@Entity
@Table(name="content")
public class Content {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String original;
    private String stemmed;
    private Integer subcategory_id;

    protected Content() {}

    public Content(String original, String stemmed, Integer subcategory_id) {
        this.original = original;
        this.stemmed = stemmed;
        this.subcategory_id = subcategory_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getStemmed() {
        return stemmed;
    }

    public void setStemmed(String stemmed) {
        this.stemmed = stemmed;
    }

    public Integer getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(Integer subcategory_id) {
        this.subcategory_id = subcategory_id;
    }
}
