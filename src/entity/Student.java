package entity;

import java.util.ArrayList; 
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

// @Entity is for telling this class ot its objects will be mapped to the database's table 
// @Table is for telling the table's name on the schema 
@Entity
@Table(name="student")
public class Student {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	int id;
	@Column(name="first_name")
	String first_name;
	@Column(name="last_name")
	String last_name;
	@Column(name="email")
	String email;
	// @JoinTable act like the intermediate table between the student table and the course table 
	/* As the primary key of the join table named course_student includes both the student_id column and course_id colum, 
	 these two columns can have duplicate values, allow the many-to-many relationship to be possible */
	// Lazy fetch type is the default for @ManyToMany, so making it explicitly here is optional
	// I won't use the delete cascade type here because it will hram the many-to-many relationship
	@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(name="course_student", joinColumns=@JoinColumn(name="student_id"), inverseJoinColumns=@JoinColumn(name="course_id") )
	private List<Course> courses;
	
	public Student(){}

	public Student(String first_name, String last_name, String email) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	
	// Convenience method for adding course objects into its List<Course>
	public void addCourse(Course course) {
		if (courses == null) {
			courses = new ArrayList<>();
		}
		
		if (!courses.contains(course)){
			courses.add(course);
		}
		
		List<Student> students = course.getStudents();
		
		if (!(students == null)) {
			if (!students.contains(this)) {
				students.add(this);
			}
		}
	}

	// Override toString() for printing out values from fields for debugging purposes , not required
	@Override
	public String toString() {
		return "Student [id=" + id + ", first_name=" + first_name + ", last_name=" + last_name + ", email="
				+ email + "]";
	}

	
	
}
