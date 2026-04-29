package raisetech.student.management.data;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseStatus {

  private Long id;

  @NotNull
  private Long studentCourseId;

  @NotNull
  private String courseStatus;

}
