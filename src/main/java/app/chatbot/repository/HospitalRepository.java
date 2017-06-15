package app.chatbot.repository;

import app.chatbot.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Created by adeliadjuarto on 5/26/17.
 */
public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    public List<Hospital> findByCityAndProviderAndBpjs(String city, String provider, String Bpjs);
}
