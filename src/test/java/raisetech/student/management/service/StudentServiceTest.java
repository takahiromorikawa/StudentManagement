package raisetech.student.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.RegisterStudentRequest;
import raisetech.student.management.controller.dto.StudentCourseRequest;
import raisetech.student.management.controller.dto.UpdateStudentRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentCourseMapper;
import raisetech.student.management.repository.StudentMapper;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StudentServiceTest {

  @Mock
  private StudentMapper studentMapper;

  @Mock
  private StudentCourseMapper studentCourseListRepository;

  @Mock
  private StudentConverter converter;

  @Mock
  private CourseStatusService courseStatusService;

  @InjectMocks
  private StudentService sut;

  @BeforeEach
  void setUp() {
    sut = new StudentService(
        studentMapper,
        studentCourseListRepository,
        converter,
        courseStatusService
    );

    // StudentのID疑似付与
    doAnswer(invocation -> {
      Student arg = invocation.getArgument(0);
      arg.setId(1L);
      return null;
    }).when(studentMapper).registerStudent(any());

    // StudentCourseのID疑似付与
    doAnswer(invocation -> {
      StudentCourse arg = invocation.getArgument(0);
      arg.setIdBigint(1L);
      return null;
    }).when(studentCourseListRepository).registerStudentCourse(any());
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(studentMapper.search()).thenReturn(studentList);
    when(studentCourseListRepository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(studentMapper, times(1)).search();
    verify(studentCourseListRepository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
    verify(courseStatusService, times(0)).findOptionalByStudentCourseId(any());
  }

  @Test
  void 受講生詳細検索_正常系_コースステータスが未登録の場合でも例外が発生せず取得できること() {

    Long studentId = 1L;
    Long courseId = 100L;

    Student student = new Student();
    student.setId(studentId);

    StudentCourse course = new StudentCourse();
    course.setIdBigint(courseId);
    List<StudentCourse> courseList = List.of(course);

    when(studentMapper.searchStudent(studentId)).thenReturn(student);
    when(studentCourseListRepository.searchStudentCourse(studentId)).thenReturn(courseList);

    when(courseStatusService.findOptionalByStudentCourseId(courseId)).thenReturn(Optional.empty());

    StudentDetail result = sut.searchStudent(studentId);

    assertThat(result.getStudent()).isEqualTo(student);
    assertThat(result.getStudentCourseList()).hasSize(1);
  }

  @Test
  void 受講生詳細検索_異常系_受講生が存在しない場合は例外() {
    Long id = 1L;
    when(studentMapper.searchStudent(id)).thenReturn(null);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.searchStudent(id));

    assertEquals("該当する受講生が存在しません", exception.getMessage());

    verify(studentMapper).searchStudent(id);
    verify(studentCourseListRepository, never()).searchStudentCourse(any());
  }

  @Test
  void 受講生登録_正常系_コースあり() {
    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");
    request.setAge(20);

    StudentCourseRequest courseRequest = new StudentCourseRequest();
    courseRequest.setCourseName("Java");
    List<StudentCourseRequest> courseRequests = new ArrayList<>();
    courseRequests.add(courseRequest);
    request.setStudentCourseList(courseRequests);

    StudentDetail result = sut.registerStudent(request);

    verify(courseStatusService, times(1))
        .register(any(), any());

    assertEquals("山田", result.getStudent().getName());
    assertEquals(1L, result.getStudent().getId());

    assertEquals(1, result.getStudentCourseList().size());
    assertEquals("Java", result.getStudentCourseList().get(0).getCourseName());
    assertEquals(1L, result.getStudentCourseList().get(0).getStudentsId());
  }

  @Test
  void 受講生登録_正常系_コースなし() {

    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");
    request.setStudentCourseList(null);

    StudentDetail result = sut.registerStudent(request);

    assertEquals(0, result.getStudentCourseList().size());

    verify(studentMapper).registerStudent(any());
    verify(studentCourseListRepository, never()).registerStudentCourse(any());
    verify(courseStatusService, never()).register(any(), any());
  }

  @Test
  void 受講生登録_異常系_コースが空リストの場合() {

    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");

    request.setStudentCourseList(new ArrayList<>()); // 空リスト

    StudentDetail result = sut.registerStudent(request);

    verify(studentMapper).registerStudent(any());

    verify(studentCourseListRepository, never())
        .registerStudentCourse(any());

    assertEquals(0, result.getStudentCourseList().size());

    verify(courseStatusService, never()).register(any(), any());
  }

  @Test
  void 受講生登録_正常系_コース名nullは無視されること() {

    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");

    StudentCourseRequest course = new StudentCourseRequest();
    course.setCourseName(null);

    request.setStudentCourseList(List.of(course));

    StudentDetail result = sut.registerStudent(request);

    assertEquals(0, result.getStudentCourseList().size());

    verify(courseStatusService, never()).register(any(), any());
  }

  @Test
  void 受講生登録_異常系_名前がnullの場合() {

    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName(null); // 不正

    assertThrows(NullPointerException.class, () -> {
      sut.registerStudent(request);
    });

  }

  @Test
  void 受講生更新_正常系_全項目更新されること() {
    UpdateStudentRequest request = new UpdateStudentRequest();
    request.setId(1);
    request.setName("山田");
    request.setAge(20);
    request.setNameKana("ヤマダ");
    request.setNickname("やまちゃん");
    request.setMailaddress("test@test.com");
    request.setLive("大阪");
    request.setSex("男");
    request.setRemark("備考");
    request.setIsDeleted(false);

    Student student = new Student();
    student.setId(1L);

    when(studentMapper.searchStudent(1L)).thenReturn(student);

    sut.updateStudent(request);

    verify(studentMapper).updateStudent(student);

    assertEquals("山田", student.getName());
    assertEquals(20, student.getAge());
    assertEquals("ヤマダ", student.getNameKana());
    assertEquals("やまちゃん", student.getNickname());
    assertEquals("test@test.com", student.getMailaddress());
    assertEquals("大阪", student.getLive());
    assertEquals("男", student.getSex());
    assertEquals("備考", student.getRemark());
    assertEquals(false, student.isDeleted());
  }

  @Test
  void 受講生更新_正常系_null項目は更新されないこと() {
    UpdateStudentRequest request = new UpdateStudentRequest();
    request.setId(1);
    request.setName("山田"); // nameだけ更新

    Student student = new Student();
    student.setId(1L);
    student.setAge(99); // 元の値

    when(studentMapper.searchStudent(1L)).thenReturn(student);

    sut.updateStudent(request);

    verify(studentMapper).updateStudent(student);

    assertEquals("山田", student.getName());

    // 変わってないことを確認
    assertEquals(99, student.getAge());
  }

  @Test
  void 受講生更新_異常系_存在しない場合例外() {
    UpdateStudentRequest request = new UpdateStudentRequest();
    request.setId(1);

    when(studentMapper.searchStudent(1L)).thenReturn(null);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.updateStudent(request));

    assertEquals("対象が存在しません", exception.getMessage());

    // updateは呼ばれない
    verify(studentMapper, never()).updateStudent(any());
  }

}


