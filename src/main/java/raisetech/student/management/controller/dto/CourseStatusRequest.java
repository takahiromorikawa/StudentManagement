package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CourseStatusRequest {

  private Long id;

  @NotNull
  private Long studentCourseId;

  @NotNull
  private String courseStatus;

}
