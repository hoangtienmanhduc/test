package vn.techres.order.online.v1.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.entity.FoodInComboModel;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodInComboResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    public FoodInComboResponse() {
    }

    public FoodInComboResponse(FoodInComboModel entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.quantity = BigDecimal.valueOf(0);
    }

    public FoodInComboResponse(CustomerOrderDetail entity) {
        this.id = entity.getId();
        this.name = entity.getFoodName();
        this.quantity = entity.getQuantity();
    }

    public List<FoodInComboResponse> mapToList(List<FoodInComboModel> entities) {
        return entities.stream().map(FoodInComboResponse::new).toList();
    }
}
