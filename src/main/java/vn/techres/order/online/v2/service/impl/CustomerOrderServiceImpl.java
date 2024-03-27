package vn.techres.order.online.v1.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.techres.order.online.common.storeProcedure.CustomerOrderAlolineModel;
import vn.techres.order.online.common.storeProcedure.StoreProcedureResult;
import vn.techres.order.online.v1.dao.CustomerOrderDao;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.service.CustomerOrderService;
import vn.techres.order.online.v1.version.VersionService;

import java.util.List;

@Service("CustomerOrderService" + VersionService.VERSION)
@Transactional(rollbackOn = Exception.class)
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Autowired
    private CustomerOrderDao dao;

    @Override
    public CustomerOrder spUCreateOnlineOrder(int restaurantId, int restaurantBrandId, int branchId, int customerId,
            int paymentMethod,
            String customerName, String phone, String address, String foodsJson, String foodData, String note,
            int customerOrderOnlineStatus)
            throws Exception {
        return dao.spUCreateOnlineOrder(restaurantId, restaurantBrandId, branchId, customerId, paymentMethod,
                customerName, phone,
                address,
                foodsJson, foodData, note, customerOrderOnlineStatus);
    }

    @Override
    public CustomerOrder findOne(int id) {
        return dao.findOne(id);
    }

    @Override
    public CustomerOrder findOneCustomerOrderOnlineByOrderId(long orderId) {
        return dao.findOneCustomerOrderOnlineByOrderId(orderId);
    }

    @Override
    public void update(CustomerOrder orderCustomer) {
        dao.update(orderCustomer);
    }

    @Override
    public void delete(CustomerOrder orderCustomer) {
        dao.delete(orderCustomer);
    }

    @Override
    public void deleteCustomerOrder(String customerOrderIds) throws Exception {
        dao.deleteCustomerOrder(customerOrderIds);
    }

    @Override
    public List<CustomerOrder> getListByRestaurantIdAndRestaurantBrandIdAndBranchIdAndOnlineOrderStatus(
            int restaurantId, int restaurantBrandId, int branchId, int areaId,
            int tableId,
            int customerOrderType, List<Integer> orderStatus) {
        return dao.getListByRestaurantIdAndRestaurantBrandIdAndBranchIdAndOnlineOrderStatus(restaurantId,
                restaurantBrandId, branchId, areaId, tableId, customerOrderType, orderStatus);
    }

    @Override
    public void spCalculateOrderTotalAmount(int id) throws Exception {
        dao.spCalculateOrderTotalAmount(id);
    }

    @Override
    public CustomerOrder findByTransactionId(long transactionId) {
        return dao.findByTransactionId(transactionId);
    }

    @Override
    public CustomerOrder findByRestaurantIdAndRestaurantBrandIdAndBranchIdAndAreaIdAndTableIdAnhCustomerOrderStatus(
            int restaurantId, int restaurantBrandId, int branchId, int areaId, int tableId, int customerOrderStatus,
            int customerOrderType) {
        return dao.findByRestaurantIdAndRestaurantBrandIdAndBranchIdAndAreaIdAndTableIdAnhCustomerOrderStatus(
                restaurantId, restaurantBrandId, branchId, areaId, tableId, customerOrderStatus, customerOrderType);
    }

    @Override
    public CustomerOrder spUCreateCustomerOrder(int restaurantId, int restaurantBrandId, int branchId, int areaId,
            int tableId, String foodsJson, String foodData, String tableName, String note) throws Exception {
        return dao.spUCreateCustomerOrder(restaurantId, restaurantBrandId, branchId, areaId, tableId, foodsJson,
                foodData, tableName, note);
    }

    @Override
    public List<CustomerOrder> getListByCustomerIdAndOnlineOrderStatus(int customerId, List<Integer> orderStatuses) {
        return dao.getListByCustomerIdAndOnlineOrderStatus(customerId, orderStatuses);
    }

    @Override
    public StoreProcedureResult<CustomerOrderAlolineModel> spGetListCustomerOrderAloline(int restaurantId,
            int restaurantBrandId, int branchId, int customerId, List<Integer> orderStatuses, int page, int limit)
            throws Exception {
        return dao.spGetListCustomerOrderAloline(restaurantId, restaurantBrandId, branchId, customerId, orderStatuses,
                page, limit);
    }

}
