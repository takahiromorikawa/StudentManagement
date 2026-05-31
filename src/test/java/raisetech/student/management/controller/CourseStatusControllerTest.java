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
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.CourseStatusRequest;
import raisetech.student.management.controller.dto.CourseStatusResponse;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.service.CourseStatusService;

@WebMvcTest(CourseStatusController.class)
class CourseStatusControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CourseStatusService service;

  @MockitoBean
  private StudentConverter converter;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void ステータス全件取得ができること() throws Exception {
    CourseStatus status1 = new CourseStatus();
    status1.setStudentCourseId(1L);
    status1.setCourseStatus("TEMPORARY");
    List<CourseStatus> statuses = List.of(status1);

    CourseStatusResponse response = new CourseStatusResponse();
    response.setStudentCourseId(1L);
    response.setCourseStatus("TEMPORARY");

    when(service.findAll()).thenReturn(statuses);
    when(converter.convertCourseStatusResponseList(statuses)).thenReturn(List.of(response));

    mockMvc.perform(get("/course-status"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].studentCourseId").value(1))
        .andExpect(jsonPath("$[0].courseStatus").value("TEMPORARY"));

    verify(service).findAll();
  }

  @Test
  void ステータス取得ができること() throws Exception {
    CourseStatus mockStatus = new CourseStatus();
    CourseStatusResponse response = new CourseStatusResponse();
    response.setStudentCourseId(1L);
    response.setCourseStatus("TEMPORARY");

    when(service.findByStudentCourseId(1L)).thenReturn(mockStatus);
    when(converter.convertCourseStatusResponse(mockStatus)).thenReturn(response);

    mockMvc.perform(get("/course-status/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.studentCourseId").value(1))
        .andExpect(jsonPath("$.courseStatus").value("TEMPORARY"));

    verify(service).findByStudentCourseId(1L);
  }

  @Test
  void ステータス登録ができること() throws Exception {
    CourseStatusRequest request = new CourseStatusRequest();
    request.setStudentCourseId(1L);
    request.setCourseStatus("TEMPORARY");

    mockMvc.perform(post("/course-status")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());

    verify(service).register(1L, "TEMPORARY");
  }

  @Test
  void ステータス更新ができること() throws Exception {
    CourseStatusRequest request = new CourseStatusRequest();
    request.setStudentCourseId(1L);
    request.setCourseStatus("FORMAL");

    mockMvc.perform(put("/course-status/1/status")
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