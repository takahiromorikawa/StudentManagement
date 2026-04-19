package raisetech.student.management.converter;

import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentConverterTest {

  private StudentConverter converter = new StudentConverter();

  @Test
  void 受講生ごとにコースが正しく紐づくこと() {
    // ===== 準備 =====
    Student student1 = new Student();
    student1.setId(1L);
    student1.setName("A");

    Student student2 = new Student();
    student2.setId(2L);
    student2.setName("B");

    List<Student> studentList = Arrays.asList(student1, student2);

    StudentCourse course1 = new StudentCourse();
    course1.setStudentsId(1L);
    course1.setCourseName("Java");

    StudentCourse course2 = new StudentCourse();
    course2.setStudentsId(1L);
    course2.setCourseName("Spring");

    StudentCourse course3 = new StudentCourse();
    course3.setStudentsId(2L);
    course3.setCourseName("AWS");

    List<StudentCourse> courseList = Arrays.asList(course1, course2, course3);

    // ===== 実行 =====
    List<StudentDetail> result = converter.convertStudentDetails(studentList, courseList);

    // ===== 検証 =====
    // student1
    assertEquals(2, result.get(0).getStudentCourseList().size());
    assertEquals("Java", result.get(0).getStudentCourseList().get(0).getCourseName());
    assertEquals("Spring", result.get(0).getStudentCourseList().get(1).getCourseName());

    // student2
    assertEquals(1, result.get(1).getStudentCourseList().size());
    assertEquals("AWS", result.get(1).getStudentCourseList().get(0).getCourseName());
  }

  @Test
  void コースが存在しない場合空リストになること() {
    Student student = new Student();
    student.setId(1L);

    List<StudentDetail> result = converter.convertStudentDetails(
        List.of(student),
        List.of()
    );

    assertEquals(0, result.get(0).getStudentCourseList().size());
  }

  @Test
  void 関係ないコースは紐づかないこと() {
    Student student = new Student();
    student.setId(1L);

    StudentCourse course = new StudentCourse();
    course.setStudentsId(999L);

    List<StudentDetail> result = converter.convertStudentDetails(
        List.of(student),
        List.of(course)
    );

    assertEquals(0, result.get(0).getStudentCourseList().size());
  }

}


