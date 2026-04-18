package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private String name;
  private Integer age;
  private String nameKana;
  private String nickname;
  private String mailaddress;
  private String live;
  private String sex;
  @NotNull
  private Long id;
  private String remark;
  private boolean isDeleted;
}
