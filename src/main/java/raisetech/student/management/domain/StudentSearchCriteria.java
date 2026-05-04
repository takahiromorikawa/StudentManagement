package raisetech.student.management.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 受講生検索の条件を保持するクラスです。
 */
@Getter
@Setter
public class StudentSearchCriteria {

  private String name;
  private String nameKana;
  private Long id;
  private Long studentCourseId;
  private String courseName;
  private String courseStatus;

}
