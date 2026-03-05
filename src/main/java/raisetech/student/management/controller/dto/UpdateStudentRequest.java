package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentRequest {

  @NotNull(message ="idは必須です")
  private Integer id;

  @NotBlank(message = "nameは必須です")
  private String name;

}
