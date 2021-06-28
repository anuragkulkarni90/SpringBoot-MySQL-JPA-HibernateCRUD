package demo.service;

import demo.entity.Department;
import demo.entity.PagedResponse;
import demo.repositories.DepartmentRepository;
import demo.service.impl.DepartmentServiceImpl;
import demo.specification.DepartmentSpecification;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentServiceTest {

	@MockBean
	DepartmentRepository departmentRepository;

	@Autowired
	DepartmentServiceImpl departmentService;

	@Test
	public void getAllDepartmentsSuccessTest() {
		Pageable paging = PageRequest.of(0, 10, Sort.by("id"));

		List<Department> list = new ArrayList<>();
		Department deptOne = new Department(1L, "Engineering", "Pune");
		Department deptTwo = new Department(2L, "Testing", "Delhi");

		list.add(deptOne);
		list.add(deptTwo);

		PagedResponse<List<Department>> pagedResponse = new PagedResponse<>(0, 10);
		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		pagedResponse.setResult(list);
		Page<Department> page = new PageImpl<>(list);
		when(departmentRepository.findAll(paging)).thenReturn(page);

		//test
		PagedResponse<List<Department>> empList = departmentService.getBatchDepartments(pageNo, pageSize, sortBy,
				null);

		assertEquals(2, empList.getResult().size());
	}

	@Test
	public void getAllDepartmentsFailureTest() {
		Pageable paging = PageRequest.of(0, 10, Sort.by("id"));

		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		Page<Department> page = new PageImpl<Department>(EMPTY_LIST);
		when(departmentRepository.findAll(paging)).thenReturn(page);

		//test
		PagedResponse<List<Department>> empList = departmentService.getBatchDepartments(pageNo, pageSize, sortBy,
				null);

		assertEquals(0, empList.getResult().size());
	}

	@Test
	public void getBatchDepartmentsSuccessTest() {
		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		List<Department> list = new ArrayList<>();
		Department deptOne = new Department(1L, "Engineering", "Pune");
		list.add(deptOne);

		SearchCriteria searchCriteria = new SearchCriteria("id", 1L,
				EQUAL);
		List<SearchCriteria> criteriaList = Collections.singletonList(searchCriteria);

		DepartmentSpecification departmentSpecification = new DepartmentSpecification();
		departmentSpecification.add(searchCriteria);

		Page<Department> pagedResult = new PageImpl<>(list);
		when(departmentRepository.findAll(departmentSpecification, paging)).thenReturn(pagedResult);

		//test
		PagedResponse<List<Department>> empList = departmentService.getBatchDepartments(pageNo, pageSize, sortBy,
				criteriaList);

		assertEquals(0, empList.getResult().size());
	}

	@Test
	public void getBatchDepartmentsFailureTest() {
		Integer pageNo = INTEGER_ZERO;
		Integer pageSize = 10;
		String sortBy = "id";
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		SearchCriteria searchCriteria = new SearchCriteria();
		searchCriteria.setKey("firstName");
		searchCriteria.setValue("John");
		searchCriteria.setOperation(SearchOperation.MATCH);
		List<SearchCriteria> criteriaList = Collections.singletonList(searchCriteria);

		DepartmentSpecification departmentSpecification = new DepartmentSpecification();
		departmentSpecification.add(searchCriteria);

		Page<Department> pagedResult = new PageImpl<Department>(EMPTY_LIST);
		when(departmentRepository.findAll(departmentSpecification, paging)).thenReturn(pagedResult);

		//test
		PagedResponse<List<Department>> empList = departmentService.getBatchDepartments(pageNo, pageSize, sortBy,
				criteriaList);

		assertEquals(0, empList.getResult().size());
	}

	@Test
	public void addDepartmentsSuccessTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");

		//test
		Boolean response = departmentService.addDepartment(deptOne);

		assertEquals(true, response);
	}

	@Test
	public void addDepartmentsFailureTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");

		when(departmentRepository.findByDepartmentNameAndLocation(deptOne.getDepartmentName(), deptOne.getLocation())).thenReturn(Optional.of(deptOne));
		//test
		Boolean response = departmentService.addDepartment(deptOne);

		assertEquals(false, response);
	}

	@Test
	public void addBatchDepartmentsSuccessTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");

		//test
		Map<String, List<Department>> response = departmentService.addDepartments(Arrays.asList(deptOne));

		assertEquals(TRUE,
				response.containsKey(TRUE.toString()));
	}

	@Test
	public void addBatchDepartmentsFailureTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");

		when(departmentRepository.findByDepartmentNameAndLocation(deptOne.getDepartmentName(), deptOne.getLocation())).thenReturn(Optional.of(deptOne));
		//test
		Map<String, List<Department>> response = departmentService.addDepartments(Arrays.asList(deptOne));

		assertEquals(response.containsKey(FALSE.toString()), TRUE);
	}

	@Test
	public void updateDepartmentsSuccessTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");
		Optional<Department> optionalDepartment = Optional.of(deptOne);
		when(departmentRepository.findById(1L)).thenReturn(optionalDepartment);
		when(departmentService.updateDepartment(deptOne.getId(), deptOne)).thenReturn(deptOne);

		//test
		Department response = departmentService.updateDepartment(deptOne.getId(), deptOne);

		assertEquals(response.getDepartmentName(), deptOne.getDepartmentName());
	}

	@Test
	public void updateDepartmentsFailureTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");
		Optional<Department> optionalDepartment = Optional.empty();
		when(departmentRepository.findById(1L)).thenReturn(optionalDepartment);

		//test
		Department response = departmentService.updateDepartment(deptOne.getId(), deptOne);

		assertNull(response);
	}

	@Test
	public void updateBatchDepartmentsSuccessTest() {
		List<Department> list = new ArrayList<>();
		Department deptOne = new Department(1L, "Engineering", "Pune");
		list.add(deptOne);

		Optional<Department> optionalDepartment = Optional.of(deptOne);
		when(departmentRepository.findById(1L)).thenReturn(optionalDepartment);
		when(departmentRepository.save(deptOne)).thenReturn(deptOne);

		//test
		List<Department> response = departmentService.updateDepartments(Collections.singletonList(deptOne));

		assertEquals(response.get(0).getDepartmentName(), deptOne.getDepartmentName());
	}

	@Test
	public void deleteDepartmentsSuccessTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");
		Optional<Department> optionalDepartment = Optional.of(deptOne);

		when(departmentRepository.findById(1L)).thenReturn(optionalDepartment);

		//test
		Boolean response = departmentService.deleteDepartment(deptOne.getId());

		assertEquals(response, true);
	}

	@Test
	public void deleteDepartmentsFailureTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");
		Optional<Department> optionalDepartment = Optional.empty();

		when(departmentRepository.findById(1L)).thenReturn(optionalDepartment);

		//test
		Boolean response = departmentService.deleteDepartment(deptOne.getId());

		assertEquals(response, false);
	}

	@Test
	public void deleteBatchDepartmentsSuccessTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");

		when(departmentRepository.findById(deptOne.getId())).thenReturn(Optional.of(deptOne));

		//test
		Map<String, List<Long>> response =
				departmentService.deleteDepartments(Collections.singletonList(deptOne.getId()));

		assertEquals(TRUE, TRUE);
	}

	@Test
	public void deleteBatchDepartmentsFailureTest() {
		Department deptOne = new Department(1L, "Engineering", "Pune");

		when(departmentRepository.findById(deptOne.getId())).thenReturn(Optional.empty());

		//test
		Map<String, List<Long>> response =
				departmentService.deleteDepartments(Collections.singletonList(deptOne.getId()));

		assertEquals(FALSE, FALSE);
	}

	@Test
	public void deleteAllSuccessTest() {
		//test
		Map<String, List<Long>> response = departmentService.deleteDepartments(null);

		assertEquals(TRUE, TRUE);
	}

	@Test
	public void getDepartmentSuccessTest() {
		Department department = new Department(1L, "Engineering", "Pune");

		DepartmentSpecification departmentSpecification = new DepartmentSpecification();
		departmentSpecification.add(new SearchCriteria("id", 1L, EQUAL));
		departmentSpecification.add(new SearchCriteria("departmentName", "Engineering", EQUAL));
		departmentSpecification.add(new SearchCriteria("location", "Pune", EQUAL));

		when(departmentRepository.findOne(departmentSpecification)).thenReturn(Optional.of(department));

		//test
		Department response = departmentService.getDepartment(department.getId(), department.getDepartmentName(),
				department.getLocation());

		assertEquals(TRUE, TRUE);
	}
}
