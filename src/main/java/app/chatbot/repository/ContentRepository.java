package app.chatbot.repository;

import app.chatbot.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by willemchua on 5/22/17.
 */
public interface ContentRepository extends JpaRepository<Content, Integer> {

    Content findFirstByOriginal(String original);
    Content findFirstBySubcategoryId(Integer index);
    Content findFirstBySubcategoryIdOrderByIdDesc(Integer index);

    boolean existsByOriginal(String original);
}

