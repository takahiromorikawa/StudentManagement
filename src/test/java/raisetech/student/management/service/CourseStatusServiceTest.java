package raisetech.student.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.repository.CourseStatusMapper;

class CourseStatusServiceTest {

  private CourseStatusService sut;
  private CourseStatusMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mockito.mock(CourseStatusMapper.class);
    sut = new CourseStatusService(mapper);
  }

  @Test
  void 受講生コースステータスの全件検索ができること() {
    // 準備
    CourseStatus status1 = new CourseStatus();
    status1.setStudentCourseId(1L);
    CourseStatus status2 = new CourseStatus();
    status2.setStudentCourseId(2L);
    List<CourseStatus> mockList = List.of(status1, status2);

    when(mapper.findAll()).thenReturn(mockList);

    // 実行
    List<CourseStatus> result = sut.findAll();

    // 検証
    assertEquals(2, result.size());
    verify(mapper).findAll();
  }

  @Test
  void コースステータスが取得できること() {
    // 準備
    CourseStatus mockStatus = new CourseStatus();
    mockStatus.setStudentCourseId(1L);
    mockStatus.setCourseStatus("TEMPORARY");

    when(mapper.findByStudentCourseId(1L)).thenReturn(mockStatus);

    // 実行
    CourseStatus result = sut.findByStudentCourseId(1L);

    // 検証
    assertThat(result).isNotNull();
    assertThat(result.getCourseStatus()).isEqualTo("TEMPORARY");

    verify(mapper).findByStudentCourseId(1L);
  }

  @Test
  void コースステータスが登録できること() {
    // 実行
    sut.register(1L, "TEMPORARY");

    // 検証（Mapperが呼ばれているか）
    verify(mapper).insertCourseStatus(any());
  }

  @Test
  void ステータスが更新できること() {
    // ===== 準備 =====
    CourseStatus existing = new CourseStatus();
    existing.setStudentCourseId(1L);
    existing.setCourseStatus("TEMPORARY");

    when(mapper.findByStudentCourseId(1L)).thenReturn(existing);

    // ===== 実行 =====
    sut.updateStatus(1L, "FORMAL");

    // ===== 検証 =====
    assertEquals("FORMAL", existing.getCourseStatus());

    verify(mapper, times(1)).updateCourseStatus(existing);
  }

  @Test
  void ステータスが存在しない場合は例外が発生すること() {
    // ===== 準備 =====
    when(mapper.findByStudentCourseId(1L)).thenReturn(null);

    // ===== 実行 & 検証 =====
    assertThrows(ResourceNotFoundException.class, () -> {
      sut.updateStatus(1L, "FORMAL");
    });

    verify(mapper, never()).updateCourseStatus(any());
  }

}

