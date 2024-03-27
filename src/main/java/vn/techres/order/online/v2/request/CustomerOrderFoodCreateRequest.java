package vn.techres.order.online.v1.request;import com.fasterxml.jackson.annotation.JsonProperty;import io.swagger.v3.oas.annotations.media.Schema;import jakarta.validation.constraints.DecimalMax;import jakarta.validation.constraints.DecimalMin;import jakarta.validation.constraints.Min;import lombok.AllArgsConstructor;import lombok.Data;import lombok.NoArgsConstructor;import org.hibernate.validator.constraints.Length;import java.math.BigDecimal;import java.util.ArrayList;import java.util.List;@Data@AllArgsConstructor@NoArgsConstructorpublic class CustomerOrderFoodCreateRequest {    @Schema(description = "Id món ăn không để trống")    @Min(value = 1, message = "Id món ăn truyền vào phải lớn hơn 0")    @JsonProperty("id")    private int id;    @Schema(description = "Số lượng không để trống")    @DecimalMax(value = "999.99", message = "Số lượng không được nhập quá 999.99")    @DecimalMin(value = "0.00", message = "Số lượng không được nhập phải lớn hơn 0")    @JsonProperty("quantity")    private BigDecimal quantity;    @Schema(description = "Danh sách món ăn kèm")    @JsonProperty("addition_foods")    private List<CustomerOrderFoodCreateRequest> additionFoods = new ArrayList<CustomerOrderFoodCreateRequest>();    @JsonProperty("note")    @Length(max = 255, message = "Ghi chú tối đa 255 ký tự")    private String note;    public CustomerOrderFoodCreateRequest(int id, BigDecimal quantity, String note) {        this.id = id;        this.quantity = quantity;        this.note = note;    }}