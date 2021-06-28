package demo.endpoint;

import demo.entity.Employee;
import demo.entity.PagedResponse;
import demo.entity.SuccessResponse;
import demo.service.EmployeeService;
import demo.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static demo.constants.EmployeeConstants.DEPARTMENT_NAME_KEY;
import static demo.constants.EmployeeConstants.FIRST_NAME_KEY;
import static demo.constants.EmployeeConstants.ID_KEY;
import static demo.constants.EmployeeConstants.LAST_NAME_KEY;
import static demo.constants.EmployeeConstants.LOCATION_KEY;
import static demo.constants.EmployeeConstants.SALARY_KEY;
import static demo.constants.EmployeeRestConstants.BATCH;
import static demo.constants.EmployeeRestConstants.CREATE;
import static demo.constants.EmployeeRestConstants.EMPLOYEE;
import static demo.constants.EmployeeRestConstants.ID;
import static demo.constants.EmployeeRestConstants.UPDATE;
import static demo.constants.MessageConstants.EMPLOYEE_ALREADY_EXIST;
import static demo.constants.MessageConstants.EMPLOYEE_CREATE_FAILED;
import static demo.constants.MessageConstants.EMPLOYEE_CREATE_SUCCESS;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_FAILED;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_FAILED_WITH_IDS;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_SUCCESS;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_SUCCESS_WITH_IDS;
import static demo.constants.MessageConstants.EMPLOYEE_FETCH_FAILED;
import static demo.constants.MessageConstants.EMPLOYEE_FETCH_SUCCESS;
import static demo.constants.MessageConstants.EMPLOYEE_NOT_EXIST;
import static demo.constants.MessageConstants.EMPLOYEE_UPDATE_FAILED;
import static demo.constants.MessageConstants.EMPLOYEE_UPDATE_SUCCESS;
import static demo.constants.MessageConstants.SOMETHING_WENT_WRONG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(EMPLOYEE)
public class EmployeeController {

	@Autowired
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping(value = BATCH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> getBatchEmployees(@RequestParam(defaultValue = "0") Integer pageNo,
	                                                         @RequestParam(defaultValue = "10") Integer pageSize,
	                                                         @RequestParam(defaultValue = "id") String sortBy,
	                                                         @RequestBody(required = false) List<SearchCriteria> searchCriteria) {
		SuccessResponse<?> successResponse;
		PagedResponse<List<Employee>> employees = employeeService.getBatchEmployees(pageNo, pageSize, sortBy,
                searchCriteria);
		if (isNotEmpty(employees.getResult())) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_FETCH_SUCCESS, employees);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_FETCH_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> getEmployee(@RequestParam(value = ID_KEY, defaultValue = "") Long id,
	                                                   @RequestParam(value = FIRST_NAME_KEY, defaultValue = "") String firstName,
	                                                   @RequestParam(value = LAST_NAME_KEY, defaultValue = "") String lastName,
	                                                   @RequestParam(value = SALARY_KEY, defaultValue = "") Long salary,
	                                                   @RequestParam(value = DEPARTMENT_NAME_KEY, defaultValue = "") String departmentName,
	                                                   @RequestParam(value = LOCATION_KEY, defaultValue = "") String location
	) {
		SuccessResponse<?> successResponse;
		List<Employee> employee = employeeService.getEmployee(id, firstName, lastName, salary, departmentName,
                location);
		if (nonNull(employee)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_FETCH_SUCCESS, employee);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_FETCH_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@GetMapping(value = ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> getEmployee(@PathVariable Long id
	) {
		SuccessResponse<?> successResponse;
		Employee employee = employeeService.getEmployeeById(id);
		if (nonNull(employee)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_FETCH_SUCCESS, employee);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_FETCH_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PostMapping(value = CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> addEmployee(@RequestBody Employee employee) {
		SuccessResponse<?> successResponse;
		Boolean response = employeeService.addEmployee(employee);
		if (response) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_CREATE_SUCCESS, employee);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_CREATE_FAILED, EMPLOYEE_ALREADY_EXIST);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PostMapping(value = BATCH + CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> addEmployees(@RequestBody List<Employee> employees) {
		SuccessResponse<?> successResponse;
		Map<String, List<Employee>> response = employeeService.addEmployees(employees);
		if (response.containsKey(TRUE.toString())) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_CREATE_SUCCESS, response.get(TRUE.toString()));
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_ALREADY_EXIST, response.get(FALSE.toString()));
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PutMapping(value = ID + UPDATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
		SuccessResponse<?> successResponse;
		Employee updatedEmployee = employeeService.updateEmployee(id, employee);
		if (nonNull(updatedEmployee)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_UPDATE_SUCCESS, updatedEmployee);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_UPDATE_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PutMapping(value = UPDATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> updateEmployees(@RequestBody List<Employee> employees) {
		SuccessResponse<?> successResponse;
		List<Employee> updatedEmployees = employeeService.updateEmployees(employees);
		if (isNotEmpty(updatedEmployees)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_UPDATE_SUCCESS, updatedEmployees);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_UPDATE_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@DeleteMapping(value = ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> deleteEmployee(@PathVariable long id) {
		SuccessResponse<?> successResponse;
		Boolean response = employeeService.deleteEmployee(id);
		if (response) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_DELETE_SUCCESS);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_DELETE_FAILED, EMPLOYEE_FETCH_FAILED);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@DeleteMapping(value = BATCH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse> deleteEmployees(@RequestBody(required = false) List<Long> ids) {
		SuccessResponse<?> successResponse;
		Map<String, List<Long>> response = employeeService.deleteEmployees(ids);
		if (response.containsKey(TRUE.toString())) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
                    EMPLOYEE_DELETE_SUCCESS, EMPLOYEE_DELETE_SUCCESS_WITH_IDS + response.get(TRUE.toString()));
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value(), EMPLOYEE_NOT_EXIST,
                    EMPLOYEE_DELETE_FAILED_WITH_IDS + response.get(FALSE.toString()));
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}
}
