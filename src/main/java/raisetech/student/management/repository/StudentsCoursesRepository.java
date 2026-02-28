package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentsCourses;

@Mapper
  public interface StudentsCoursesRepository {

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> search();

  @Select("select * FROM students_courses")
    List<StudentsCourses> searchStudentsCoursesList();

  @Select("select * FROM students_courses WHERE students_ID = #{studentId}")
  List<StudentsCourses> searchStudentsCourses(Long studentId);

  @Insert("""
    INSERT INTO students_courses (students_ID, course_name, start, endplan) 
    VALUES(#{studentsId}, #{courseName}, #{start}, #{endplan})
    """)
    void registerStudentsCourses(StudentsCourses studentsCourses);

  @Update("""
     UPDATE students_courses SET course_name = #{courseName} WHERE id_bigint = #{idBigint}
     """)
     void updateStudentsCourses(StudentsCourses studentsCourses);

}







