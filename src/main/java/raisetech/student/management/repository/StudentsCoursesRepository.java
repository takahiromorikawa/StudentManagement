package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.StudentCourse;

/**
 * 受講生コース情報テーブルのRepositoryです。(受講生テーブルと受講生コース情報テーブルと紐付きます。)
 */
@Mapper
  public interface StudentsCoursesRepository {

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return　受講生のコース情報(全件)
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDをもとに受講生のコース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生コース情報一覧
   */
  List<StudentCourse> searchStudentCourse(Long studentId);

  /**
   * 受講生コース情報を新規登録します。
   * IDに関しては自動採番を行う。
   *
   * @param studentCourse　受講生コース情報
   */
    void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生コース情報のコース名を更新します。
   * @param studentCourse　受講生コース情報
   */
     void updateStudentCourse(StudentCourse studentCourse);
}







