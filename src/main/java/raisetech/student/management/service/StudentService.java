package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.RegisterStudentRequest;
import raisetech.student.management.controller.dto.StudentCourseRequest;
import raisetech.student.management.controller.dto.UpdateStudentRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentCourseRepository;
import raisetech.student.management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private final StudentRepository studentRepository;
  private final StudentCourseRepository studentCourseListRepository;

  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository studentRepository,
      StudentCourseRepository studentCourseListRepository, StudentConverter converter) {
    this.studentRepository = studentRepository;
    this.studentCourseListRepository = studentCourseListRepository;

    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = studentRepository.search();
    List<StudentCourse> studentCourseList = studentCourseListRepository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return　受講生詳細
   */
  public StudentDetail searchStudent(Long id) {
    Student student = studentRepository.searchStudent(id);

    if (student == null) {
      throw new IllegalArgumentException("該当する受講生が存在しません");
    }

    List<StudentCourse> studentCourse = studentCourseListRepository.searchStudentCourse(
        student.getId());
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生の登録を行います。 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値と、コース開始日、コース終了日を設定します。
   *
   * @param request 登録内容を含むリクエストオブジェクト
   * @return　登録された受講生情報とコース情報をまとめた詳細オブジェクト
   */
  @Transactional
  public StudentDetail registerStudent(RegisterStudentRequest request) {

    if (request.getName() == null) {
      throw new NullPointerException("名前は必須です");
    }

    // ① Student作成
    Student s = new Student();
    s.setName(request.getName());
    s.setAge(request.getAge());
    s.setNameKana(request.getNameKana());
    s.setNickname(request.getNickname());
    s.setMailaddress(request.getMailaddress());
    s.setLive(request.getLive());
    s.setSex(request.getSex());
    s.setRemark(request.getRemark());
    s.setDeleted(false);

    // ② DB登録（ここでIDが入る想定）
    studentRepository.registerStudent(s);
    Long generatedStudentId = s.getId(); // ← これ使う

    // ③ コース登録
    List<StudentCourse> courseList = new ArrayList<>();

    if (request.getStudentCourseList() != null) {
      for (StudentCourseRequest course : request.getStudentCourseList()) {

        if (course.getCourseName() == null) {
          continue;
        }

        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName(course.getCourseName());

        studentCourse.setStudentsId(generatedStudentId);

        studentCourseListRepository.registerStudentCourse(studentCourse);
        courseList.add(studentCourse);
      }
    }

    return new StudentDetail(s, courseList);
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param sc                 受講生コース情報
   * @param generatedStudentId 受講生
   */
  private void initStudentsCourse(StudentCourse sc, Long generatedStudentId) {
    LocalDateTime now = LocalDateTime.now();

    sc.setStudentsId(generatedStudentId);
    sc.setStart(now);
    sc.setEndplan(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param request 受講生詳細
   */
  @Transactional
  public void updateStudent(UpdateStudentRequest request) {

    Student student = studentRepository.searchStudent(request.getId().longValue());

    if (student == null) {
      throw new IllegalArgumentException("対象が存在しません");
    }

    if (request.getName() == null) {
      throw new NullPointerException("名前は必須です");
    }

    student.setName(request.getName());

    if (request.getAge() != null) {
      student.setAge(request.getAge());
    }
    if (request.getNameKana() != null) {
      student.setNameKana(request.getNameKana());
    }
    if (request.getNickname() != null) {
      student.setNickname(request.getNickname());
    }
    if (request.getMailaddress() != null) {
      student.setMailaddress(request.getMailaddress());
    }
    if (request.getLive() != null) {
      student.setLive(request.getLive());
    }
    if (request.getSex() != null) {
      student.setSex(request.getSex());
    }
    if (request.getRemark() != null) {
      student.setRemark(request.getRemark());
    }
    if (request.getIsDeleted() != null) {
      student.setDeleted(request.getIsDeleted());
    }

    studentRepository.updateStudent(student);

    if (request.getStudentCourseList() != null) {
      for (StudentCourseRequest courseRequest : request.getStudentCourseList()) {
        StudentCourse studentCourse = new StudentCourse();

        // DTO(Request)からEntity(StudentCourse)へ値を移し替える
        studentCourse.setIdBigint(courseRequest.getId().longValue());
        studentCourse.setCourseName(courseRequest.getCourseName());

        // ここでRepositoryのメソッドを呼び出す！
        studentCourseListRepository.updateStudentCourse(studentCourse);
      }
    }
  }

}