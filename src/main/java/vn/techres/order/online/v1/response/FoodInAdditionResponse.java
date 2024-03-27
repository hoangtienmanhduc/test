package vn.techres.order.online.v1.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.entity.FoodInAdditionModel;

@Data
public class FoodInAdditionResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    public FoodInAdditionResponse() {
    }

    public FoodInAdditionResponse(FoodInAdditionModel entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public FoodInAdditionResponse(CustomerOrderDetail entity) {
        this.id = (int) entity.getId();
        this.name = entity.getFoodName();
        this.quantity = entity.getQuantity();
    }

    public List<FoodInAdditionResponse> mapToList(List<FoodInAdditionModel> entities) {
        return entities.stream().map(FoodInAdditionResponse::new).toList();
    }
}
