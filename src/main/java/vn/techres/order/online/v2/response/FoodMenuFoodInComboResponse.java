package vn.techres.order.online.v1.response;import com.fasterxml.jackson.annotation.JsonProperty;import lombok.Data;import java.math.BigDecimal;import java.util.List;import java.util.stream.Collectors;@Datapublic class FoodMenuFoodInComboResponse {    private int id;    @JsonProperty("name")    private String name;    @JsonProperty("avatar")    private String avatar;    @JsonProperty("price")    private BigDecimal price;    @JsonProperty("combo_quantity")    private float comboQuantity;    @JsonProperty("is_out_stock")    private int isuOtStock;    public FoodMenuFoodInComboResponse() {}    public FoodMenuFoodInComboResponse(vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.FoodMenuFoodInComboResponse entity) {        this.id = entity.getId();        this.name = entity.getName() != null ? entity.getName() : "";        this.avatar = entity.getAvatar() != null ? entity.getAvatar() : "";        this.price = BigDecimal.valueOf(entity.getPrice());        this.comboQuantity = entity.getComboQuantity();        this.isuOtStock = entity.getIsOutStock();    }    public List<FoodMenuFoodInComboResponse> mapToList(List<vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.FoodMenuFoodInComboResponse> baseEntities) {        return baseEntities.stream().map(FoodMenuFoodInComboResponse::new).collect(Collectors.toList());    }}