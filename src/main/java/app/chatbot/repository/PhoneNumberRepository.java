package app.chatbot.repository;

import app.chatbot.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * Created by adeliadjuarto on 6/19/17.
 */
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, String> {
    public List<PhoneNumber> findFirstByContactIdAndType(String contact_id, String type);
}
