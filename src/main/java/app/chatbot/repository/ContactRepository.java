package app.chatbot.repository;

import app.chatbot.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 6/19/17.
 */
public interface ContactRepository extends JpaRepository<Contact, String> {

}
