package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.UpdateStudentRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
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
  private final StudentsCoursesRepository studentsCoursesRepository;

  private StudentConverter converter;

  @Autowired
  public StudentsService(StudentRepository studentRepository,
      StudentsCoursesRepository studentsCoursesRepository ,StudentConverter converter) {
    this.studentRepository = studentRepository;
    this.studentsCoursesRepository = studentsCoursesRepository;

    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生一覧(全件)
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = studentRepository.search();
    List<StudentsCourses> studentsCoursesList = studentsCoursesRepository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(Long id) {
    Student student = studentRepository.searchStudent(id);
    List<StudentsCourses> studentsCourses = studentsCoursesRepository.searchStudentsCourses(student.getId());
    return new StudentDetail(student, studentsCourses);
  }

  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student s = studentDetail.getStudent();

    // ① students をINSERT（ここでDBがIDを採番）
    studentRepository.registerStudent(s);

    // ② ここで s.getId() に採番済みのIDが入っている
    Long generatedStudentId = s.getId();

    // ③ コースをINSERT（students_IDに採番IDを入れる）
    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses sc : studentDetail.getStudentsCourses()) {
        sc.setStudentsId(generatedStudentId);
        sc.setStart(LocalDateTime.now());
        sc.setEndplan(LocalDateTime.now().plusYears(1));
        studentsCoursesRepository.registerStudentsCourses(sc);
      }
    }
    return studentDetail;
  }

  @Transactional
  public void updateStudent(UpdateStudentRequest request) {
    Student student = studentRepository.searchStudent(request.getId().longValue());

    if (student == null) {
      throw new IllegalArgumentException("対象が存在しません");
    }

    student.setName(request.getName());
    studentRepository.updateStudent(student);
  }

}