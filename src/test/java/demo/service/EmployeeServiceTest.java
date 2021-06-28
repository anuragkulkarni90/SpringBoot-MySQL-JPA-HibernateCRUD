package demo.service;

import demo.entity.Department;
import demo.entity.Employee;
import demo.entity.PagedResponse;
import demo.repositories.DepartmentRepository;
import demo.repositories.EmployeeRepository;
import demo.service.impl.EmployeeServiceImpl;
import demo.specification.EmployeeSpecification;
import demo.specification.SearchCriteria;
import demo.specification.SearchOperation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static demo.specification.SearchOperation.EQUAL;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.EMPTY_LIST;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.codehaus.groovy.runtime.InvokerHelper.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {

	@MockBean
	EmployeeRepository employeeRepository;

	@MockBean
	DepartmentRepository departmentRepository;

	@Autowired
	EmployeeServiceImpl employeeService;

	@Test
	public void getEmployeesSuccessTest() {
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department(1L, "Engineering", "Pune"));
		EmployeeSpecification employeeSpecification = new EmployeeSpecification();
		employeeSpecification.add(new SearchCriteria("id", "1", EQUAL));
		employeeSpecification.add(new SearchCriteria("firstName", "John", EQUAL));
		employeeSpecification.add(new SearchCriteria("lastName", "Wick", EQUAL));
		employeeSpecification.add(new SearchCriteria("salary", 10000, EQUAL));

		when(departmentRepository.findByDepartmentNameAndLocation(empOne.getDepartment().getDepartmentName(),
				empOne.getDepartment().getLocation())).thenReturn(Optional.of(empOne.getDepartment()));
		when(employeeRepository.findAll(employeeSpecification)).thenReturn(asList(empOne));

		List<Employee> employees = employeeService.getEmployee(empOne.getId(), empOne.getFirstName(),
				empOne.getLastName(), empOne.getSalary(), empOne.getDepartment().getDepartmentName(),
				empOne.getDepartment().getLocation());

		assertNotNull(employees);
	}

	@Test
	public void getEmployeesWhenOnlyDepartmentNamePassedSuccessTest() {
		Department department = new Department(1L, "Engineering", "Pune");

		Employee empOne = new Employee(1L, "John", "Wick", 10000L, department);
		EmployeeSpecification employeeSpecification = new EmployeeSpecification();
		employeeSpecification.add(new SearchCriteria("id", "1", EQUAL));
		employeeSpecification.add(new SearchCriteria("firstName", "John", EQUAL));
		employeeSpecification.add(new SearchCriteria("lastName", "Wick", EQUAL));
		employeeSpecification.add(new SearchCriteria("salary", 10000, EQUAL));

		when(departmentRepository.findByDepartmentName(empOne.getDepartment().getDepartmentName())).thenReturn(asList(department));
		when(employeeRepository.findAll(employeeSpecification)).thenReturn(asList(empOne));

		List<Employee> employees = employeeService.getEmployee(empOne.getId(), empOne.getFirstName(),
				empOne.getLastName(), empOne.getSalary(), empOne.getDepartment().getDepartmentName(), "");

		assertNotNull(employees);
	}

	@Test
	public void getEmployeesWhenOnlyLocationPassedSuccessTest() {
		Department department = new Department(1L, "Engineering", "Pune");

		Employee empOne = new Employee(1L, "John", "Wick", 10000L, department);
		EmployeeSpecification employeeSpecification = new EmployeeSpecification();
		employeeSpecification.add(new SearchCriteria("id", "1", EQUAL));
		employeeSpecification.add(new SearchCriteria("firstName", "John", EQUAL));
		employeeSpecification.add(new SearchCriteria("lastName", "Wick", EQUAL));
		employeeSpecification.add(new SearchCriteria("salary", 10000, EQUAL));

		when(departmentRepository.findByDepartmentName(empOne.getDepartment().getDepartmentName())).thenReturn(asList(department));
		when(employeeRepository.findAll(employeeSpecification)).thenReturn(asList(empOne));

		List<Employee> employees = employeeService.getEmployee(empOne.getId(), empOne.getFirstName(),
				empOne.getLastName(), empOne.getSalary(), "", empOne.getDepartment().getLocation());

		assertNotNull(employees);
	}

	@Test
	public void getAllEmployeesSuccessTest() {
		Pageable paging = PageRequest.of(0, 10, Sort.by("id"));

		List<Employee> list = new ArrayList<>();
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
		Employee empTwo = new Employee(2L, "Alex", "Hales", 20000L, new Department());
		Employee empThree = new Employee(3L, "Steve", "Bucky", 30000L, new Department());
		list.add(empOne);
		list.add(empTwo);
		list.add(empThree);

		PagedResponse<List<Employee>> pagedResponse = new PagedResponse<>(0, 10);
		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		pagedResponse.setResult(list);
		Page<Employee> page = new PageImpl<>(list);
		when(employeeRepository.findAll(paging)).thenReturn(page);

		//test
		PagedResponse<List<Employee>> empList = employeeService.getBatchEmployees(pageNo, pageSize, sortBy, null);

		assertEquals(3, empList.getResult().size());
	}

	@Test
	public void getAllEmployeesFailureTest() {
		Pageable paging = PageRequest.of(0, 10, Sort.by("id"));

		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		Page<Employee> page = new PageImpl<Employee>(EMPTY_LIST);
		when(employeeRepository.findAll(paging)).thenReturn(page);

		//test
		PagedResponse<List<Employee>> empList = employeeService.getBatchEmployees(pageNo, pageSize, sortBy, null);

		assertEquals(0, empList.getResult().size());
	}

	@Test
	public void getBatchEmployeesSuccessTest() {
		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		List<Employee> list = new ArrayList<>();
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
		list.add(empOne);

		SearchCriteria searchCriteria = new SearchCriteria("id", 1L,
				EQUAL);
		List<SearchCriteria> criteriaList = Collections.singletonList(searchCriteria);

		EmployeeSpecification employeeSpecification = new EmployeeSpecification();
		employeeSpecification.add(searchCriteria);

		Page<Employee> pagedResult = new PageImpl<>(list);
		when(employeeRepository.findAll(employeeSpecification, paging)).thenReturn(pagedResult);

		//test
		PagedResponse<List<Employee>> empList = employeeService.getBatchEmployees(pageNo, pageSize, sortBy,
				criteriaList);

		assertEquals(0, empList.getResult().size());
	}

	@Test
	public void getBatchEmployeesFailureTest() {
		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setKey("firstName");
		searchCriteria.setValue("John");
		searchCriteria.setOperation(SearchOperation.MATCH);
		List<SearchCriteria> criteriaList = Collections.singletonList(searchCriteria);

		EmployeeSpecification employeeSpecification = new EmployeeSpecification();
		employeeSpecification.add(searchCriteria);

		Page<Employee> pagedResult = new PageImpl<Employee>(EMPTY_LIST);
		when(employeeRepository.findAll(employeeSpecification, paging)).thenReturn(pagedResult);

		//test
		PagedResponse<List<Employee>> empList = employeeService.getBatchEmployees(pageNo, pageSize, sortBy,
				criteriaList);

		assertEquals(0, empList.getResult().size());
	}

	@Test
	public void addEmployeesSuccessTest() {
		Employee empOne = new Employee(null, "John", "Wick", 10000L, new Department());

		//test
		Boolean response = employeeService.addEmployee(empOne);
		System.out.println(empOne);
		assertEquals(response, true);
	}

	@Test
	public void addEmployeesFailureTest() {
		Employee empOne = new Employee(null, "John", "Wick", 10000L, new Department());

		when(employeeRepository.findByFirstNameAndLastName(empOne.getFirstName(), empOne.getLastName())).thenReturn(Optional.of(empOne));
		//test
		Boolean response = employeeService.addEmployee(empOne);
		System.out.println(empOne);
		assertEquals(response, false);
	}

	@Test
	public void addBatchEmployeesSuccessTest() {
		Employee empOne = new Employee(null, "John", "Wick", 10000L, new Department());

		//test
		Map<String, List<Employee>> response = employeeService.addEmployees(Arrays.asList(empOne));
		System.out.println(empOne);
		assertEquals(response.containsKey(TRUE.toString()), TRUE);
	}

	@Test
	public void addBatchEmployeesFailureTest() {
		Employee empOne = new Employee(null, "John", "Wick", 10000L, new Department());

		when(employeeRepository.findByFirstNameAndLastName(empOne.getFirstName(), empOne.getLastName())).thenReturn(Optional.of(empOne));
		//test
		Map<String, List<Employee>> response = employeeService.addEmployees(Arrays.asList(empOne));
		System.out.println(empOne);
		assertEquals(response.containsKey(FALSE.toString()), TRUE);
	}

	@Test
	public void updateEmployeesSuccessTest() {
		Department department = new Department(1L, "Engineering", "Pune");
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, department);
		Optional<Employee> optionalEmployee = Optional.of(empOne);

		when(departmentRepository.findByDepartmentNameAndLocation(department.getDepartmentName(),
				department.getLocation())).thenReturn(Optional.of(department));
		when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);
		when(employeeService.updateEmployee(empOne.getId(), empOne)).thenReturn(empOne);

		//test
		Employee response = employeeService.updateEmployee(empOne.getId(), empOne);

		assertEquals(response.getFirstName(), empOne.getFirstName());
	}

	@Test
	public void updateEmployeesFailureTest() {
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
		Optional<Employee> optionalEmployee = Optional.empty();
		when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);

		//test
		Employee response = employeeService.updateEmployee(empOne.getId(), empOne);

		assertNull(response);
	}

	@Test
	public void updateBatchEmployeesSuccessTest() {
		List<Employee> list = new ArrayList<>();
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
		list.add(empOne);

		Optional<Employee> optionalEmployee = Optional.of(empOne);
		when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);
		when(employeeRepository.save(empOne)).thenReturn(empOne);

		//test
		List<Employee> response = employeeService.updateEmployees(Collections.singletonList(empOne));

		assertEquals(response.get(0).getFirstName(), empOne.getFirstName());
	}

	@Test
	public void deleteEmployeesSuccessTest() {
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
		Optional<Employee> optionalEmployee = Optional.of(empOne);

		when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);

		//test
		Boolean response = employeeService.deleteEmployee(empOne.getId());

		assertEquals(response, true);
	}

	@Test
	public void deleteEmployeesFailureTest() {
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());
		Optional<Employee> optionalEmployee = Optional.empty();

		when(employeeRepository.findById(1L)).thenReturn(optionalEmployee);

		//test
		Boolean response = employeeService.deleteEmployee(empOne.getId());

		assertEquals(response, false);
	}

	@Test
	public void deleteBatchEmployeesSuccessTest() {
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());

		when(employeeRepository.findById(empOne.getId())).thenReturn(Optional.of(empOne));

		//test
		Map<String, List<Long>> response = employeeService.deleteEmployees(Collections.singletonList(empOne.getId()));

		assertEquals(response.containsKey(TRUE.toString()), TRUE);
	}

	@Test
	public void deleteBatchEmployeesFailureTest() {
		Employee empOne = new Employee(1L, "John", "Wick", 10000L, new Department());

		when(employeeRepository.findById(empOne.getId())).thenReturn(Optional.empty());

		//test
		Map<String, List<Long>> response = employeeService.deleteEmployees(Collections.singletonList(empOne.getId()));

		assertEquals(response.containsKey(FALSE.toString()), TRUE);
	}

	@Test
	public void deleteAllSuccessTest() {
		//test
		Map<String, List<Long>> response = employeeService.deleteEmployees(null);

		assertEquals(response.containsKey(TRUE.toString()), TRUE);
	}
}
