package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.UpdateStudentRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.repository.StudentsCoursesRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentsService {

  private final StudentRepository studentRepository;
  private final StudentsCoursesRepository studentCourseListRepository;

  private StudentConverter converter;

  @Autowired
  public StudentsService(StudentRepository studentRepository,
      StudentsCoursesRepository studentCourseListRepository,StudentConverter converter) {
    this.studentRepository = studentRepository;
    this.studentCourseListRepository = studentCourseListRepository;

    this.converter = converter;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = studentRepository.search();
    List<StudentCourse> studentCourseList = studentCourseListRepository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生詳細
   */
  public StudentDetail searchStudent(Long id) {
    Student student = studentRepository.searchStudent(id);
    List<StudentCourse> studentCourse = studentCourseListRepository.searchStudentCourse(student.getId());
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値と、コース開始日、コース終了日を設定します。
   *
   * @param studentDetail　受講生詳細
   * @return　登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student s = studentDetail.getStudent();

    // ① students をINSERT（ここでDBがIDを採番）
    studentRepository.registerStudent(s);
    // ② ここで s.getId() に採番済みのIDが入っている
    Long generatedStudentId = s.getId();

    //System.out.println("Generated ID = " + generatedStudentId);

    // ③ コースをINSERT（students_IDに採番IDを入れる）
    if (studentDetail.getStudentCourseList() != null) {
      for (StudentCourse sc : studentDetail.getStudentCourseList()) {
        initStudentsCourse(sc, generatedStudentId);
        studentCourseListRepository.registerStudentCourse(sc);
      }
    }

    return studentDetail;
  }

  /**
   *受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param sc　受講生コース情報
   * @param generatedStudentId　受講生
   */
  private void initStudentsCourse(StudentCourse sc, Long generatedStudentId) {
    LocalDateTime now = LocalDateTime.now();

    sc.setStudentsId(generatedStudentId);
    sc.setStart(now);
    sc.setEndplan(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生と受講生コース情報をそれぞれ更新します。
   * @param request　受講生詳細
   */
  @Transactional
  public void updateStudent(UpdateStudentRequest request) {
    Student student = studentRepository.searchStudent(request.getId().longValue());

    if (student == null) {
      throw new IllegalArgumentException("対象が存在しません");
    }

    student.setName(request.getName());
    student.setAge(request.getAge());
    student.setNameKana(request.getNameKana());
    student.setNickname(request.getNickname());
    student.setMailaddress(request.getMailaddress());
    student.setLive(request.getLive());
    student.setSex(request.getSex());
    student.setRemark(request.getRemark());
    student.setDeleted(request.getIsDeleted());
    studentRepository.updateStudent(student);
  }

}