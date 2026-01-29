package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.data.StudentsCourses;


@Mapper
  public interface StudentsCoursesRepository {

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> search();

    @Select("SELECT * FROM students_courses WHERE course_name = 'Javaコース'")
    List<StudentsCourses> searchInJava();

  }







