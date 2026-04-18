package raisetech.student.management.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {

    when(service.searchStudentList()).thenReturn(List.of());

    mockMvc.perform(get("/studentList"))
        .andExpect(status().isOk());

    verify (service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の取得ができること() throws Exception {
    mockMvc.perform(get("/student/1"))
        .andExpect(status().isOk());
  }

  @Test
  void 受講生登録ができること() throws Exception {

    when(service.registerStudent(org.mockito.ArgumentMatchers.any()))
        .thenReturn(new StudentDetail());

    mockMvc.perform(post("/registerStudent")
            .contentType("application/json")
            .content("""
        {
          "name": "テスト",
          "age": 20,
          "nameKana": "テスト",
          "nickname": "テスト",
          "mailaddress": "test@example.com",
          "live": "奈良県",
          "sex": "男性"
        }
      """))
        .andExpect(status().isOk());
  }

  @Test
  void 受講生更新ができること() throws Exception {
    mockMvc.perform(put("/updateStudent")
            .contentType("application/json")
            .content("""
          {
            "id": 1,
            "name": "更新"
          }
      """))
        .andExpect(status().isNoContent());
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に400エラーになること() throws Exception {

    mockMvc.perform(get("/student/abc"))
        .andDo(print())
        .andExpect(status().isBadRequest());
    }

}