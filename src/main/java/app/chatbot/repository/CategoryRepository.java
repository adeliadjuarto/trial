package app.chatbot.repository;

import app.chatbot.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/**
 * Created by willemchua on 5/22/17.
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findFirstByName(String name);
    List<Category> findByParentId(Integer parentId);
    Category findFirstByNameAndParentId(String name, Integer parentId);

    boolean existsByName(String name);

}
