package vn.techres.order.online.v1.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.techres.order.online.common.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodMenuResponse {

    private int id;

    @JsonProperty("restaurant_id")
    private int restaurantId;

    @JsonProperty("restaurant_brand_id")
    private int restaurantBrandId;

    @JsonProperty("branch_id")
    private int branchId;

    @JsonProperty("name")
    private String name = "";

    @JsonProperty("food_addition_ids")
    private List<Integer> foodAdditionIds = new ArrayList<>();

    private BigDecimal price;

    @JsonProperty("price_with_temporary")
    private BigDecimal priceWithTemporary;

    @JsonProperty("is_combo")
    private int isCombo;

    @JsonProperty("is_addition")
    private int isAddition;

    @JsonProperty("is_addition_like_food")
    private int isAdditionLikeFood;

    @JsonProperty("food_in_combo")
    private List<FoodInComboResponse> foodInCombo = new ArrayList<>();

    @JsonProperty("food_in_addition")
    private List<FoodInAdditionResponse> foodInAddition = new ArrayList<>();

    public FoodMenuResponse(
            vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.FoodMenuResponse entity) {
        this.id = entity.getId();
        this.restaurantId = entity.getRestaurantId();
        this.restaurantBrandId = entity.getRestaurantBrandId();
        this.branchId = entity.getBranchId();
        this.foodAdditionIds = Utils.convertStringToArrayList(entity.getFoodAdditionIds());
        this.name = entity.getName();
        this.price = BigDecimal.valueOf(entity.getPrice());
        this.isAddition = entity.getIsAddition();
        this.isAdditionLikeFood = entity.getIsAdditionLikeFood();
        this.isCombo = entity.getIsCombo();
        this.priceWithTemporary = BigDecimal.valueOf(entity.getPriceWithTemporary());
        this.foodInCombo = Utils.convertJsonStringToListObject(entity.getFoodInCombo(), FoodInComboResponse[].class);
        this.foodInAddition = Utils.convertJsonStringToListObject(entity.getFoodInAddition(), FoodInAdditionResponse[].class);
    }

    public List<FoodMenuResponse> mapToList(
            List<vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.FoodMenuResponse> entities) {
        return entities.stream().map(FoodMenuResponse::new).toList();
    }

}
