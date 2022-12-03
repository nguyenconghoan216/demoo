package advjava.com.project1;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Project1Application {

	public static StudentList parseJSOn(String url) throws ParseException {
		Client client =  Client.create();
		WebResource webResource = client.resource(url);

		ClientResponse clientResponse  = webResource.accept("application/json").get(ClientResponse.class);
		if(clientResponse.getStatus() != 200){
			throw new RuntimeException("Failed" + clientResponse);
		}
		JSONArray pageData = (JSONArray) new JSONParser().parse(clientResponse.getEntity(String.class));
//		System.out.println("page data: \n"+pageData);

		StudentList studentList = new StudentList();
		for(Object o: pageData) {
			JSONObject studentObject = (JSONObject) o;
//			System.out.println("Student: "+student);

			int id = Integer.parseInt(studentObject.get("id").toString());
			String fName = String.valueOf(studentObject.get("first_name"));
			String mail = String.valueOf(studentObject.get("email"));
			String gender = String.valueOf(studentObject.get("gender"));

			JSONArray courseListObject = (JSONArray) studentObject.get("course");
			CourseList courses = new CourseList();
			if (courseListObject != null) {
				for (Object obj : courseListObject) {
					JSONObject courseList = (JSONObject) obj;

					String courseNo = String.valueOf(courseList.get("courseNo"));
					String grade = String.valueOf(courseList.get("grade"));
					int creditHours = Integer.parseInt(courseList.get("creditHours").toString());
					Course course = new Course(courseNo, grade, creditHours);
					courses.addCourse(course);
				}
			}
			Student student = new Student(id, fName, mail, gender, courses);
			studentList.addStudent(student);
		}
		return studentList;

	}

	public static void main(String[] args) throws ParseException {
		ConfigurableApplicationContext context = SpringApplication.run(Project1Application.class, args);

		StudentList studentList = parseJSOn("https://hccs-advancejava.s3.amazonaws.com/student_course.json");
		System.out.println(studentList.getStudentList());

		System.out.println("Search result:");
		System.out.println("Search by name: "+studentList.searchByName(" Aida ")+"\n");
		System.out.println("Search by name: "+studentList.searchByName(" Alex ")+"\n");
		System.out.println("Search by CourseNo: "+studentList.searchByCourseNo("ENG")+"\n");
		System.out.println("Search by CourseNo: "+studentList.searchByCourseNo("NCH")+"\n");

		studentList.getStudentList().forEach(e -> {
//			System.out.println(e.getCourse());
			if (!e.getCourse().getCourseList().isEmpty()) {
				System.out.println("GPA of " + e.getfName() + ": " + e.calGPA());
			} else {
				System.out.println("Cannot calculate " + e.getfName() + "'s GPA");
			}
		});

	}
}