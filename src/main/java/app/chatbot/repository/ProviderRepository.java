package app.chatbot.repository;

import app.chatbot.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
}
