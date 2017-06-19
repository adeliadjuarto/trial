package app.chatbot.repository;

import app.chatbot.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 6/19/17.
 */
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, String> {
}
