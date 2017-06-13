package app.chatbot.repository;

import app.chatbot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by willemchua on 6/12/17.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
