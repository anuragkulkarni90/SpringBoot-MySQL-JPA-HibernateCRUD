package demo.service;

import demo.entity.Employee;
import demo.entity.PagedResponse;
import demo.specification.SearchCriteria;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
	Boolean addEmployee(Employee employee);

	Employee updateEmployee(long id, Employee employee);

	List<Employee> updateEmployees(List<Employee> employee);

	Boolean deleteEmployee(long id);

	Map<String, List<Employee>> addEmployees(List<Employee> employees);

	List<Employee> getEmployee(Long id, String firstName, String lastName, Long salary, String departmentName,
                               String location);

	Map<String, List<Long>> deleteEmployees(List<Long> ids);

	PagedResponse<List<Employee>> getBatchEmployees(Integer pageNo, Integer pageSize, String sortBy,
                                                    List<SearchCriteria> params);

	Employee getEmployeeById(Long id);
}
