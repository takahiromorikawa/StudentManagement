package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import raisetech.student.management.domain.StudentSearchCriteria;
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
    assertThat(actual).isNotEmpty();
    assertThat(actual).allMatch(student -> !student.isDeleted());
  }

  @Test
  void 受講生検索が行えること() {
    Student testStudent = new Student();
    testStudent.setName("検索用太郎");
    testStudent.setMailaddress("test-search@example.com");
    testStudent.setDeleted(false);
    sut.registerStudent(testStudent);

    Long id = testStudent.getId();

    Student actual = sut.searchStudent(id);

    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(id);
    assertThat(actual.getName()).isEqualTo("検索用太郎");
    assertThat(actual.getMailaddress()).isEqualTo("test-search@example.com");
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
    StudentSearchCriteria criteria = new StudentSearchCriteria();

    List<StudentSearchDetail> actual = sut.searchStudents(criteria);
    List<Student> allStudents = sut.search();

    assertThat(actual.size()).isEqualTo(allStudents.size());
  }

  @Test
  void 受講生の複合検索にて名前の部分一致で絞り込めること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    String searchName = "悟";
    criteria.setName(searchName);

    List<StudentSearchDetail> actual = sut.searchStudents(criteria);

    assertThat(actual).isNotEmpty();
    assertThat(actual).allMatch(detail -> detail.getName().contains(searchName));
  }

  @Test
  void 受講生の複合検索にて受講生IDとステータスのAND検索が行えること() {
    Student student = new Student();
    student.setName("AND検索太郎");
    student.setDeleted(false);
    sut.registerStudent(student);
    Long studentId = student.getId();

    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setId(studentId);

    List<StudentSearchDetail> actual = sut.searchStudents(criteria);

    assertThat(actual).isNotEmpty();
    assertThat(actual).extracting(StudentSearchDetail::getStudentId).contains(studentId);
  }

  @Test
  void 検索条件に空文字が渡された場合にその条件が無視されること() {
    StudentSearchCriteria criteria = new StudentSearchCriteria();
    criteria.setName("");

    List<StudentSearchDetail> actual = sut.searchStudents(criteria);

    List<StudentSearchDetail> allResults = sut.searchStudents(new StudentSearchCriteria());

    assertThat(actual.size()).isEqualTo(allResults.size());
    assertThat(actual).isNotEmpty();
  }

}