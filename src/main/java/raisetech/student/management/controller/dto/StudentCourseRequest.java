package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourseRequest {

  private Integer id;

  @NotBlank(message = "コース名は必須です")
  private String courseName;

}
