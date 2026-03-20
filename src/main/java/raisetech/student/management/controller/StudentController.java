package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.student.management.controller.dto.RegisterStudentRequest;
import raisetech.student.management.controller.dto.UpdateStudentRequest;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.service.StudentsService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentsService service;

  @Autowired
  public StudentController(StudentsService service) {
    this.service = service;
  }

  /**
   * 受講生の一覧検索です。
   * 全件検索を行うので、条件指定は行いません。
   *
   * @return　受講生詳細一覧(全件)
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id　受講生ID
   * @return　受講生詳細
   */
  @Operation(summary ="詳細一覧検索", description = "受講生の詳細一覧検索します。")
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable Long id) {
    return service.searchStudent(id);
  }

  /**
   *受講生の登録を行います。
   *
   *@param request 登録する受講生の情報(入力バリデーション済み)
   *@return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生情報を登録します。")
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(@Valid @RequestBody RegisterStudentRequest request) {
    StudentDetail responseStudentDetail = service.registerStudent(request);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生情報の更新を行います。
   * キャンセルフラグの更新もここで行います(論理削除)
   * @param request　更新する受講生の情報(バリデーション済み)
   * @return　実行結果(HTTP 204 No Content)
   */
  @Operation(summary ="受講生更新", description ="受講生情報を更新します。")
  @PutMapping("/updateStudent")
  public ResponseEntity<Void> updateStudent(@Valid @RequestBody UpdateStudentRequest request) {
    service.updateStudent(request);
    return ResponseEntity.noContent().build();
  }
}


