package vn.techres.order.online.v1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "customer_orders")
public class CustomerOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
