package demo;
  
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entity.Course;
import entity.InstructorDetail;
import entity.Instructor;
import entity.Review;
import entity.Student;

public class DeleteManyToManyDemo {
	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		SessionFactory sessionFactory = configuration.configure("hibernate.cfg.xml").addAnnotatedClass(Instructor.class).
				addAnnotatedClass(InstructorDetail.class).addAnnotatedClass(Course.class).addAnnotatedClass(Review.class)
				.addAnnotatedClass(Student.class).buildSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		
		try {
			
			/* Create and save two course objects and three student objects into the database, we well as forming their 
			 many-to-many relationship */
			session.beginTransaction();
			Course course1 = new Course("Course 1");
			Course course2 = new Course("Course 2");
			Student student1 = new Student("Student 1", "C","example@gmail.com");
			Student student2 = new Student("Student 2", "C","example@gmail.com");
			Student student3 = new Student("Student 3", "C","example@gmail.com");
			course1.addStudent(student1);
			course1.addStudent(student2);
			course2.addStudent(student2);
			course2.addStudent(student3);
			
			session.save(course1);
			session.save(course2);
			session.save(student1);
			session.save(student2);
			session.save(student3);
			session.getTransaction().commit();
			session.close();
			
			// Rereading saved records and print them out to see their relationship
			
			Session session2 = sessionFactory.getCurrentSession();
			session2.beginTransaction();
			Course returnedCourse1 = session2.get(Course.class, course1.getId());
			Course returnedCourse2 = session2.get(Course.class, course2.getId());
			Student returnedStudent1 = session2.get(Student.class, student1.getId());
			Student returnedStudent2 = session2.get(Student.class, student2.getId());
			Student returnedStudent3 = session2.get(Student.class, student3.getId());
			
			System.out.println("Course 1: " + returnedCourse1.getStudents() + "\n");
			System.out.println("Course 2: " + returnedCourse2.getStudents() + "\n");
			System.out.println("Student 1: " + returnedStudent1.getCourses() + "\n");
			System.out.println("Student 2: " + returnedStudent2.getCourses() + "\n");
			System.out.println("Student 3: " + returnedStudent3.getCourses() + "\n");
			
			session2.getTransaction().commit();
			session2.close();
			
			// Delete the first course object and the second student object
			/* We can see that because the delete cascade effect is not available, objects that
			 associate with deleted objects will not be deleted,   */
			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(course1.getId(), Course.class, sessionFactory.getCurrentSession());
			StaticFunctions.deleteRecordOnDatabaseByPersistentObject(student2.getId(), Student.class, sessionFactory.getCurrentSession());
			
			Session session3 = sessionFactory.getCurrentSession();
			session3.beginTransaction();
			Course returnedCourseObject = session3.get(Course.class, course2.getId());
			Student returnedStudentObject1 = session3.get(Student.class, student1.getId());
			Student returnedStudentObject3 = session3.get(Student.class, student3.getId());
			
			System.out.println("Course 2: " + returnedCourseObject.getStudents() + "\n");
			System.out.println("Student 1: " + returnedStudentObject1.getCourses() + "\n");
			System.out.println("Student 3: " + returnedStudentObject3.getCourses() + "\n");
			
			session3.getTransaction().commit();
			session3.close(); 
		} finally {
			sessionFactory.close();
		}
	}

}
