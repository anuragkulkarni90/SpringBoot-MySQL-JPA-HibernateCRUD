package demo.repositories;

import demo.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, PagingAndSortingRepository<Employee, Long>,
		                                            JpaSpecificationExecutor<Employee> {
	Optional<Employee> findByFirstNameAndLastName(String firstName, String lastName);

	@Query("select id from Employee")
	List<Long> findAllIds();

	Page<Employee> findAll(Pageable pageable);
}