package vn.techres.order.online.v1.response;

import lombok.Data;
import vn.techres.order.online.v1.entity.CustomerOrder;

import java.util.List;


@Data
public class CustomerOrderResult {

    private List<CustomerOrder> customerOrders;
    private long totalRecords;
}
