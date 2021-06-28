package demo.specification;

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
import static demo.specification.SearchOperation.IN;
import static demo.specification.SearchOperation.LESS_THAN;
import static demo.specification.SearchOperation.LESS_THAN_EQUAL;
import static demo.specification.SearchOperation.MATCH;
import static demo.specification.SearchOperation.MATCH_END;
import static demo.specification.SearchOperation.MATCH_START;
import static demo.specification.SearchOperation.NOT_EQUAL;
import static demo.specification.SearchOperation.NOT_IN;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeSpecificationTest {

	@MockBean
	Root<Employee> root;

	@MockBean
	CriteriaQuery<?> query;

	@MockBean
	CriteriaBuilder builder;

	@Autowired
	EmployeeSpecification employeeSpecification;

	@Test
	public void toPredicateTest() {
		SearchCriteria searchCriteria;
		searchCriteria = new SearchCriteria("id", 1L, GREATER_THAN);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, LESS_THAN);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, GREATER_THAN_EQUAL);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, LESS_THAN_EQUAL);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, NOT_EQUAL);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("id", 1L, EQUAL);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("firstName", "John", MATCH);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("firstName", "John", MATCH_START);
		employeeSpecification.add(searchCriteria);
		searchCriteria = new SearchCriteria("firstName", "John", MATCH_END);
		employeeSpecification.add(searchCriteria);

		Predicate predicate = employeeSpecification.toPredicate(root, query, builder);
		assertEquals(TRUE, predicate.isNotNull());
	}
}
