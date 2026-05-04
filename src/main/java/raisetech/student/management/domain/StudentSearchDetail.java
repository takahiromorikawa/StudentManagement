package raisetech.student.management.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 検索結果として返却する受講生の統合情報です。
 */
@Getter
@Setter
public class StudentSearchDetail {

  // 受講生情報
  private Long studentId;
  private String name;
  private String nameKana;

  // コース情報
  private Long studentCourseId;
  private String courseName;

  // 申込状況
  private String courseStatus;

}
