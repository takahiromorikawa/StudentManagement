package raisetech.student.management.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.AssertionsKt.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.CourseStatus;

@MybatisTest
class CourseStatusMapperTest {

  @Autowired
  private CourseStatusMapper mapper;

  @Test
  void コースステータスが全件検索できること() {
    // 準備：2件のデータを登録
    CourseStatus status1 = new CourseStatus();
    status1.setStudentCourseId(1L);
    status1.setCourseStatus("受講中");
    mapper.insertCourseStatus(status1);

    CourseStatus status2 = new CourseStatus();
    status2.setStudentCourseId(2L);
    status2.setCourseStatus("仮登録");
    mapper.insertCourseStatus(status2);

    // 実行
    List<CourseStatus> result = mapper.findAll();

    // 検証
    // 1. リストのサイズが2であること
    assertEquals(2, result.size());
    // 2. 1件目のデータが正しく取得できていること
    assertEquals("受講中", result.get(0).getCourseStatus());
    // 3. 2件目のデータが正しく取得できていること
    assertEquals("仮登録", result.get(1).getCourseStatus());
  }

  @Test
  void 複数のデータがある中から特定のIDに紐づくステータスが取得できること() {
    // 準備：ID 1 と 2 を登録
    CourseStatus status1 = new CourseStatus();
    status1.setStudentCourseId(1L);
    status1.setCourseStatus("受講中");
    mapper.insertCourseStatus(status1);

    CourseStatus status2 = new CourseStatus();
    status2.setStudentCourseId(2L);
    status2.setCourseStatus("仮登録");
    mapper.insertCourseStatus(status2);

    // 実行：ID 2 を検索
    CourseStatus result = mapper.findByStudentCourseId(2L);

    // 検証：ID 2 のステータスが取れていること
    assertEquals("仮登録", result.getCourseStatus());
    assertEquals(2L, result.getStudentCourseId());
  }

  @Test
  void コースステータスが登録できること() {
    CourseStatus status = new CourseStatus();
    status.setStudentCourseId(1L);
    status.setCourseStatus("受講中");

    mapper.insertCourseStatus(status);

    CourseStatus result = mapper.findByStudentCourseId(1L);

    assertEquals("受講中", result.getCourseStatus());
  }

  @Test
  void コースステータスが更新できること() {
    // 準備：まず1件登録
    CourseStatus status = new CourseStatus();
    status.setStudentCourseId(1L);
    status.setCourseStatus("受講中");
    mapper.insertCourseStatus(status);

    // 実行：値を書き換えて更新
    status.setCourseStatus("卒業");
    mapper.updateCourseStatus(status);

    // 検証
    CourseStatus result = mapper.findByStudentCourseId(1L);
    assertEquals("卒業", result.getCourseStatus());
  }

  @Test
  void コースステータスが1件も登録されていない場合に空のリストが返ること() {
    // 何も登録せずに実行
    List<CourseStatus> result = mapper.findAll();

    // 検証
    assertEquals(0, result.size());
  }

  @Test
  void 存在しない受講生コースIDで検索した場合にnullが返ること() {
    // 存在しない適当なID(999など)で実行
    CourseStatus result = mapper.findByStudentCourseId(999L);

    // 検証
    assertNull(result);
  }

}