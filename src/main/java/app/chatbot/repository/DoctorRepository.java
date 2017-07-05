package app.chatbot.repository;

import app.chatbot.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
}
