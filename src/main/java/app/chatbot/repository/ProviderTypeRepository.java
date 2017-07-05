package app.chatbot.repository;

import app.chatbot.model.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
public interface ProviderTypeRepository extends JpaRepository<ProviderType, Integer> {
    public ProviderType findFirstByName(String name);
}
