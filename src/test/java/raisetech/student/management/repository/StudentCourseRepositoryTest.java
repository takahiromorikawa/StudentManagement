package raisetech.student.management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import raisetech.student.management.data.StudentCourse;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentCourseRepositoryTest {

    @Autowired
    private StudentCourseRepository sut;

    @Test
    void 受講生コース情報の全件検索が行えること() {
        List<StudentCourse> actual = sut.searchStudentCourseList();
        assertThat(actual.size()).isEqualTo(3);
    }

    @Test
    void 受講生IDをもとに受講生コース情報の検索が行えること() {
        List<StudentCourse> actual = sut.searchStudentCourse(3L);

        assertThat(actual).isNotEmpty();
        StudentCourse firstCourse = actual.get(0);

        assertThat(firstCourse.getStudentsId()).isEqualTo(3L);
        assertThat(firstCourse.getCourseName()).isEqualTo("デザインコース");
    }

    @Test
    void 受講生コース情報の登録が行えること() {
        StudentCourse course = new StudentCourse();
        course.setStudentsId(3L);
        course.setCourseName("Javaコース");
        // 他の項目(start, endplan)はDBのデフォルト値が入る想定、または必要に応じてセット

        sut.registerStudentCourse(course);

        List<StudentCourse> actual = sut.searchStudentCourseList();
        assertThat(actual.size()).isEqualTo(4);
    }

    @Test
    void 受講生コース情報の更新が行えること() {
        List<StudentCourse> courses = sut.searchStudentCourseList();
        StudentCourse target = courses.get(0);

        String updateName = "最強の呪術コース";
        target.setCourseName(updateName);

        sut.updateStudentCourse(target);

        List<StudentCourse> actual = sut.searchStudentCourseList();
        // 0番目のデータのコース名が変わっていることを確認
        assertThat(actual.get(0).getCourseName()).isEqualTo(updateName);
    }

}
