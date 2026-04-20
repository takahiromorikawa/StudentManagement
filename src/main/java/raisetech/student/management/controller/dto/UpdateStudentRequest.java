package raisetech.student.management.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UpdateStudentRequest {

  @NotNull(message ="idは必須です")
  private Integer id;

  @NotNull(message ="nameは必須です")
  private String name;

  private Integer age;
  private String nameKana;
  private String nickname;
  private String mailaddress;
  private String live;
  private String sex;
  private String remark;
  private Boolean isDeleted;

  private List<StudentCourseRequest> studentCourseList;

}
