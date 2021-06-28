package demo.service.impl;

import demo.entity.Department;
import demo.entity.PagedResponse;
import demo.repositories.DepartmentRepository;
import demo.service.DepartmentService;
import demo.specification.DepartmentSpecification;
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
import static demo.constants.EmployeeConstants.DEPARTMENT_NAME_KEY;
import static demo.constants.EmployeeConstants.ID_KEY;
import static demo.constants.EmployeeConstants.LOCATION_KEY;
import static demo.specification.SearchOperation.EQUAL;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private final DepartmentRepository departmentRepository;

	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Override
	public PagedResponse<List<Department>> getBatchDepartments(Integer pageNo, Integer pageSize, String sortBy,
	                                                           List<SearchCriteria> searchCriteriaList) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Page<Department> pagedResult = getDepartments(searchCriteriaList, paging);
		int count = (int) departmentRepository.count();
		int totalPages = (count % pageSize == 0) ? (count / pageSize) : (count / pageSize) + 1;

		if (nonNull(pagedResult) && pagedResult.hasContent()) {
			return new PagedResponse<>(pageNo, pagedResult.getContent().size(), count, totalPages,
					pagedResult.getContent());
		} else {
			return new PagedResponse<>(pageNo, 0, new ArrayList<>());
		}
	}

	@Override
	public Department getDepartment(Long id, String departmentName, String location) {
		DepartmentSpecification departmentSpecification = getSpecification(id, departmentName, location);
		Optional<Department> department = departmentRepository.findOne(departmentSpecification);
		return department.orElse(null);
	}

	@Override
	public Boolean addDepartment(Department department) {
		Optional<Department> optionalDepartment =
				departmentRepository.findByDepartmentNameAndLocation(department.getDepartmentName(),
						department.getLocation());
		if (!optionalDepartment.isPresent()) {
			departmentRepository.save(department);
			return TRUE;
		}
		return FALSE;
	}

	@Override
	public Map<String, List<Department>> addDepartments(List<Department> departments) {
		Map<String, List<Department>> listMap = new HashMap<>();
		List<Department> existingDepartments = new ArrayList<>();
		for (Department department : departments) {
			Boolean aBoolean = addDepartment(department);
			if (!aBoolean.booleanValue()) {
				existingDepartments.add(department);
			}
		}
		if (isNotEmpty(existingDepartments)) {
			listMap.put(FALSE.toString(), existingDepartments);
		} else {
			departments = departmentRepository.saveAll(departments);
			listMap.put(TRUE.toString(), departments);
		}
		return listMap;
	}

	@Override
	public List<Department> updateDepartments(List<Department> departments) {
		List<Department> departmentList = new ArrayList<>();
		for (Department department : departments) {
			departmentList.add(updateDepartment(department.getId(), department));
		}
		return departmentList;
	}

	@Override
	public Department updateDepartment(long id, Department department) {
		Optional<Department> departmentOne = departmentRepository.findById(id);
		if (departmentOne.isPresent()) {
			Department existingDepartment = departmentOne.get();
			existingDepartment.setDepartmentName(nonNull(department.getDepartmentName()) ?
				department.getDepartmentName() : existingDepartment.getDepartmentName());
			existingDepartment.setLocation(nonNull(department.getLocation()) ?
				department.getLocation() : existingDepartment.getLocation());
			return departmentRepository.save(existingDepartment);
		}
		return null;
	}

	@Override
	public Boolean deleteDepartment(long id) {
		Optional<Department> department = getDepartmentById(id);
		if (department.isPresent()) {
			departmentRepository.delete(department.get());
			return TRUE;
		}
		return FALSE;

	}

	@Override
	public Map<String, List<Long>> deleteDepartments(List<Long> ids) {
		Map<String, List<Long>> response = new HashMap<>();
		List<Long> departmentIds = new ArrayList<>();
		if (isNotEmpty(ids)) {
			for (Long id : ids) {
				Optional<Department> optionalDepartment = departmentRepository.findById(id);
				if (!optionalDepartment.isPresent()) {
					departmentIds.add(id);
				}
			}

			if (isNotEmpty(departmentIds)) {
				response.put(FALSE.toString(), departmentIds);
			} else {
				ids.forEach(departmentRepository::deleteById);
				response.put(TRUE.toString(), ids);
			}
		} else {
			departmentIds = departmentRepository.findAllIds();
			departmentIds.forEach(departmentRepository::deleteById);
			response.put(TRUE.toString(), departmentIds);
		}
		return response;
	}

	@Override
	public Optional<Department> getDepartmentById(Long id) {
		return departmentRepository.findById(id);
	}

	private DepartmentSpecification getSpecification(Long id, String departmentName, String location) {
		DepartmentSpecification departmentSpecification = new DepartmentSpecification();
		if (nonNull(id)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(ID_KEY);
			searchCriteria.setValue(id);
			searchCriteria.setOperation(EQUAL);
			departmentSpecification.add(searchCriteria);
		}
		if (!isNullOrEmpty(departmentName)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(DEPARTMENT_NAME_KEY);
			searchCriteria.setValue(departmentName);
			searchCriteria.setOperation(EQUAL);
			departmentSpecification.add(searchCriteria);
		}
		if (!isNullOrEmpty(location)) {
			SearchCriteria searchCriteria = new SearchCriteria();
			searchCriteria.setKey(LOCATION_KEY);
			searchCriteria.setValue(location);
			searchCriteria.setOperation(EQUAL);
			departmentSpecification.add(searchCriteria);
		}
		return departmentSpecification;
	}

	private Page<Department> getDepartments(List<SearchCriteria> searchCriteriaList, Pageable paging) {
		Page<Department> pagedResult;
		if (isNotEmpty(searchCriteriaList)) {
			DepartmentSpecification departmentSpecification = new DepartmentSpecification();
			searchCriteriaList.forEach(departmentSpecification::add);
			pagedResult = departmentRepository.findAll(departmentSpecification, paging);
		} else {
			pagedResult = departmentRepository.findAll(paging);
		}
		return pagedResult;
	}

}
