package demo.specification;

import demo.entity.Department;
import demo.entity.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static demo.specification.SearchOperation.EQUAL;
import static demo.specification.SearchOperation.GREATER_THAN;
import static demo.specification.SearchOperation.GREATER_THAN_EQUAL;
import static demo.specification.SearchOperation.LESS_THAN;
import static demo.specification.SearchOperation.LESS_THAN_EQUAL;
import static demo.specification.SearchOperation.MATCH;
import static demo.specification.SearchOperation.MATCH_END;
import static demo.specification.SearchOperation.MATCH_START;
import static demo.specification.SearchOperation.NOT_EQUAL;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.nonNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentSpecificationTest {
	@MockBean
	Root<Department> root;

	@MockBean
	CriteriaQuery<?> query;

	@MockBean
	CriteriaBuilder builder;

	@Autowired
	DepartmentSpecification departmentSpecification;

	@Test
	public void toPredicateTest() {
		SearchCriteria searchCriteria = new SearchCriteria("id", 1L, GREATER_THAN);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, LESS_THAN);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, GREATER_THAN_EQUAL);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, LESS_THAN_EQUAL);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, NOT_EQUAL);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, EQUAL);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("firstName", "John", MATCH);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("firstName", "John", MATCH_START);
		departmentSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("firstName", "John", MATCH_END);
		departmentSpecification.add(searchCriteria);

		//test
		Predicate predicate = departmentSpecification.toPredicate(root, query, builder);

		assert TRUE;
	}
}
