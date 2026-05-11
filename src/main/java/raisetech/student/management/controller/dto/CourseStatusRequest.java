package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CourseStatusRequest {

  private Long id;

  @NotNull
  private Long studentCourseId;

  @NotBlank(message = "申込状況は必須です")
  private String courseStatus;

}
