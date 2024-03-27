package vn.techres.order.online.v1.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.techres.order.online.common.utils.Utils;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderDetailResponse {

    @Schema(description = "Id detail của món ăn")
    @JsonProperty("id")
    private long id;

    @Schema(description = "Id của bill tạm")
    @JsonProperty("customer_order_id")
    private int customerOrderId;

    @Schema(description = "Id của món ăn")
    @JsonProperty("food_id")
    private int foodId;

    @Schema(description = "Tên của món ăn")
    @JsonProperty("food_name")
    private String foodName;

    @Schema(description = "Id món cha")
    @JsonProperty("order_detail_parent_id")
    private int orderDetailParentId;

    @Schema(description = "Id của món combo cha")
    @JsonProperty("order_detail_combo_parent_id")
    private int orderDetailComboParentId;

    @Schema(description = "Id của bill tạm chưa danh sách món con")
    @JsonProperty("customer_order_detail_addition_ids")
    private List<Integer> customerOrderDetailAdditionIds;

    @Schema(description = "Id của bill tạm chưa danh sách món combo")
    @JsonProperty("customer_order_detail_combo_ids")
    private List<Integer> customerOrderDetailComboIds;

    @Schema(description = "Danh sách món combo")
    @JsonProperty("customer_order_detail_combo")
    private List<FoodInComboResponse> customerOrderDetailCombos = new ArrayList<>();

    @Schema(description = "Danh sách món addition")
    @JsonProperty("customer_order_detail_addition")
    private List<FoodInAdditionResponse> customerOrderDetailAddition = new ArrayList<>();

    @Schema(description = "Số lượng món")
    @JsonProperty("quantity")
    private BigDecimal quantity;

    @Schema(description = "Giá")
    @JsonProperty("price")
    private BigDecimal price;

    @Schema(description = "Tổng tiền")
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @Schema(description = "Có phải món bán kèm không")
    @JsonProperty("is_addition")
    private int isAddition;

    @Schema(description = "Có phải món combo không")
    @JsonProperty("is_combo")
    private int isCombo;

    @Schema(description = "Ghi chú")
    @JsonProperty("note")
    private String note;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    public CustomerOrderDetailResponse(CustomerOrderDetail entity) throws Exception {
        this.id = entity.getId();
        this.customerOrderId = entity.getCustomerOrderId();
        this.foodId = entity.getFoodId();
        this.foodName = entity.getFoodName();
        this.orderDetailParentId = entity.getOrderDetailParentId();
        this.orderDetailComboParentId = entity.getOrderDetailComboParentId();
        this.customerOrderDetailAdditionIds = Utils
                .convertJsonStringToListObject(entity.getCustomerOrderDetailAdditionIds(), Integer[].class);
        this.customerOrderDetailComboIds = Utils.convertJsonStringToListObject(entity.getCustomerOrderDetailComboIds(),
                Integer[].class);
        this.quantity = entity.getQuantity();
        this.price = entity.getPrice();
        this.totalPrice = entity.getTotalPrice();
        this.isAddition = entity.getIsAddition();
        this.isCombo = entity.getIsCombo();
        this.note = entity.getNote();
        this.createdAt = Utils.getDatetimeFormatVN(entity.getCreatedAt());
        this.updatedAt = Utils.getDatetimeFormatVN(entity.getUpdatedAt());


    }

    public List<CustomerOrderDetailResponse> mapToList(List<CustomerOrderDetail> entities) throws Exception {
        return entities.stream().map(x -> {
            try {
                return new CustomerOrderDetailResponse(x);
            } catch (Exception e) {

                return new CustomerOrderDetailResponse();
            }
        }).toList();
    }

}
