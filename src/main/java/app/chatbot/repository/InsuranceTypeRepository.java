package app.chatbot.repository;

import app.chatbot.model.InsuranceType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by adeliadjuarto on 7/5/17.
 */
public interface InsuranceTypeRepository extends JpaRepository<InsuranceType, Integer> {
}
