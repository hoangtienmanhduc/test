package vn.techres.order.online.v1.request;import com.fasterxml.jackson.annotation.JsonProperty;import io.swagger.v3.oas.annotations.media.Schema;import jakarta.validation.constraints.NotEmpty;import lombok.Getter;import org.hibernate.validator.constraints.Length;@Getterpublic class CustomerOrderQRCodeRequest {    @Schema(description = "Id thiết bị không được để trống")    @NotEmpty(message = "device_id truyền không được phép truyền NULL hoặc rỗng")    @Length(max = 255, message = "device_id tối đa 255 ký tự")    @JsonProperty("device_id")    private String deviceId;    @Schema(description = "QR code không được phép truyền NULL hoặc rỗng")    @NotEmpty(message = "qr_code truyền không được phép truyền NULL hoặc rỗng")    @Length(max = 255, message = "qr_code tối đa 255 ký tự")    @JsonProperty("qr_code")    private String qrCode;}