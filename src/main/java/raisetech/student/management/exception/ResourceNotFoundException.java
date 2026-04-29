package raisetech.student.management.exception;

/**
 * リソースが見つからない場合（404）に使用する例外クラスです。
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
