package app.chatbot.repository;

import app.chatbot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
/**
 * Created by willemchua on 6/12/17.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    public List<Employee> findAllByOrderByEmployeeNameAsc();
    public List<Employee> findByEmployeeNameContaining(String employeeName);
    public Employee findByRowID(Integer rowID);
    public List<Employee> findByJobTitleIDContaining(String jobTitleID);
}
