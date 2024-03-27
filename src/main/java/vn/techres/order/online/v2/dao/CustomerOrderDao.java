package vn.techres.order.online.v1.dao;

import java.util.List;

import vn.techres.order.online.common.storeProcedure.CustomerOrderAlolineModel;
import vn.techres.order.online.common.storeProcedure.StoreProcedureResult;
import vn.techres.order.online.v1.entity.CustomerOrder;

public interface CustomerOrderDao {
        CustomerOrder spUCreateOnlineOrder(int restaurantId, int restaurantBrandId, int branchId, int customerId,
                        int paymentMethod, String customerName,
                        String phone, String address, String foodsJson, String foodData, String note,
                        int customerOrderOnlineStatus)
                        throws Exception;

        CustomerOrder findOne(int id);

        void update(CustomerOrder orderCustomer);

        void delete(CustomerOrder orderCustomer);

<<<<<<< HEAD
    void deleteCustomerOrder(String customerOrderIds) throws Exception;

    List<CustomerOrder> getListByRestaurantIdAndRestaurantBrandIdAndBranchIdAndOnlineOrderStatus(int restaurantId,
            int restaurantBrandId, int branchId, int areaId,
            int tableId,
            int customerOrderType, List<Integer> orderStatus);
=======
        List<CustomerOrder> getListByRestaurantIdAndRestaurantBrandIdAndBranchIdAndOnlineOrderStatus(int restaurantId,
                        int restaurantBrandId, int branchId, int areaId,
                        int tableId,
                        int customerOrderType, List<Integer> orderStatus);
>>>>>>> 40c75648976d1f1e9bd140bb3a9930f538e92ed8

        void spCalculateOrderTotalAmount(int id) throws Exception;

        CustomerOrder findByTransactionId(long transactionId);

        CustomerOrder findByRestaurantIdAndRestaurantBrandIdAndBranchIdAndAreaIdAndTableIdAnhCustomerOrderStatus(
                        int restaurantId, int restaurantBrandId, int branchId, int areaId, int tableId,
                        int customerOrderStatus,
                        int customerOrderType);

        CustomerOrder spUCreateCustomerOrder(int restaurantId, int restaurantBrandId, int branchId, int areaId,
                        int tableId, String foodsJson, String foodData, String tableName, String note) throws Exception;

        List<CustomerOrder> getListByCustomerIdAndOnlineOrderStatus(int customerId, List<Integer> orderStatuses);

        StoreProcedureResult<CustomerOrderAlolineModel> spGetListCustomerOrderAloline(int restaurantId,
                        int restaurantBrandId, int branchId, int customerId, List<Integer> orderStatuses, int page,
                        int limit)
                        throws Exception;

        CustomerOrder findOneCustomerOrderOnlineByOrderId(long orderId);
}
