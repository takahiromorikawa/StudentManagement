package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.service.StudentsService;

@RestController
public class StudentController {

  private StudentsService service;

  @Autowired
  public StudentController(StudentsService service) {
    this.service = service;
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return service.searchStudentList();
  }

  @GetMapping("/studentsCoursesList")
  public List<StudentsCourses> getStudentsCoursesList() {
    return service.searchStudentsCoursesList();
  }

  // ★ 30代のみ
  @GetMapping("/students/30s")
  public List<Student> getStudentsIn30s() {
    return service.searchStudentsIn30s();
  }

  // * Javaコースのみ
  @GetMapping("/studentsCourses/Java")
  public List<StudentsCourses> getStudentsCoursesInJava() {
    return service.searchInJava();
  }

}
