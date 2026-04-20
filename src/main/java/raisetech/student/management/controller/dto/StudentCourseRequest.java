package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourseRequest {

  @NotNull
  private Integer id;

  @NotBlank
  private String courseName;

}
