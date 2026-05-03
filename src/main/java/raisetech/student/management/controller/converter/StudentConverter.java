package raisetech.student.management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.student.management.controller.dto.CourseStatusResponse;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。
   * 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @param studentList　受講生一覧
   * @param studentCourseList　受講生コース情報のリスト
   * @return　受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    studentList.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getId().equals(studentCourse.getStudentsId()))
          .collect(Collectors.toList());

      studentDetail.setStudentCourseList(convertStudentCourseList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  /**
   * 受講生コースステータスをレスポンス用DTOに変換します。
   *
   * @param status 受講生コースステータス（Entity）
   * @return 受講生コースステータス（DTO）
   */
  public CourseStatusResponse convertCourseStatusResponse(CourseStatus status) {
    CourseStatusResponse response = new CourseStatusResponse();
    response.setId(status.getId());
    response.setStudentCourseId(status.getStudentCourseId());
    response.setCourseStatus(status.getCourseStatus());
    return response;
  }

  /**
   * 受講生コースステータスのリストをレスポンス用DTOのリストに変換します。
   *
   * @param statuses 受講生コースステータスのリスト（Entity）
   * @return 受講生コースステータスのリスト（DTO）
   */
  public List<CourseStatusResponse> convertCourseStatusResponseList(List<CourseStatus> statuses) {
    return statuses.stream()
        .map(this::convertCourseStatusResponse)
        .collect(Collectors.toList());
  }

}
