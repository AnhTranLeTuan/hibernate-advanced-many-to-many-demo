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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="course")
public class Course {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="title")
	private String title;
	// @ManyToOne have the default fetch type as FetchType.EAGER, so providing FetchType.EAGER on the property "fetch" will be optional 
	/* Many-to-one mapping between the course and the intructor tables. By default, if we don't
    specify the cascade type, none of the type will be applied. In this case, I specify Cascade types, because not all of them are efficent */
	/* Cascade features of Hibernate seem like the reserve version of cascade features on MySQL. 
	 Specifically, on Hibernate, the parent table will be course table, and the instructor table will be the child table.
	 On the other hand, on MySQL, the parent table will be instructor table, and the course table will be the child table. */
	// This field will be equivalent to the foreign key which will match to the primary key on the instructor table
	@ManyToOne(fetch=FetchType.EAGER, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name="instructor_id")
	private Instructor instructor;
	/* Unidirectional relationship between the Course table and the Review table
	 as all of the cascade effects will be available from the Course table to the Review table, but
	 not all of the cascade effects will be available from the Review table to the Course table */
	@OneToMany(fetch=FetchType.LAZY, mappedBy="course",cascade=CascadeType.ALL)
	private List<Review> reviews;
	// @JoinTable act like the intermediate table between the student table and the course table 
	/* As the primary key of the join table named course_student includes both the student_id column and course_id colum, 
	 these two columns can have duplicate values, allow the many-to-many relationship to be possible */
	// Lazy fetch type is the default for @ManyToMany, so making it explicitly here is optional
	// I won't use the delete cascade type here because it will hram the many-to-many relationship
	@ManyToMany
	@JoinTable(name="course_student", joinColumns=@JoinColumn(name="course_id"), inverseJoinColumns=@JoinColumn(name="student_id") )
	private List<Student> students;
	
	public Course() {}

	public Course(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public void addStudent(Student student) {
		if (students == null) {
			students = new ArrayList<>();
		}
		
		if (!students.contains(student)){
			students.add(student);
		}
		
		List<Course> courses = student.getCourses();
		
		if (!(courses == null)) {
			if (!courses.contains(this)) {
				courses.add(this);
			}
		}
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", title=" + title + ", instructor=" + instructor + "]";
	}

}
