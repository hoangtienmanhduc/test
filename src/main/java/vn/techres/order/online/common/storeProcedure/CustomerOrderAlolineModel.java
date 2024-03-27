package vn.techres.order.online.common.storeProcedure;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import vn.techres.order.online.v1.entity.BaseEntity;
import vn.techres.order.online.v1.entity.CustomerOrder;

@Data
@Entity
public class CustomerOrderAlolineModel extends BaseEntity {

	@Id
    private int id;

    @Column(name = "restaurant_id")
    private int restaurantId;

    @Column(name = "restaurant_brand_id")
    private int restaurantBrandId;

    @Column(name = "branch_id")
    private int branchId;

    @Column(name = "area_id")
    private int areaId;

    @Column(name = "table_id")
    private int tableId;

    @Column(name = "order_id")
    private int orderId;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "transaction_id")
    private long transactionId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "payment_method")
    private int paymentMethod;

    @Column(name = "payment_status")
    private int paymentStatus;

    @Column(name = "customer_order_status")
    private int customerOrderStatus;

    @Column(name = "customer_order_type")
    private int customerOrderType;

    private String note;

    @Column(name = "food_count")
    private int foodCount;
    
}
