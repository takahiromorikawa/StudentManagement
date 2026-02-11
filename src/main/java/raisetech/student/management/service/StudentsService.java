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

  //絞り込みをする。年齢が30代の人のみを抽出する。
  //抽出したリストをControllerに返す。
  //public List<Student> searchStudentsIn30s() {
    //return studentRepository.searchIn30s();}

  public List<StudentsCourses> searchStudentsCoursesList() {
    return studentsCoursesRepository.search();
  }

  //絞り込み検索で「Javaコース」のコース情報のみを抽出する。
  //抽出したリストをControllerに返す。
//public List<StudentsCourses> searchInJava() {
    //return studentsCoursesRepository.searchInJava();}



public void registerStudent(StudentDetail studentDetail) {
  Student s = studentDetail.getStudent();

  // IDが空なら採番（画面で入力しなくていい）
  if (s.getId() == null) {
    s.setId(UUID.randomUUID().toString());
  }

  studentRepository.insertStudent(s);

}




}
