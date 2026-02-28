package raisetech.student.management.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private LocalDateTime start;
  private LocalDateTime endplan;
  private Long studentsId;
  private String courseName;
  private Long idBigint;
}
