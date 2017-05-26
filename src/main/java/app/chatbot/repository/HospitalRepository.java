package app.chatbot.repository;

import app.chatbot.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 5/26/17.
 */
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {

}
