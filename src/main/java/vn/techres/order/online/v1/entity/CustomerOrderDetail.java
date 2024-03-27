package vn.techres.order.online.v1.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_order_details")
public class CustomerOrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "customer_order_id")
    private int customerOrderId;

    @Column(name = "food_id")
    private int foodId;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "order_detail_parent_id")
    private int orderDetailParentId;

    @Column(name = "order_detail_combo_parent_id")
    private int orderDetailComboParentId;

    @Column(name = "customer_order_detail_addition_ids")
    private String customerOrderDetailAdditionIds;

    @Column(name = "customer_order_detail_combo_ids")
    private String customerOrderDetailComboIds;

    private BigDecimal quantity;

    private BigDecimal price;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    private String note;

    @Column(name = "is_combo")
    private int isCombo;

    @Column(name = "is_addition")
    private int isAddition;


}
