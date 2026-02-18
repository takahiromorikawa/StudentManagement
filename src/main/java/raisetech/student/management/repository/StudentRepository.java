package raisetech.student.management.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.student.management.data.Student;

@Mapper
public interface StudentRepository {

  @Select("select * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students WHERE age BETWEEN 30 AND 39")
  List<Student> searchIn30s();

  @Insert("INSERT INTO students (name,age,name_kana,nickname,mailaddress,live,sex,ID) " +
  "VALUES (#{name}, #{age}, #{nameKana}, #{nickname}, #{mailaddress}, #{live}, #{sex},#{id})")
  void insertStudent(Student student);

}