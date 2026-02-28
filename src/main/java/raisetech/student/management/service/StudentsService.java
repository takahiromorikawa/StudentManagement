package raisetech.student.management.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.repository.StudentsCoursesRepository;

@Service
public class StudentsService {

  private final StudentRepository studentRepository;
  private final StudentsCoursesRepository studentsCoursesRepository;

  @Autowired
  public StudentsService(StudentRepository studentRepository,
      StudentsCoursesRepository studentsCoursesRepository) {
    this.studentRepository = studentRepository;
    this.studentsCoursesRepository = studentsCoursesRepository;
  }

  public List<Student> searchStudentList() {
    return studentRepository.search();
  }

  public StudentDetail searchStudent(Long id) {
    Student student = studentRepository.searchStudent(id);
    List<StudentsCourses> Courses = studentsCoursesRepository.searchStudentsCourses(student.getId());
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    detail.setStudentsCourses(Courses);
    return detail;
  }

  public List<StudentsCourses> searchStudentsCoursesList() {
    return studentsCoursesRepository.search();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
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
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    studentRepository.updateStudent(student);
    for (StudentsCourses sc : studentDetail.getStudentsCourses()) {
      studentsCoursesRepository.updateStudentsCourses(sc);
    }

  }
}