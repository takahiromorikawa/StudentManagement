package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.data.StudentsCourses;

@Mapper
  public interface StudentsCoursesRepository {

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> search();

    @Insert("INSERT INTO students_courses (students_ID, course_name, start, endplan) "
      + "VALUES (#{studentsId}, #{courseName}, #{start}, #{endplan})")
    void insert(StudentsCourses sc);

}







