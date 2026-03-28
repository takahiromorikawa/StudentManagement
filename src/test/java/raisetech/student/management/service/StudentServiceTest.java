package raisetech.student.management.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.RegisterStudentRequest;
import raisetech.student.management.controller.dto.StudentCourseRequest;
import raisetech.student.management.controller.dto.UpdateStudentRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentCourseRepository;
import raisetech.student.management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository studentRepository;

  @Mock
  private StudentCourseRepository studentCourseListRepository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(studentRepository, studentCourseListRepository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    //事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(studentRepository.search()).thenReturn(studentList);
    when(studentCourseListRepository.searchStudentCourseList()).thenReturn(studentCourseList);

    //実行
    sut.searchStudentList();

    //検証
    verify(studentRepository,times(1)).search();
    verify(studentCourseListRepository,times(1)).searchStudentCourseList();
    verify(converter,times(1)).convertStudentDetails(studentList,studentCourseList);
  }

  @Test
  void 受講生詳細検索_正常系_正しく取得できること() {
    // 事前準備
    Long id = 1L;
    Student student = new Student();
    student.setId(id);

    List<StudentCourse> courseList = new ArrayList<>();

    when(studentRepository.searchStudent(id)).thenReturn(student);
    when(studentCourseListRepository.searchStudentCourse(id)).thenReturn(courseList);

    // 実行
    StudentDetail result = sut.searchStudent(id);

    // 検証（呼び出し）
    verify(studentRepository).searchStudent(id);
    verify(studentCourseListRepository).searchStudentCourse(id);

    // 検証（結果）
    assertEquals(student, result.getStudent());
    assertEquals(courseList, result.getStudentCourseList());
  }

  @Test
  void 受講生詳細検索_異常系_受講生が存在しない場合は例外() {
    // 事前準備
    Long id = 1L;
    when(studentRepository.searchStudent(id)).thenReturn(null);

    // 実行 & 検証
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.searchStudent(id));

    assertEquals("該当する受講生が存在しません", exception.getMessage());

    // 呼び出し確認
    verify(studentRepository).searchStudent(id);
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

      doAnswer(invocation -> {
        Student arg = invocation.getArgument(0);
        arg.setId(1L); // DBがID付与した想定
        return null;
      }).when(studentRepository).registerStudent(any());

      StudentDetail result = sut.registerStudent(request);

      // student登録確認
      verify(studentRepository).registerStudent(any());

      // course登録確認
      verify(studentCourseListRepository, times(1))
          .registerStudentCourse(any());

      // 戻り値確認
      assertEquals("山田", result.getStudent().getName());
      assertEquals(1, result.getStudentCourseList().size());
      assertEquals("Java", result.getStudentCourseList().get(0).getCourseName());
  }
  @Test
  void 受講生登録_正常系_コースなし() {
    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");

    request.setStudentCourseList(null);

    doAnswer(invocation -> {
      Student arg = invocation.getArgument(0);
      arg.setId(1L);
      return null;
    }).when(studentRepository).registerStudent(any());

    StudentDetail result = sut.registerStudent(request);

    verify(studentRepository).registerStudent(any());

    // コースは呼ばれない
    verify(studentCourseListRepository, never())
        .registerStudentCourse(any());

    assertEquals(0, result.getStudentCourseList().size());
  }

  @Test
  void 受講生登録_異常系_コースが空リストの場合() {
    // 事前準備
    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");

    request.setStudentCourseList(new ArrayList<>()); // 空リスト

    doAnswer(invocation -> {
      Student arg = invocation.getArgument(0);
      arg.setId(1L);
      return null;
    }).when(studentRepository).registerStudent(any());

    // 実行
    StudentDetail result = sut.registerStudent(request);

    // 検証
    verify(studentRepository).registerStudent(any());

    // コースは登録されない
    verify(studentCourseListRepository, never())
        .registerStudentCourse(any());

    // 戻り値も空
    assertEquals(0, result.getStudentCourseList().size());
  }

  @Test
  void 受講生登録_異常系_コース名がnullでも登録されてしまうこと() {
    // 事前準備
    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName("山田");

    StudentCourseRequest courseRequest = new StudentCourseRequest();
    courseRequest.setCourseName(null); // 不正

    List<StudentCourseRequest> list = new ArrayList<>();
    list.add(courseRequest);
    request.setStudentCourseList(list);

    doAnswer(invocation -> {
      Student arg = invocation.getArgument(0);
      arg.setId(1L);
      return null;
    }).when(studentRepository).registerStudent(any());

    // 実行
    StudentDetail result = sut.registerStudent(request);

    // 検証
    verify(studentCourseListRepository).registerStudentCourse(any());

    // nullのまま登録されてしまう（現状仕様）
    assertEquals(null, result.getStudentCourseList().get(0).getCourseName());
  }

  @Test
  void 受講生登録_異常系_名前がnullの場合() {
    // 事前準備
    RegisterStudentRequest request = new RegisterStudentRequest();
    request.setName(null); // 不正

    doAnswer(invocation -> {
      Student arg = invocation.getArgument(0);
      arg.setId(1L);
      return null;
    }).when(studentRepository).registerStudent(any());

    // 実行
    StudentDetail result = sut.registerStudent(request);

    // 検証
    verify(studentRepository).registerStudent(any());

    // 名前がnullのまま登録される（現状）
    assertEquals(null, result.getStudent().getName());
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

    when(studentRepository.searchStudent(1L)).thenReturn(student);

    sut.updateStudent(request);

    verify(studentRepository).updateStudent(student);

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

    when(studentRepository.searchStudent(1L)).thenReturn(student);

    sut.updateStudent(request);

    verify(studentRepository).updateStudent(student);

    assertEquals("山田", student.getName());

    // 変わってないことを確認
    assertEquals(99, student.getAge());
  }

  @Test
  void 受講生更新_異常系_存在しない場合例外() {
    UpdateStudentRequest request = new UpdateStudentRequest();
    request.setId(1);

    when(studentRepository.searchStudent(1L)).thenReturn(null);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> sut.updateStudent(request));

    assertEquals("対象が存在しません", exception.getMessage());

    // updateは呼ばれない
    verify(studentRepository, never()).updateStudent(any());
  }

}


