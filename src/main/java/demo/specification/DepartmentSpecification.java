package demo.specification;

import demo.entity.Department;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class DepartmentSpecification implements Specification<Department> {

	private final List<SearchCriteria> list;

	public DepartmentSpecification() {
		this.list = new ArrayList<>();
	}

	public void add(SearchCriteria criteria) {
		list.add(criteria);
	}

	@Override
	public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		//create a new predicate list
		List<Predicate> predicates = new ArrayList<>();

		//add add criteria to predicates
		for (SearchCriteria criteria : list) {
			SearchOperation operation = criteria.getOperation();
			switch (operation) {
				case GREATER_THAN:
					predicates.add(builder.greaterThan(
							root.get(criteria.getKey()), criteria.getValue().toString()));
					break;
				case LESS_THAN:
					predicates.add(builder.lessThan(
							root.get(criteria.getKey()), criteria.getValue().toString()));
					break;
				case GREATER_THAN_EQUAL:
					predicates.add(builder.greaterThanOrEqualTo(
							root.get(criteria.getKey()), criteria.getValue().toString()));
					break;
				case LESS_THAN_EQUAL:
					predicates.add(builder.lessThanOrEqualTo(
							root.get(criteria.getKey()), criteria.getValue().toString()));
					break;
				case NOT_EQUAL:
					predicates.add(builder.notEqual(
							root.get(criteria.getKey()), criteria.getValue()));
					break;
				case EQUAL:
					predicates.add(builder.equal(
							root.get(criteria.getKey()), criteria.getValue()));
					break;
				case MATCH:
					predicates.add(builder.like(
							builder.lower(root.get(criteria.getKey())),
							"%" + criteria.getValue().toString().toLowerCase() + "%"));
					break;
				case MATCH_END:
					predicates.add(builder.like(
							builder.lower(root.get(criteria.getKey())),
							criteria.getValue().toString().toLowerCase() + "%"));
					break;
				case MATCH_START:
					predicates.add(builder.like(
							builder.lower(root.get(criteria.getKey())),
							"%" + criteria.getValue().toString().toLowerCase()));
					break;
				case IN:
					predicates.add(builder.in(root.get(criteria.getKey())).value(criteria.getValue()));
					break;
				case NOT_IN:
					predicates.add(builder.not(root.get(criteria.getKey())).in(criteria.getValue()));
					break;
				default:
					break;
			}
		}
		return builder.and(predicates.toArray(new Predicate[0]));
	}
}
