package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.student.management.data.Student;

@Mapper
public interface StudentRepository {

  @Select("select * FROM students")
  List<Student> search();

  @Select("select * FROM students WHERE id = #{id}")
  Student searchStudent(Long id);

  @Insert("INSERT INTO students (name,age,name_kana,nickname,mailaddress,live,sex) " +
      "VALUES (#{name}, #{age}, #{nameKana}, #{nickname}, #{mailaddress}, #{live}, #{sex})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  @Update("""
      UPDATE students SET name = #{name}, age = #{age}, name_kana = #{nameKana}, nickname = #{nickname},
      mailaddress = #{mailaddress}, live = #{live}, sex = #{sex}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}
      """)
  void updateStudent(Student student);

}