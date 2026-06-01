package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.controller.dto.CourseStatusRequest;
import raisetech.student.management.controller.dto.CourseStatusResponse;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.service.CourseStatusService;

/**
 * 受講生のコースステータスを管理するコントローラーです。 ステータスの登録、取得、更新などのAPIを提供します。
 */
@RestController
@RequestMapping("/course-status")
public class CourseStatusController {

  private final CourseStatusService service;
  private final StudentConverter converter;

  @Autowired
  public CourseStatusController(CourseStatusService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  /**
   * 受講生コースステータスの全件取得です。
   *
   * @return 受講生コースステータスのリスト
   */
  @Operation(summary = "申込み状況の一覧検索", description = "申込み状況の一覧を検索します。")
  @GetMapping
  public List<CourseStatusResponse> getAllStatuses() {
    List<CourseStatus> statuses = service.findAll();
    return converter.convertCourseStatusResponseList(statuses);
  }

  /**
   * 受講生コースステータスの取得です。 受講生コースIDに紐づく現在のステータス情報を取得します。
   *
   * @param studentCourseId 受講生コースID
   * @return 受講生コースステータス
   */
  @Operation(summary = "申込み状況の個別検索", description = "申込み状況の個別検索します。")
  @GetMapping("/{studentCourseId}")
  public CourseStatusResponse getStatus(@PathVariable Long studentCourseId) {
    CourseStatus status = service.findByStudentCourseId(studentCourseId);
    return converter.convertCourseStatusResponse(status);
  }

  /**
   * 受講生コースステータスの登録です。 指定された受講生コースIDに対してステータス情報を新規登録します。
   *
   * @param request ステータス登録情報
   */
  @Operation(summary = "申込み状況の登録", description = "申込み状況の登録をします。")
  @PostMapping
  public ResponseEntity<Void> register(@RequestBody @Valid CourseStatusRequest request) {
    service.register(
        request.getStudentCourseId(),
        request.getCourseStatus()
    );
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 受講生コースステータスの更新です。 既存の受講生コースIDのステータス情報を新しい情報で更新します。
   *
   * @param request ステータス更新情報
   */
  @Operation(summary = "申込み状況の更新", description = "申込み状況の更新をします。")
  @PutMapping("/{id}/status")
  public ResponseEntity<Void> update(
      @PathVariable Long id,
      @RequestBody @Valid CourseStatusRequest request) {

    service.updateStatus(
        id,
        request.getCourseStatus()
    );

    return ResponseEntity.noContent().build();
  }

}