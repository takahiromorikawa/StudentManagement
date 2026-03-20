package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourseRequest {

  @NotBlank
  private String courseName;
}
