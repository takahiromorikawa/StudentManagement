package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.domain.StudentSearchCriteria;
import raisetech.student.management.data.Student;
import raisetech.student.management.domain.StudentSearchDetail;

/**
 * 受講生テーブルのRepositoryです。(受講生テーブルと受講生コース情報テーブルと紐付きます。)
 */
@Mapper
public interface StudentMapper {

  /**
   * 受講生の全件検索を行います。
   *
   * @return　受講生一覧(全件)
   */
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  Student searchStudent(Long id);

  /**
   * 受講生を新規登録します。
   * IDに関しては自動採番を行う。
   *
   * @param student　受講生
   */
  void registerStudent(Student student);

  /**
   * 受講生を更新します。
   * @param student　受講生
   */
  void updateStudent(Student student);

  /**
   * 条件に合致する受講生情報を検索します。
   * 受講生情報、コース情報、申込状況をまとめて取得します。
   *
   * @param request 検索条件
   * @return 検索結果一覧
   */
  List<StudentSearchDetail> searchStudents(StudentSearchCriteria request);

}