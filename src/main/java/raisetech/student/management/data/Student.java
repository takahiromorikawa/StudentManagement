package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private String name;
  private int age;
  private String nameKana;
  private String nickname;
  private String mailaddress;
  private String live;
  private String sex;
  private Long id;
  private String remark;
  private boolean isDeleted;
}
