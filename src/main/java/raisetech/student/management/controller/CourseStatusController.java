package raisetech.student.management.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import raisetech.student.management.controller.dto.CourseStatusRequest;
import raisetech.student.management.data.CourseStatus;
import raisetech.student.management.service.CourseStatusService;

/**
 * 受講生のコースステータスを管理するコントローラーです。 ステータスの登録、取得、更新などのAPIを提供します。
 */
@RestController
@RequestMapping("/course-status")
public class CourseStatusController {

  private final CourseStatusService service;

  @Autowired
  public CourseStatusController(CourseStatusService service) {
    this.service = service;
  }

  /**
   * 受講生コースステータスの全件取得です。
   *
   * @return 受講生コースステータスのリスト
   */
  @GetMapping
  public List<CourseStatus> getAllStatuses() {
    return service.findAll();
  }

  /**
   * 受講生コースステータスの取得です。 受講生コースIDに紐づく現在のステータス情報を取得します。
   *
   * @param studentCourseId 受講生コースID
   * @return 受講生コースステータス
   */
  @GetMapping("/{studentCourseId}")
  public CourseStatus getStatus(@PathVariable Long studentCourseId) {
    return service.findByStudentCourseId(studentCourseId);
  }

  /**
   * 受講生コースステータスの登録です。 指定された受講生コースIDに対してステータス情報を新規登録します。
   *
   * @param request ステータス登録情報
   */
  @PostMapping
  public void register(@RequestBody CourseStatusRequest request) {
    service.register(
        request.getStudentCourseId(),
        request.getCourseStatus()
    );
  }

  /**
   * 受講生コースステータスの更新です。 既存の受講生コースIDのステータス情報を新しい情報で更新します。
   *
   * @param request ステータス更新情報
   */
  @PutMapping
  public void update(@RequestBody CourseStatusRequest request) {
    service.updateStatus(
        request.getStudentCourseId(),
        request.getCourseStatus()
    );
  }

}