package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;

/**
 * 受講生テーブルのRepositoryです。(受講生テーブルと受講生コース情報テーブルと紐付きます。)
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return　受講生一覧(全件)
   */
  @Select("select * FROM students")
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param id　受講生ID
   * @return　受講生
   */
  @Select("select * FROM students WHERE id = #{id}")
  Student searchStudent(Long id);

  /**
   * 受講生を新規登録します。
   * IDに関しては自動採番を行う。
   *
   * @param student　受講生
   */
  @Insert("INSERT INTO students (name,age,name_kana,nickname,mailaddress,live,sex) " +
      "VALUES (#{name}, #{age}, #{nameKana}, #{nickname}, #{mailaddress}, #{live}, #{sex})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  /**
   * 受講生を更新します。
   * @param student　受講生
   */
  @Update("""
      UPDATE students SET name = #{name}, age = #{age}, name_kana = #{nameKana}, nickname = #{nickname},
      mailaddress = #{mailaddress}, live = #{live}, sex = #{sex}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}
      """)
  void updateStudent(Student student);
}