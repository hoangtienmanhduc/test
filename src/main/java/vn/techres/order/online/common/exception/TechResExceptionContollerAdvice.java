package vn.techres.order.online.common.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.techres.order.online.v1.response.BaseResponse;

@ControllerAdvice
public class TechResExceptionContollerAdvice {


    @ExceptionHandler(TechResHttpException.class)
    public ResponseEntity<BaseResponse<String>> handleException(Exception ex) {
        BaseResponse<String> response = new BaseResponse<String>();
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setMessageError(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
