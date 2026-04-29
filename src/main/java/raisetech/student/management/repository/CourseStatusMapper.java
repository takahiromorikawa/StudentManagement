package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.student.management.data.CourseStatus;

/**
 * 受講生コースステータスに関するDB操作を行うリポジトリ（Mapper）です。 course_statusテーブルに対するCRUD操作を提供します。
 */
@Mapper
public interface CourseStatusMapper {

  /**
   * 受講生コースステータスの全件検索です。
   *
   * @return 全ての受講生コースステータスリスト
   */
  List<CourseStatus> findAll();

  /**
   * 受講生コースIDによるステータスの検索です。
   *
   * @param studentCourseId 受講生コースID
   * @return 該当する受講生コースステータス
   */
  CourseStatus findByStudentCourseId(Long studentCourseId);

  /**
   * 受講生コースステータスの新規登録です。
   *
   * @param courseStatus 登録するステータス情報
   */
  void insertCourseStatus(CourseStatus courseStatus);

  /**
   * 受講生コースステータスの更新です。 受講生コースIDを主キーとしてステータス情報を更新します。
   *
   * @param courseStatus 更新するステータス情報
   */
  void updateCourseStatus(CourseStatus courseStatus);

}