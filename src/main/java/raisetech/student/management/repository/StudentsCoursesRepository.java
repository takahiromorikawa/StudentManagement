package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生コース情報テーブルのRepositoryです。(受講生テーブルと受講生コース情報テーブルと紐付きます。)
 */
@Mapper
  public interface StudentsCoursesRepository {

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> search();

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報(全件)
   */
  @Select("select * FROM students_courses")
    List<StudentCourse> searchStudentCourseList();

  @Select("""
  SELECT
    id_bigint   AS idBigint,
    students_ID AS studentsId,
    course_name AS courseName,
    start,
    endplan
  FROM students_courses
  WHERE students_ID = #{studentId}
""")
  List<StudentCourse> searchStudentCourse(Long studentId);

  /**
   * 受講生コース情報を新規登録します。
   * IDに関しては自動採番を行う。
   *
   * @param studentCourse　受講生コース情報
   */
  @Insert("""
    INSERT INTO students_courses (students_ID, course_name, start, endplan) 
    VALUES(#{studentsId}, #{courseName}, #{start}, #{endplan})
    """)
    void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生コース情報のコース名を更新します。
   * @param studentCourse　受講生コース情報
   */
  @Update("""
     UPDATE students_courses SET course_name = #{courseName} WHERE id_bigint = #{idBigint}
     """)
     void updateStudentCourse(StudentCourse studentCourse);
}







