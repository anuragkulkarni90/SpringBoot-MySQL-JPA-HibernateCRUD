/*
package demo.endpoint;

import demo.entity.Department;
import demo.entity.Employee;
import demo.entity.SuccessResponse;
import demo.repositories.EmployeeRepository;
import demo.service.impl.EmployeeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {
    @MockBean
    EmployeeRepository employeeRepository;

    @MockBean
    EmployeeServiceImpl employeeService;

    @Autowired
    EmployeeController employeeController;

    @Test
    public void getEmployeeById() {
        Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
        Optional<Employee> optionalEmployee = Optional.of(empOne);

        when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);

        //test
        //  ResponseEntity<SuccessResponse> response = employeeController.getEmployee(empOne.getId());
    }
}
*/
