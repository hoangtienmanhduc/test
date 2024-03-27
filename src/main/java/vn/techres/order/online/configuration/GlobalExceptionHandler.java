package vn.techres.order.online.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.techres.order.online.common.exception.TechResHttpException;
import vn.techres.order.online.v1.response.BaseResponse;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ TechResHttpException.class }) // Có thể bắt nhiều loại exception
    public ResponseEntity<?> handleException(Exception e) {
        BaseResponse<?> response = new BaseResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessageError(new Date() + "Error: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler({ Exception.class }) // Có thể bắt nhiều loại exception
    public ResponseEntity<?> exception(Exception e) {
        BaseResponse<?> response = new BaseResponse<>();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessageError(e.getMessage());
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessageError(getValidationErrorMessage(ex.getBindingResult()));
        response.setData(null);

        return ResponseEntity.badRequest().body(response);
    }

    private String getValidationErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMessage.append(error.getDefaultMessage()).append(";");
        }

        return errorMessage.toString();
    }

    // // Nên bắt cả Exception.class
    // @ExceptionHandler({ Exception.class })
    // public ResponseEntity<?> handleUnwantedException(Exception e) {
    // BaseResponse<?> response = new BaseResponse<>();
    // response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    // response.setMessageError(e.toString());
    // // Thực tế người ta dùng logger
    // return ResponseEntity.status(HttpStatus.OK).body(response);
    // }
}
