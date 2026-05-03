package raisetech.student.management.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.repository.CourseStatusMapper;

/**
 * 受講生コースステータスの管理を行うサービスです。 DBへの登録・検索・更新処理のビジネスロジックを担当します。
 */
@Service
public class CourseStatusService {

  private final CourseStatusMapper courseStatusMapper;

  @Autowired
  public CourseStatusService(CourseStatusMapper courseStatusMapper) {
    this.courseStatusMapper = courseStatusMapper;
  }

  /**
   * 受講生コースステータスの全件検索です。
   *
   * @return 全ての受講生コースステータスリスト
   */
  public List<CourseStatus> findAll() {
    return courseStatusMapper.findAll();
  }

  /**
   * 受講生コースステータスをDBから取得します。
   *
   * @param studentCourseId 受講生コースID
   * @return 受講生コースステータス（存在しない場合はnull）
   */
  private CourseStatus findInternal(Long studentCourseId) {
    return courseStatusMapper.findByStudentCourseId(studentCourseId);
  }
  /**
   * 受講生コースステータスの検索です。
   * 存在しない場合は例外をスローします。
   */
  public CourseStatus findByStudentCourseId(Long studentCourseId) {
    CourseStatus status = findInternal(studentCourseId);
    if (status == null) {
      throw new ResourceNotFoundException(
          "受講生コースID:" + studentCourseId + " のステータスが見つかりません。"
      );
    }
    return status;
  }
  /**
   * 受講生コースステータスの検索です。
   * 存在しない場合はOptional.empty()を返します。
   * 一覧取得など、未登録を許容する場合に使用します。
   */
  public Optional<CourseStatus> findOptionalByStudentCourseId(Long studentCourseId) {
    return Optional.ofNullable(findInternal(studentCourseId));
  }

  /**
   * 受講生コースステータスの新規登録です。 指定された受講生コースIDとステータス情報をDBに保存します。
   *
   * @param studentCourseId 受講生コースID
   * @param status          設定するステータス文字列
   */
  public void register(Long studentCourseId, String status) {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setStudentCourseId(studentCourseId);
    courseStatus.setCourseStatus(status);

    courseStatusMapper.insertCourseStatus(courseStatus);
  }

  /**
   * 受講生コースステータスの更新です。 既存のステータス情報を新しい値に変更します。
   *
   * @param studentCourseId 受講生コースID
   * @param status          更新後のステータス文字列
   * @throws ResourceNotFoundException 指定されたIDのステータスが存在しない場合にスローされます
   */
  public void updateStatus(Long studentCourseId, String status) {
    CourseStatus courseStatus = findByStudentCourseId(studentCourseId);

    courseStatus.setCourseStatus(status);

    courseStatusMapper.updateCourseStatus(courseStatus);
  }

}