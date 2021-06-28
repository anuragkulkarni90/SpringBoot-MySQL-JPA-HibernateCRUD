package demo.service;

import demo.entity.Department;
import demo.entity.PagedResponse;
import demo.specification.SearchCriteria;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DepartmentService {
	PagedResponse<List<Department>> getBatchDepartments(Integer pageNo, Integer pageSize, String sortBy,
	                                                    List<SearchCriteria> searchCriteria);

	Department getDepartment(Long id, String departmentName, String location);

	Boolean addDepartment(Department department);

	Map<String, List<Department>> addDepartments(List<Department> departments);

	List<Department> updateDepartments(List<Department> departments);

	Department updateDepartment(long id, Department department);

	Boolean deleteDepartment(long id);

	Map<String, List<Long>> deleteDepartments(List<Long> ids);

	Optional<Department> getDepartmentById(Long id);
}
