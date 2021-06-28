package demo.repositories;

import demo.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository
		extends JpaRepository<Department, Long>, PagingAndSortingRepository<Department, Long>,
				        JpaSpecificationExecutor<Department> {
	Optional<Department> findByDepartmentNameAndLocation(String departmentName, String location);

	List<Department> findByDepartmentName(String departmentName);

	List<Department> findByLocation(String location);

	@Query("select id from Department")
	List<Long> findAllIds();
}
