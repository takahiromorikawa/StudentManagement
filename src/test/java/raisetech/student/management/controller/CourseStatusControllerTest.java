package raisetech.student.management.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.student.management.controller.dto.CourseStatusRequest;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.service.CourseStatusService;

@WebMvcTest(CourseStatusController.class)
class CourseStatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CourseStatusService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void ステータス全件取得ができること() throws Exception {
    // 準備
    CourseStatus status1 = new CourseStatus();
    status1.setStudentCourseId(1L);
    status1.setCourseStatus("TEMPORARY");

    CourseStatus status2 = new CourseStatus();
    status2.setStudentCourseId(2L);
    status2.setCourseStatus("FORMAL");

    when(service.findAll()).thenReturn(List.of(status1, status2));

    // 実行
    mockMvc.perform(get("/course-status"))
        .andExpect(status().isOk())
        // 配列の長さを検証
        .andExpect(jsonPath("$.length()").value(2))
        // 0番目の要素を検証
        .andExpect(jsonPath("$[0].studentCourseId").value(1))
        .andExpect(jsonPath("$[0].courseStatus").value("TEMPORARY"))
        // 1番目の要素を検証
        .andExpect(jsonPath("$[1].studentCourseId").value(2))
        .andExpect(jsonPath("$[1].courseStatus").value("FORMAL"));

    // 検証
    verify(service).findAll();
  }

  @Test
  void ステータス取得ができること() throws Exception {
    // 準備
    CourseStatus mockStatus = new CourseStatus();
    mockStatus.setStudentCourseId(1L);
    mockStatus.setCourseStatus("TEMPORARY");

    when(service.findByStudentCourseId(1L)).thenReturn(mockStatus);

    // 実行
    mockMvc.perform(get("/course-status/1"))
        .andExpect(status().isOk());

    // 検証
    verify(service).findByStudentCourseId(1L);
  }

  @Test
  void ステータス登録ができること() throws Exception {
    // 準備
    CourseStatusRequest request = new CourseStatusRequest();
    request.setStudentCourseId(1L);
    request.setCourseStatus("TEMPORARY");

    // 実行
    mockMvc.perform(post("/course-status")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    // 検証
    verify(service).register(1L, "TEMPORARY");
  }

  @Test
  void ステータス更新ができること() throws Exception {
    CourseStatusRequest request = new CourseStatusRequest();
    request.setStudentCourseId(1L);
    request.setCourseStatus("FORMAL");

    mockMvc.perform(put("/course-status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNoContent());

    verify(service).updateStatus(1L, "FORMAL");
  }

  @Test
  void 受講生コースIDが未入力の場合に400エラーが返ること() throws Exception {
    String invalidJson = """
        {
            "studentCourseId": null,
            "courseStatus": "受講中"
        }
        """;

    mockMvc.perform(post("/course-status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.studentCourseId").value("must not be null"));
  }

}