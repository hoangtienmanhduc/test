package vn.techres.order.online.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

public class BaseResponse<T> {

    @Schema(description = "Trạng thái dữ liệu trả về")
    private int status;

    @Schema(description = "THông báo")
    private String message;
    @Schema(description = "Nội dung trả về")
    private T data;

    public BaseResponse() {
        this.setStatus(HttpStatus.OK);
        this.setMessage(HttpStatus.OK);
        this.setData(null);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(HttpStatus statusEnum) {
        this.status = statusEnum.value();
        this.message = statusEnum.name();
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(HttpStatus statusEnum) {
        this.message = statusEnum.name();
    }

    public void setMessageError(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
