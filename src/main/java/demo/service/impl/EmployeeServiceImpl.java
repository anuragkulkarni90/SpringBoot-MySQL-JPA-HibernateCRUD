package demo.service.impl;

import demo.entity.Department;
import demo.entity.Employee;
import demo.entity.PagedResponse;
import demo.repositories.DepartmentRepository;
import demo.repositories.EmployeeRepository;
import demo.service.EmployeeService;
import demo.specification.EmployeeSpecification;
import demo.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mysql.cj.util.StringUtils.isNullOrEmpty;
import static demo.constants.EmployeeConstants.DEPARTMENT_KEY;
import static demo.constants.EmployeeConstants.FIRST_NAME_KEY;
import static demo.constants.EmployeeConstants.ID_KEY;
import static demo.constants.EmployeeConstants.LAST_NAME_KEY;
import static demo.constants.EmployeeConstants.SALARY_KEY;
import static demo.specification.SearchOperation.EQUAL;
import static demo.specification.SearchOperation.IN;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private final EmployeeRepository employeeRepository;

	@Autowired
	private final DepartmentRepository departmentRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
	}

	@Override
	public Boolean addEmployee(Employee employee) {
		Optional<Employee> optionalEmployee = employeeRepository.findByFirstNameAndLastName(employee.getFirstName(),
                employee.getLastName());
		if (optionalEmployee.isPresent()) {
			return FALSE;
		}
		Optional<Department> department =
                departmentRepository.findByDepartmentNameAndLocation(employee.getDepartment().getDepartmentName(),
                        employee.getDepartment().getLocation());
		employee.setDepartment(department.orElse(employee.getDepartment()));
		employeeRepository.save(employee);
		return TRUE;
	}

	@Override
	public Employee updateEmployee(long id, Employee employee) {
		Optional<Employee> employeeOne = employeeRepository.findById(id);
		if (employeeOne.isPresent()) {
			Employee existingEmployee = compareEmployee(employee, employeeOne.get());
			if (nonNull(employee.getDepartment())) {
				Department department = new Department();
				department.setDepartmentName(nonNull(employee.getDepartment().getDepartmentName()) ?
                                                     employee.getDepartment().getDepartmentName() :
                                                     existingEmployee.getDepartment().getDepartmentName());
				department.setLocation(nonNull(employee.getDepartment().getLocation()) ?
                                               employee.getDepartment().getLocation() :
                                               existingEmployee.getDepartment().getLocation());
				Optional<Department> department1 =
                        departmentRepository.findByDepartmentNameAndLocation(department.getDepartmentName(),
                                department.getLocation());
				if (!department1.isPresent()) {
					existingEmployee.setDepartment(departmentRepository.save(department));
				} else {
					department.setId(existingEmployee.getDepartment().getId());
					existingEmployee.setDepartment(department);
				}
			}
			return employeeRepository.save(existingEmployee);
		}
		return null;
	}

	private Employee compareEmployee(final Employee employee, final Employee existingEmployee) {
		existingEmployee.setFirstName(nonNull(employee.getFirstName()) ? employee.getFirstName() : existingEmployee.getFirstName());
		existingEmployee.setLastName(nonNull(employee.getLastName()) ? employee.getLastName() : existingEmployee.getLastName());
		existingEmployee.setSalary(nonNull(employee.getSalary()) ? employee.getSalary() : existingEmployee.getSalary());
		return existingEmployee;
	}

	@Override
	public List<Employee> updateEmployees(List<Employee> employees) {
		List<Employee> employeeList = new ArrayList<>();
		for (Employee employee : employees) {
			employeeList.add(updateEmployee(employee.getId(), employee));
		}
		return employeeList;
	}

	@Override
	public Boolean deleteEmployee(long id) {
		Employee employee = getEmployeeById(id);
		if (nonNull(employee)) {
			employeeRepository.delete(employee);
			return TRUE;
		}
		return FALSE;
	}

	@Override
	public Map<String, List<Employee>> addEmployees(List<Employee> employees) {
		Map<String, List<Employee>> listMap = new HashMap<>();
		List<Employee> existingEmployees = new ArrayList<>();
		for (Employee employee : employees) {
			Optional<Employee> optionalEmployee =
                    employeeRepository.findByFirstNameAndLastName(employee.getFirstName(), employee.getLastName());
			if (optionalEmployee.isPresent()) {
				existingEmployees.add(employee);
			}
		}
		if (isNotEmpty(existingEmployees)) {
			listMap.put(FALSE.toString(), existingEmployees);
		} else {
			employees.forEach(this::addEmployee);
			listMap.put(TRUE.toString(), employees);
		}
		return listMap;
	}

	@Override
	public List<Employee> getEmployee(Long id, String firstName, String lastName, Long salary, String departmentName,
                                      String location) {
		List<Department> departments = new ArrayList<>();
		if (!isNullOrEmpty(departmentName) && !isNullOrEmpty(location)) {
			Optional<Department> department = departmentRepository.findByDepartmentNameAndLocation(departmentName,
                    location);
			departments.add(department.orElse(null));
		} else if (!isNullOrEmpty(departmentName)) {
			departments.addAll(departmentRepository.findByDepartmentName(departmentName));
		} else if (!isNullOrEmpty(location)) {
			departments.addAll(departmentRepository.findByLocation(location));
		}

		EmployeeSpecification employeeSpecification = getSpecification(id, firstName, lastName, salary, departments);
		return employeeRepository.findAll(employeeSpecification);
	}

	@Override
	public Map<String, List<Long>> deleteEmployees(List<Long> ids) {
		Map<String, List<Long>> response = new HashMap<>();
		List<Long> employeeIds = new ArrayList<>();
		if (isNotEmpty(ids)) {
			for (Long id : ids) {
				Optional<Employee> optionalEmployee = employeeRepository.findById(id);
				if (!optionalEmployee.isPresent()) {
					employeeIds.add(id);
				}
			}

			if (isNotEmpty(employeeIds)) {
				response.put(FALSE.toString(), employeeIds);
			} else {
				ids.forEach(employeeRepository::deleteById);
				response.put(TRUE.toString(), ids);
			}
		} else {
			employeeIds = employeeRepository.findAllIds();
			employeeIds.forEach(employeeRepository::deleteById);
			response.put(TRUE.toString(), employeeIds);
		}
		return response;
	}

	@Override
	public PagedResponse<List<Employee>> getBatchEmployees(Integer pageNo, Integer pageSize, String sortBy,
                                                           List<SearchCriteria> searchCriteriaList) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Employee> pagedResult = getEmployees(searchCriteriaList, paging);
		int count = (int) employeeRepository.count();
		int totalPages = (count % pageSize == 0) ? (count / pageSize) : (count / pageSize) + 1;
		if (nonNull(pagedResult) && pagedResult.hasContent()) {
			return new PagedResponse<>(pageNo, pagedResult.getContent().size(), count, totalPages,
                    pagedResult.getContent());
		} else {
			return new PagedResponse<>(pageNo, 0, new ArrayList<>());
		}
	}

	@Override
	public Employee getEmployeeById(Long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		return employee.orElse(null);
	}

	private Page<Employee> getEmployees(List<SearchCriteria> searchCriteriaList, Pageable paging) {
		Page<Employee> pagedResult;
		if (isNotEmpty(searchCriteriaList)) {
			EmployeeSpecification employeeSpecification = new EmployeeSpecification();
			searchCriteriaList.forEach(employeeSpecification::add);
			pagedResult = employeeRepository.findAll(employeeSpecification, paging);
		} else {
			pagedResult = employeeRepository.findAll(paging);
		}
		return pagedResult;
	}

	private EmployeeSpecification getSpecification(Long id, String firstName, String lastName, Long salary,
                                                   List<Department> departments) {
		EmployeeSpecification employeeSpecification = new EmployeeSpecification();
		if (nonNull(id)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(ID_KEY);
			searchCriteria.setValue(id);
			searchCriteria.setOperation(EQUAL);
			employeeSpecification.add(searchCriteria);
		}
		if (!isNullOrEmpty(firstName)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(FIRST_NAME_KEY);
			searchCriteria.setValue(firstName);
			searchCriteria.setOperation(EQUAL);
			employeeSpecification.add(searchCriteria);
		}
		if (!isNullOrEmpty(lastName)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(LAST_NAME_KEY);
			searchCriteria.setValue(lastName);
			searchCriteria.setOperation(EQUAL);
			employeeSpecification.add(searchCriteria);
		}
		if (nonNull(salary)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(SALARY_KEY);
			searchCriteria.setValue(salary);
			searchCriteria.setOperation(EQUAL);
			employeeSpecification.add(searchCriteria);
		}
		if (isNotEmpty(departments)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(DEPARTMENT_KEY);
			searchCriteria.setValue(departments);
			searchCriteria.setOperation(IN);
			employeeSpecification.add(searchCriteria);
		}
		return employeeSpecification;
	}
}
