package api.spring.exception;

public class ApiDataNotExistException extends RuntimeException{
    public static int errorCode = 204;
    public static String message = "API 데이터가 없습니다.";
}
