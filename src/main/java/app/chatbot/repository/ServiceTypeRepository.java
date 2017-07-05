package app.chatbot.repository;

import app.chatbot.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}
