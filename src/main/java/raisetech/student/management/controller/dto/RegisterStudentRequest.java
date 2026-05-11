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

  @NotBlank(message = "氏名は必須です")
  private String name;

  @Min(value = 0, message = "0 以上の値にしてください")
  @NotNull
  private Integer age;

  @NotBlank(message = "カナ名は必須です")
  private String nameKana;

  private String nickname;

  @Email(message = "メールアドレス形式で入力してください")
  @NotBlank(message = "メールアドレスは必須です")
  private String mailaddress;

  @NotBlank(message = "居住地は必須です")
  private String live;

  @NotBlank(message = "申込状況は必須です")
  private String courseStatus;

  private String sex;

  private String remark;
  private Boolean isDeleted;

  @Valid
  private List<StudentCourseRequest> studentCourseList;

}
