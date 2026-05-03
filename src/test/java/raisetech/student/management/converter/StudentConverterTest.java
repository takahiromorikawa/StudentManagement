package raisetech.student.management.converter;

import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.CourseStatusResponse;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentConverterTest {

  private StudentConverter converter = new StudentConverter();

  @Test
  void 受講生ごとにコースが正しく紐づくこと() {
    // ===== 準備 =====
    Student student1 = new Student();
    student1.setId(1L);
    student1.setName("A");
    List<Student> studentList = List.of(student1);

    StudentCourse course1 = new StudentCourse();
    course1.setStudentsId(1L);
    course1.setCourseName("Java");
    List<StudentCourse> courseList = List.of(course1);

    List<StudentDetail> result = converter.convertStudentDetails(studentList, courseList);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getStudent().getName()).isEqualTo("A");
    assertThat(result.get(0).getStudentCourseList()).hasSize(1);
    assertThat(result.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo("Java");
  }

  @Test
  void コースが存在しない場合空リストになること() {
    Student student = new Student();
    student.setId(1L);

    List<StudentDetail> result = converter.convertStudentDetails(
        List.of(student),
        List.of()
    );

    assertThat(result.get(0).getStudentCourseList()).isEmpty();
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

  assertThat(result.get(0).getStudentCourseList()).isEmpty();
}

  @Test
  void CourseStatusからResponseDTOに正しく変換できること() {
    // ===== 準備 =====
    CourseStatus status = new CourseStatus();
    status.setId(100L);
    status.setStudentCourseId(1L);
    status.setCourseStatus("受講中");

    CourseStatusResponse result = converter.convertCourseStatusResponse(status);

    assertThat(result.getId()).isEqualTo(100L);
    assertThat(result.getStudentCourseId()).isEqualTo(1L);
    assertThat(result.getCourseStatus()).isEqualTo("受講中");
  }

  @Test
  void CourseStatusのリストが正しく変換できること() {
    // ===== 準備 =====
    CourseStatus status1 = new CourseStatus();
    status1.setCourseStatus("受講中");
    CourseStatus status2 = new CourseStatus();
    status2.setCourseStatus("完了");
    List<CourseStatus> statusList = Arrays.asList(status1, status2);

    List<CourseStatusResponse> result = converter.convertCourseStatusResponseList(statusList);

    assertThat(result).hasSize(2);
    assertThat(result).extracting(CourseStatusResponse::getCourseStatus)
        .containsExactly("受講中", "完了");
  }
}


