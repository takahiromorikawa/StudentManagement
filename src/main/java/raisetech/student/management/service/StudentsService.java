package raisetech.student.management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.repository.StudentsCoursesRepository;
import java.util.UUID;

@Service
public class StudentsService {

  private final StudentRepository studentRepository;
  private final StudentsCoursesRepository studentsCoursesRepository;

  @Autowired
  public StudentsService(
      StudentRepository studentRepository,
      StudentsCoursesRepository studentsCoursesRepository) {
    this.studentRepository = studentRepository;
    this.studentsCoursesRepository = studentsCoursesRepository;
  }

  //検索処理
  public List<Student> searchStudentList() {
    return studentRepository.search();
  }

  public List<StudentsCourses> searchStudentsCoursesList() {
    return studentsCoursesRepository.search();
  }

  public void registerStudent(StudentDetail studentDetail) {
    Student s = studentDetail.getStudent();

    // IDが空なら採番（画面で入力しなくていい）
    if (s.getId() == null) {
      s.setId(UUID.randomUUID().toString());
    }

    // students に保存
    studentRepository.insertStudent(s);

    // コースも保存
    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses sc : studentDetail.getStudentsCourses()) {
        sc.setStudentsId(s.getId());   // ★DBは students_ID
        studentsCoursesRepository.insert(sc);
      }
    }
  }
}

