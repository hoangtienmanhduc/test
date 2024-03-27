package vn.techres.order.online.v1.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import vn.techres.order.online.configuration.validation.ExistIn;
import vn.techres.order.online.configuration.validation.PhoneValidatorConstraint;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineOrderCreateRequest {

    @Schema(description = "Id nhà hàng không để trống")
    @Min(value = 1, message = "restaurant_id truyền vào phải lớn hơn 0")
    @JsonProperty("restaurant_id")
    private int restaurantId;

    @Schema(description = "Id thương hiệu không để trống")
    @Min(value = 1, message = "restaurant_brand_id truyền vào phải lớn hơn 0")
    @JsonProperty("restaurant_brand_id")
    private int restaurantBrandId;

    @Schema(description = "Id chi nhánh không để trống")
    @Min(value = 1, message = "branch_id truyền vào phải lớn hơn 0")
    @JsonProperty("branch_id")
    private int branchId;

    // @Schema(description = "Id khách hàng không có mặc định là 0")
    // @Min(value = 0, message = "customer_id không truyền vào mặc định là 0")
    // @JsonProperty("customer_id")
    // private int customerId = 0;

    @Schema(description = "Tên khách đặt không được phép truyền NULL hoặc rỗng")
    @NotNull(message = "Tên khách không được phép truyền NULL")
    @Length(min = 1, max = 255, message = "Tên từ 3 -> 255 ký tự")
    @JsonProperty("customer_name")
    private String customerName;

    @Schema(description = "payment_method không để trống")
    @Min(value = 0, message = "payment_method truyền vào : [ 0: COD - 1: PREPAYMENT ] có giá trị là 0 hoặc 1")
    @Max(value = 1, message = "payment_method truyền vào : [ 0: COD - 1: PREPAYMENT ] có giá trị là 0 hoặc 1")
    @ExistIn(values = { 0, 1 }, message = "payment_method truyền vào : [ 0: COD - 1: PREPAYMENT ] có giá trị là 0 " +
            "hoặc 1")
    @JsonProperty("payment_method")
    private int paymentMethod;

    @Schema(description = "Số điện thoại không được phép truyền NULL hoặc rỗng")
    @NotNull(message = "Số điện thoại không được phép truyền NULL")
    @PhoneValidatorConstraint(message = "Số điện thoại không hợp lệ")
    private String phone;

    @Schema(description = "Địa chỉ nhận không được phép truyền NULL hoặc rỗng")
    @NotNull(message = "Địa chỉ không được phép truyền NULL")
    @Length(max = 555, message = "Ghi chú tối đa 555 ký tự")
    private String address;

    @Schema(description = "Ghi chú không được phép truyền NULL")
    @NotNull(message = "Ghi chú không được phép truyền NULL")
    @Length(max = 255, message = "Ghi chú tối đa 255 ký tự")
    private String note;

    @Schema(description = "Danh sách món ăn không được phép truyền rỗng hoặc NULL")
    @NotNull(message = "Danh sách món ăn không được phép truyền rỗng hoặc NULL")
    @Valid
    private List<CustomerOrderFoodCreateRequest> foods;

    @Schema(description = "customer_order_status không để trống")
    @Min(value = 0, message = "customer_order_status truyền vào >= 0")
    @Max(value = 4, message = "customer_order_status truyền vào <= 0")
    @ExistIn(values = { 0, 1, 2, 4 }, message = "Trạng thái đơn hàng" +
            "hoặc 1")
    @JsonProperty("customer_order_status")
    private int customerOrderStatus;
}
