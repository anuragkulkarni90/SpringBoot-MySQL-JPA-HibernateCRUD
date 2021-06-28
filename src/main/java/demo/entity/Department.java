package demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String departmentName;

	private String location;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id", referencedColumnName = "id")
	private Employee employee;

	public Department() {

	}

	public Department(Long id, String departmentName, String location) {
		this.id = id;
		this.departmentName = departmentName;
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Department{" +
				       "id=" + id +
				       ", departmentName='" + departmentName + '\'' +
				       ", location='" + location + '\'' +
				       '}';
	}
}
