package advjava.com.project1;


import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CourseList {
    private ArrayList<Course> list = new ArrayList<>();

    public CourseList(){
//      System.out.println("I am in CourseList Constructor");
    }

    public ArrayList<Course> getCourseList() {
        return list;
    }

    public ArrayList<Course> setCourseList(ArrayList<Course> courseList){
        list = courseList;
        return list;
    }

    public ArrayList<Course> addCourse (Course course){
        getCourseList().add(course);
        return list;
    }

    @Override
    public String toString() {
        return "CourseList{" +
                "list=" + list +
                '}';
    }
}