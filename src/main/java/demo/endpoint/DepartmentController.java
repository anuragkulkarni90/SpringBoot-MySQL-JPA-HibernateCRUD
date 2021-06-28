package demo.endpoint;

import demo.entity.Department;
import demo.entity.PagedResponse;
import demo.entity.SuccessResponse;
import demo.service.DepartmentService;
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
import static demo.constants.EmployeeConstants.ID_KEY;
import static demo.constants.EmployeeConstants.LOCATION_KEY;
import static demo.constants.EmployeeRestConstants.BATCH;
import static demo.constants.EmployeeRestConstants.CREATE;
import static demo.constants.EmployeeRestConstants.DEPARTMENT;
import static demo.constants.EmployeeRestConstants.ID;
import static demo.constants.EmployeeRestConstants.UPDATE;
import static demo.constants.MessageConstants.DEPARTMENT_ALREADY_EXISTS;
import static demo.constants.MessageConstants.DEPARTMENT_CREATE_FAILED;
import static demo.constants.MessageConstants.DEPARTMENT_CREATE_SUCCESS;
import static demo.constants.MessageConstants.DEPARTMENT_DELETE_FAILED;
import static demo.constants.MessageConstants.DEPARTMENT_DELETE_FAILED_WITH_IDS;
import static demo.constants.MessageConstants.DEPARTMENT_DELETE_SUCCESS;
import static demo.constants.MessageConstants.DEPARTMENT_DELETE_SUCCESS_WITH_IDS;
import static demo.constants.MessageConstants.DEPARTMENT_FETCH_FAILED;
import static demo.constants.MessageConstants.DEPARTMENT_FETCH_SUCCESS;
import static demo.constants.MessageConstants.DEPARTMENT_NOT_EXIST;
import static demo.constants.MessageConstants.DEPARTMENT_UPDATE_FAILED;
import static demo.constants.MessageConstants.DEPARTMENT_UPDATE_SUCCESS;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_FAILED_WITH_IDS;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_SUCCESS;
import static demo.constants.MessageConstants.EMPLOYEE_DELETE_SUCCESS_WITH_IDS;
import static demo.constants.MessageConstants.EMPLOYEE_NOT_EXIST;
import static demo.constants.MessageConstants.SOMETHING_WENT_WRONG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(DEPARTMENT)
public class DepartmentController {

	@Autowired
	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping(value = BATCH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> getBatchDepartments(@RequestParam(defaultValue = "0") Integer pageNo,
	                                                           @RequestParam(defaultValue = "10") Integer pageSize,
	                                                           @RequestParam(defaultValue = "id") String sortBy,
	                                                           @RequestBody(required = false) List<SearchCriteria> searchCriteria) {
		SuccessResponse<?> successResponse;
		PagedResponse<List<Department>> departments = departmentService.getBatchDepartments(pageNo, pageSize, sortBy,
				searchCriteria);
		if (isNotEmpty(departments.getResult())) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_FETCH_SUCCESS, departments);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_FETCH_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> getDepartment(@RequestParam(value = ID_KEY, defaultValue = "") Long id,
	                                                     @RequestParam(value = DEPARTMENT_NAME_KEY, defaultValue = "") String departmentName,
	                                                     @RequestParam(value = LOCATION_KEY, defaultValue = "") String location
	) {
		SuccessResponse<?> successResponse;
		Department department = departmentService.getDepartment(id, departmentName, location);
		if (nonNull(department)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_FETCH_SUCCESS, department);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_FETCH_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PostMapping(value = CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> addDepartment(@RequestBody Department department) {
		SuccessResponse<?> successResponse;
		Boolean response = departmentService.addDepartment(department);
		if (response) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_CREATE_SUCCESS, department);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_CREATE_FAILED, DEPARTMENT_ALREADY_EXISTS);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PostMapping(value = BATCH + CREATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> addDepartments(@RequestBody List<Department> departments) {
		SuccessResponse<?> successResponse;
		Map<String, List<Department>> response = departmentService.addDepartments(departments);
		if (response.containsKey(TRUE.toString())) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_CREATE_SUCCESS, response.get(TRUE.toString()));
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_ALREADY_EXISTS, response.get(FALSE.toString()));
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PutMapping(value = ID + UPDATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> updateDepartment(@PathVariable long id,
	                                                        @RequestBody Department department) {
		SuccessResponse<?> successResponse;
		Department updatedDepartment = departmentService.updateDepartment(id, department);
		if (nonNull(updatedDepartment)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_UPDATE_SUCCESS, updatedDepartment);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_UPDATE_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@PutMapping(value = UPDATE, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> updateDepartments(@RequestBody List<Department> departments) {
		SuccessResponse<?> successResponse;
		List<Department> updatedDepartments = departmentService.updateDepartments(departments);
		if (isNotEmpty(updatedDepartments)) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_UPDATE_SUCCESS, updatedDepartments);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_UPDATE_FAILED, SOMETHING_WENT_WRONG);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@DeleteMapping(value = ID, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> deleteDepartment(@PathVariable long id) {
		SuccessResponse<?> successResponse;
		Boolean response = departmentService.deleteDepartment(id);
		if (response) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_DELETE_SUCCESS);
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_DELETE_FAILED, DEPARTMENT_FETCH_FAILED);
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}

	@DeleteMapping(value = BATCH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<SuccessResponse<?>> deleteDepartments(@RequestBody(required = false) List<Long> ids) {
		SuccessResponse<?> successResponse;
		Map<String, List<Long>> response = departmentService.deleteDepartments(ids);
		if (response.containsKey(TRUE.toString())) {
			successResponse = new SuccessResponse<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value(),
					DEPARTMENT_DELETE_SUCCESS, DEPARTMENT_DELETE_SUCCESS_WITH_IDS + response.get(TRUE.toString()));
		} else {
			successResponse = new SuccessResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(),
					HttpStatus.BAD_REQUEST.value(), DEPARTMENT_NOT_EXIST,
					DEPARTMENT_DELETE_FAILED_WITH_IDS + response.get(FALSE.toString()));
		}
		return ResponseEntity.status(successResponse.getCode()).body(successResponse);
	}
}
