package vn.techres.order.online.v1.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import vn.techres.order.online.common.enums.StoreProcedureStatusCodeEnum;
import vn.techres.order.online.common.exception.TechResHttpException;
import vn.techres.order.online.common.storeProcedure.CustomerOrderAlolineModel;
import vn.techres.order.online.common.storeProcedure.StoreProcedureResult;
import vn.techres.order.online.v1.dao.AbstractDao;
import vn.techres.order.online.v1.dao.CustomerOrderDao;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.version.VersionService;

@Repository("CustomerOrderDaoImpl" + VersionService.VERSION)
@Transactional
public class CustomerOrderDaoImpl extends AbstractDao<CustomerOrder, Integer> implements CustomerOrderDao {

    @Override
    public CustomerOrder spUCreateOnlineOrder(int restaurantId, int restaurantBrandId, int branchId, int customerId,
            int paymentMethod,
            String customerName, String phone, String address, String foodsJson,
            String foodData, String note, int customerOrderOnlineStatus) throws Exception {
        StoredProcedureQuery query = this.getSession()
                .createStoredProcedureQuery("sp_u_create_online_order", CustomerOrder.class)

                .registerStoredProcedureParameter("restaurantId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("restaurantBrandId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("branchId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("customerId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("paymentMethod", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("customerName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("phone", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("address", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("foodsJson", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("foodData", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("_note", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("customerOrderOnlineStatus", Integer.class, ParameterMode.IN)

                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("restaurantId", restaurantId);
        query.setParameter("restaurantBrandId", restaurantBrandId);
        query.setParameter("branchId", branchId);
        query.setParameter("customerId", customerId);
        query.setParameter("paymentMethod", paymentMethod);
        query.setParameter("customerName", customerName);
        query.setParameter("phone", phone);
        query.setParameter("address", address);
        query.setParameter("foodsJson", foodsJson);
        query.setParameter("foodData", foodData);
        query.setParameter("_note", note);
        query.setParameter("customerOrderOnlineStatus", customerOrderOnlineStatus);

        int statusCode = (int) query.getOutputParameterValue("status_code");
        String messageError = query.getOutputParameterValue("message_error").toString();

        switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
            case SUCCESS:
                return (CustomerOrder) query.getResultList().stream().findFirst().orElse(null);
            case INPUT_INVALID:
                throw new TechResHttpException(HttpStatus.BAD_REQUEST, messageError);
            default:
                throw new Exception(messageError);
        }
    }

    @Override
    public CustomerOrder findOne(int id) {
        return this.getSession().find(CustomerOrder.class, id);
    }

    @Override
    public void update(CustomerOrder orderCustomer) {
        this.getSession().update(orderCustomer);
    }

    @Override
    public void delete(CustomerOrder orderCustomer) {
        this.getSession().delete(orderCustomer);
    }

    @Override
    public void deleteCustomerOrder(String customerOrderIds) throws Exception {
        StoredProcedureQuery query = this.getSession()
                .createStoredProcedureQuery("sp_delete_all_customer_order_by_customer_order_ids")
                .registerStoredProcedureParameter("customerOrderIds", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("customerOrderIds", customerOrderIds);

        query.execute();

        int statusCode = (int) query.getOutputParameterValue("status_code");
        String messageError = query.getOutputParameterValue("message_error").toString();

        switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
            case SUCCESS:
                break;
            case INPUT_INVALID:
                throw new TechResHttpException(HttpStatus.BAD_REQUEST, messageError);
            default:
                throw new Exception(messageError);
        }
    }

    @Override
    public List<CustomerOrder> getListByRestaurantIdAndRestaurantBrandIdAndBranchIdAndOnlineOrderStatus(
            int restaurantId, int restaurantBrandId, int branchId, int areaId,
            int tableId,
            int customerOrderType, List<Integer> orderStatus) {
        CriteriaQuery<CustomerOrder> criteriaQuery = this.getBuilder().createQuery(CustomerOrder.class);
        Root<CustomerOrder> root = criteriaQuery.from(CustomerOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.getBuilder().equal(root.get("restaurantId"),
                restaurantId));

        if (restaurantBrandId > 0) {
            predicates.add(this.getBuilder().equal(root.get("restaurantBrandId"),
                    restaurantBrandId));
        }
        if (branchId > 0) {
            predicates.add(this.getBuilder().equal(root.get("branchId"), branchId));
        }

        // if (areaId > 0) {
        // predicates.add(this.getBuilder().equal(root.get("areaId"), areaId));
        // }
        //
        // if (tableId > 0) {
        // predicates.add(this.getBuilder().equal(root.get("tableId"), tableId));
        // }

        if (customerOrderType >= 0) {
            predicates.add(this.getBuilder().equal(root.get("customerOrderType"), customerOrderType));
        }

        if (!orderStatus.isEmpty()) {
            predicates.add((root.get("customerOrderStatus").in(orderStatus)));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));

        return this.getSession().createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void spCalculateOrderTotalAmount(int id) throws Exception {
        StoredProcedureQuery query = this.getSession()
                .createStoredProcedureQuery("sp_calculate_online_order_total_amount", CustomerOrder.class)
                .registerStoredProcedureParameter("customerOrderId", Integer.class, ParameterMode.IN)

                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("customerOrderId", id);

        int statusCode = (int) query.getOutputParameterValue("status_code");
        String messageError = query.getOutputParameterValue("message_error").toString();

        switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
            case SUCCESS:
                break;
            case INPUT_INVALID:
                throw new TechResHttpException(HttpStatus.BAD_REQUEST, messageError);
            default:
                throw new Exception(messageError);
        }
    }

    @Override
    public CustomerOrder findByTransactionId(long transactionId) {

        CriteriaQuery<CustomerOrder> criteriaQuery = this.getBuilder().createQuery(CustomerOrder.class);
        Root<CustomerOrder> root = criteriaQuery.from(CustomerOrder.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(this.getBuilder().equal(root.get("transactionId"), transactionId));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        return this.getSession().createQuery(criteriaQuery).getSingleResultOrNull();

    }

    @Override
    public CustomerOrder findByRestaurantIdAndRestaurantBrandIdAndBranchIdAndAreaIdAndTableIdAnhCustomerOrderStatus(
            int restaurantId, int restaurantBrandId,
            int branchId, int areaId, int tableId,
            int customerOrderStatus, int customerOrderType) {

        CriteriaQuery<CustomerOrder> criteriaQuery = this.getBuilder().createQuery(CustomerOrder.class);
        Root<CustomerOrder> root = criteriaQuery.from(CustomerOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.getBuilder().equal(root.get("restaurantId"),
                restaurantId));

        if (restaurantBrandId > 0) {
            predicates.add(this.getBuilder().equal(root.get("restaurantBrandId"),
                    restaurantBrandId));
        }
        if (branchId > 0) {
            predicates.add(this.getBuilder().equal(root.get("branchId"), branchId));
        }
        if (areaId > 0) {
            predicates.add(this.getBuilder().equal(root.get("areaId"), areaId));
        }
        if (tableId > 0) {
            predicates.add(this.getBuilder().equal(root.get("tableId"), tableId));
        }

        if (customerOrderStatus >= 0) {
            predicates.add(this.getBuilder().equal(root.get("customerOrderStatus"), customerOrderStatus));
        }

        if (customerOrderType >= 0) {
            predicates.add(this.getBuilder().equal(root.get("customerOrderType"), customerOrderType));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));

        return this.getSession().createQuery(criteriaQuery).getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public CustomerOrder spUCreateCustomerOrder(int restaurantId, int restaurantBrandId,
            int branchId, int areaId, int tableId,
            String foodsJson, String foodData, String tableName, String note) throws Exception {
        StoredProcedureQuery query = this.getSession()
                .createStoredProcedureQuery("sp_u_create_customer_order", CustomerOrder.class)

                .registerStoredProcedureParameter("restaurantId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("restaurantBrandId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("branchId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("areaId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("tableId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("foodsJson", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("foodData", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("tableName", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("note", String.class, ParameterMode.IN)

                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT);

        query.setParameter("restaurantId", restaurantId);
        query.setParameter("restaurantBrandId", restaurantBrandId);
        query.setParameter("branchId", branchId);
        query.setParameter("areaId", areaId);
        query.setParameter("tableId", tableId);
        query.setParameter("foodsJson", foodsJson);
        query.setParameter("foodData", foodData);
        query.setParameter("tableName", tableName);
        query.setParameter("note", note);

        int statusCode = (int) query.getOutputParameterValue("status_code");
        String messageError = query.getOutputParameterValue("message_error").toString();

        switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
            case SUCCESS:
                return (CustomerOrder) query.getResultList().stream().findFirst().orElse(null);
            case INPUT_INVALID:
                throw new TechResHttpException(HttpStatus.BAD_REQUEST, messageError);
            default:
                throw new Exception(messageError);
        }
    }

    @Override
    public StoreProcedureResult<CustomerOrderAlolineModel> spGetListCustomerOrderAloline(int restaurantId,
            int restaurantBrandId, int branchId, int customerId, List<Integer> orderStatuses, int page, int limit)
            throws Exception {
        StoredProcedureQuery query = this.getSession()
                .createStoredProcedureQuery("sp_get_list_customer_order_aloline", CustomerOrderAlolineModel.class)

                .registerStoredProcedureParameter("restaurantId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("restaurantBrandId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("branchId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("customerId", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("orderStatuses", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("limit", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("_offset", Integer.class, ParameterMode.IN)

                .registerStoredProcedureParameter("status_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("message_error", String.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("_totalRecord", Integer.class, ParameterMode.OUT);

        query.setParameter("restaurantId", restaurantId);
        query.setParameter("restaurantBrandId", restaurantBrandId);
        query.setParameter("branchId", branchId);
        query.setParameter("customerId", customerId);
        query.setParameter("orderStatuses", orderStatuses.toString());
        query.setParameter("limit", limit);
        query.setParameter("_offset", page);

        int statusCode = (int) query.getOutputParameterValue("status_code");
        String messageError = query.getOutputParameterValue("message_error").toString();

        switch (StoreProcedureStatusCodeEnum.valueOf(statusCode)) {
            case SUCCESS:
                int totalRecord = (int) query.getOutputParameterValue("_totalRecord");
                return new StoreProcedureResult<CustomerOrderAlolineModel>(statusCode, messageError, totalRecord,
                        query.getResultList());
            case INPUT_INVALID:
                throw new TechResHttpException(HttpStatus.BAD_REQUEST, messageError);
            default:
                throw new Exception(messageError);
        }
    }

    @Override
    public List<CustomerOrder> getListByCustomerIdAndOnlineOrderStatus(int customerId, List<Integer> orderStatuses) {
        CriteriaQuery<CustomerOrder> criteriaQuery = this.getBuilder().createQuery(CustomerOrder.class);
        Root<CustomerOrder> root = criteriaQuery.from(CustomerOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(this.getBuilder().equal(root.get("customerId"),
                customerId));

        predicates.add(this.getBuilder().equal(root.get("customerOrderType"), 1));

        if (!orderStatuses.isEmpty()) {
            predicates.add((root.get("customerOrderStatus").in(orderStatuses)));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[] {}));

        return this.getSession().createQuery(criteriaQuery).getResultList();
    }
    

    @Override
    public CustomerOrder findOneCustomerOrderOnlineByOrderId(long orderId) {

        CriteriaQuery<CustomerOrder> criteriaQuery = this.getBuilder().createQuery(CustomerOrder.class);
        Root<CustomerOrder> root = criteriaQuery.from(CustomerOrder.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(this.getBuilder().equal(root.get("orderId"), orderId));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        return this.getSession().createQuery(criteriaQuery).getSingleResultOrNull();

    }
}
