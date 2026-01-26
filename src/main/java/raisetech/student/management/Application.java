package raisetech.student.management;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@RestController
public class Application {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private StudentsCoursesRepository studentsCoursesRepository;



@GetMapping("/studentList")
  public List <Student> getStudentList() {
   return studentRepository.search();
}

@GetMapping("/studentsCourses")
  public List<StudentsCourses> getStudentsCoursesList() {
    return studentsCoursesRepository.search();
  }

public static void main(String[] args) {
  SpringApplication.run(Application.class, args);


}

}
