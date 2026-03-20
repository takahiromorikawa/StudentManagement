package raisetech.student.management.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStudentRequest {

  private Integer id;

  @NotBlank(message = "nameは必須です")
  private String name;

  @Min(0)
  @NotNull(message = "ageは必須です")
  private Integer age;

  @NotBlank(message = "nameKanaは必須です")
  private String nameKana;

  @NotBlank(message = "nicknameは必須です")
  private String nickname;

  @Email
  @NotBlank(message = "mailaddressは必須です")
  private String mailaddress;

  @NotBlank(message = "liveは必須です")
  private String live;

  @NotBlank(message = "sexは必須です")
  private String sex;

  private String remark;
  private Boolean isDeleted;

  @Valid
  private List<StudentCourseRequest> studentCourseList;

}
