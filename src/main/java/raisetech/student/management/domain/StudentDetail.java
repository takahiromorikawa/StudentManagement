package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor

public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;

  public StudentDetail(Student student, List<StudentCourse> studentCourseList) {
    this.student = student;
    this.studentCourseList = studentCourseList;
  }

  public Student getStudent() {
    return student;
  }

  public List<StudentCourse> getStudentCourseList() {
    return studentCourseList == null ? new ArrayList<>() : studentCourseList;
  }
}

