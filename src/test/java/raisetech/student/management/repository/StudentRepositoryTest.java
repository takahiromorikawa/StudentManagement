package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import raisetech.student.management.data.Student;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

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

}