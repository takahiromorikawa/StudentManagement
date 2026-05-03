package raisetech.student.management.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStatusResponse {
  private Long id;
  private Long studentCourseId;
  private String courseStatus;

}
