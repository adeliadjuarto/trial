package app.chatbot.repository;

import app.chatbot.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by adeliadjuarto on 6/19/17.
 */
public interface ContactRepository extends JpaRepository<Contact, String> {
    public List<Contact> findByNameContaining(String employeeName);
    public List<Contact> findAllByOrderByNameAsc();
    public List<Contact> findByJobtitleIdContaining(String jobTitleID);
}
