package vn.techres.order.online.v1.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CustomerOrderCreateRequest {

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

    @Schema(description = "Id khu vực không để trống")
    @Min(value = 1, message = "area_id truyền vào phải lớn hơn 0")
    @JsonProperty("area_id")
    private int areaId;

    @Schema(description = "Id bàn không để trống")
    @Min(value = 1, message = "table_id truyền vào phải lớn hơn 0")
    @JsonProperty("table_id")
    private int tableId;

    @Schema(description = "Ghi chú không được phép truyền NULL")
    @NotNull(message = "Ghi chú không được phép truyền NULL")
    private String note;

    @Schema(description = "Danh sách món ăn không được phép truyền rỗng hoặc NULL")
    @NotNull(message = "Danh sách món ăn không được phép truyền rỗng hoặc NULL")
    @Valid
    private List<CustomerOrderFoodCreateRequest> foods;

    @Schema(description = "trạng thái đơn hàng")
    @Min(value = 0, message = "customer_order_status truyền vào phải lớn hơn 0")
    @Max(value = 4, message = "customer_order_status truyền vào phải lớn hơn 0")
    @JsonProperty("customer_order_status")
    private int customerOrderStatus;

}
