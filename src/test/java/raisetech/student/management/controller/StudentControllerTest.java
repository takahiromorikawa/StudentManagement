package raisetech.student.management.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.domain.StudentSearchCriteria;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.ResourceNotFoundException;
import raisetech.student.management.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

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

    when(service.registerStudent(any()))
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
        .andExpect(status().isCreated());

    verify(service, times(1)).registerStudent(any());
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
  void 存在しない受講生の場合は404が返ること() throws Exception {
    when(service.searchStudent(1L))
        .thenThrow(new ResourceNotFoundException("見つかりません"));

    mockMvc.perform(get("/student/1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("見つかりません"));
  }

  @Test
  void 不正なリクエストの場合は400が返ること() throws Exception {
    when(service.searchStudent(1L))
        .thenThrow(new IllegalArgumentException("不正"));

    mockMvc.perform(get("/student/1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("不正"));
  }

  @Test
  void 想定外エラーの場合は500が返ること() throws Exception {
    when(service.searchStudent(1L))
        .thenThrow(new RuntimeException());

    mockMvc.perform(get("/student/1"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error")
            .value("サーバーエラーが発生しました"));
  }

  @Test
  void 受講生詳細の受講生でIDに数字以外を用いた時に400エラーになること() throws Exception {

    mockMvc.perform(get("/student/abc"))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  void getStudents_検索リクエストが正常に処理され200が返ること() throws Exception {
    // 準備：Serviceが空のリストを返すように設定
    when(service.searchStudents(any(StudentSearchCriteria.class)))
        .thenReturn(Collections.emptyList());

    // 実行 & 検証
    mockMvc.perform(get("/students")
            .param("name", "五条")  // クエリパラメータを擬似的に設定
            .param("courseStatus", "受講中"))
        .andExpect(status().isOk()); // 200 OKが返るか
  }

}