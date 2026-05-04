package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import raisetech.student.management.controller.dto.StudentSearchRequest;
import raisetech.student.management.data.Student;
import raisetech.student.management.domain.StudentSearchDetail;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentMapperTest {

  @Autowired
  private StudentMapper sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 受講生検索が行えること() {
    Long id = 3L;

    Student actual = sut.searchStudent(id);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(id);
    assertThat(actual.getName()).isEqualTo("五条悟");
    assertThat(actual.getMailaddress()).isEqualTo("gojyou@example.com");
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setName("五条悟");
    student.setNameKana("ゴジョウサトル");
    student.setNickname("さとる");
    student.setMailaddress("gojyou@example.com");
    student.setLive("山梨");
    student.setAge(25);
    student.setSex("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生の更新が行えること() {
    List<Student> students = sut.search();
    Student targetStudent = students.get(0);

    String newNickname = "最強の呪術師";
    targetStudent.setNickname(newNickname);
    targetStudent.setLive("東京都");

    sut.updateStudent(targetStudent);

    List<Student> actual = sut.search();

    assertThat(actual.get(0).getNickname()).isEqualTo(newNickname);
    assertThat(actual.get(0).getLive()).isEqualTo("東京都");
  }

  //異常系テスト
  @Test
  void 存在しない受講生IDで検索した際にnullが返ること() {
    // data.sqlに存在しないID（例: 999）で検索
    Student actual = sut.searchStudent(999L);

    assertThat(actual).isNull();
  }

  @Test
  void 名前がnullの状態で受講生を登録しようとすると例外が発生すること() {
    Student student = new Student();
    student.setName(null);

    // MyBatis/H2の制約違反（NOT NULL制約）で例外が発生することを検証
    assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
      sut.registerStudent(student);
    });
  }

  // --- 新規追加した検索機能 (searchStudents) のテスト ---

  @Test
  void 受講生の複合検索にて条件なしの場合に全件取得できること() {
    StudentSearchRequest request = new StudentSearchRequest();
    // 条件を何もセットしない

    List<StudentSearchDetail> actual = sut.searchStudents(request);

    // 現在のdata.sqlの全件（3件）が取得できているか
    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 受講生の複合検索にて名前の部分一致で絞り込めること() {
    StudentSearchRequest request = new StudentSearchRequest();
    request.setName("悟"); // 「五条悟」がヒットするはず

    List<StudentSearchDetail> actual = sut.searchStudents(request);

    assertThat(actual).hasSize(1);
    assertThat(actual.get(0).getName()).isEqualTo("五条悟");
  }

  @Test
  void 受講生の複合検索にて受講生IDとステータスのAND検索が行えること() {
    StudentSearchRequest request = new StudentSearchRequest();
    request.setId(3L);
    request.setCourseStatus("受講中"); // data.sqlで五条悟が「受講中」である想定

    List<StudentSearchDetail> actual = sut.searchStudents(request);

    // 条件に合致する場合のみ取得される
    assertThat(actual).allMatch(detail ->
        detail.getStudentId().equals(3L) && "受講中".equals(detail.getCourseStatus())
    );
  }

  @Test
  void 検索条件に空文字が渡された場合にその条件が無視されること() {
    StudentSearchRequest request = new StudentSearchRequest();
    request.setName(""); // 空文字をセット

    List<StudentSearchDetail> actual = sut.searchStudents(request);

    // 空文字条件が無視され、結果的に全件検索（3件）になること
    assertThat(actual.size()).isEqualTo(3);
  }

}