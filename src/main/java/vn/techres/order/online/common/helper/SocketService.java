package vn.techres.order.online.common.helper;

import org.springframework.beans.factory.annotation.Autowired;

import vn.techres.order.online.common.model.OrderRealtimeModel;
import vn.techres.order.online.common.utils.Constants;
import vn.techres.order.online.configuration.SocketClientSingleton;
import vn.techres.order.online.v1.entity.CustomerOrder;

public class SocketService {



    public static void addOrder(CustomerOrder order) {
        try {
            OrderRealtimeModel model = new OrderRealtimeModel();
            model.setOrderId(order.getId());
            model.setRestaurantId(order.getRestaurantId());
            model.setRestaurantBrandId(order.getRestaurantBrandId());
            model.setBranchId(order.getBranchId());
            model.setCustomerName(order.getCustomerName());
            model.setCustomerPhone(order.getCustomerPhone());
            model.setCustomerAddress(order.getCustomerAddress());
            model.setTotalAmount(order.getTotalAmount());
            model.setPaymentMethod(order.getPaymentMethod());
            model.setPaymentStatus(order.getPaymentStatus());
            model.setCustomerOrderStatus(order.getCustomerOrderStatus());
            SocketClientSingleton.getInstance().emit(Constants.SOCKET_IO_ORDER_ONLINE_PATH, model.toJSON());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
