package raisetech.student.management.controller.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 🔹 バリデーションエラー
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  // 🔹 データが存在しない場合
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(
      IllegalArgumentException ex) {

    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  // 🔹 想定外エラー
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleException(Exception ex) {

    Map<String, String> error = new HashMap<>();
    error.put("error", "サーバーエラーが発生しました");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}