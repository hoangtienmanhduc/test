package vn.techres.order.online.grpc.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import vn.techres.microservice.grpc.java.customer_order_online.order_online_aloline.*;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuByFoodIdsRequest;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuServiceGrpc;
import vn.techres.order.online.common.enums.CustomerOrderStatusEnum;
import vn.techres.order.online.common.helper.SocketService;
import vn.techres.order.online.common.pagination.Pagination;
import vn.techres.order.online.common.storeProcedure.CustomerOrderAlolineModel;
import vn.techres.order.online.common.storeProcedure.StoreProcedureResult;
import vn.techres.order.online.common.utils.Utils;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.request.CustomerOrderFoodCreateRequest;
import vn.techres.order.online.v1.request.OnlineOrderCreateRequest;
import vn.techres.order.online.v1.response.FoodMenuResponse;
import vn.techres.order.online.v1.service.CustomerOrderDetailService;
import vn.techres.order.online.v1.service.CustomerOrderService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@GrpcService
public class OrderOnlineAlolineServiceGrpcImpl extends OrderOnlineAlolineServiceGrpc.OrderOnlineAlolineServiceImplBase {

        @Autowired
        private CustomerOrderService customerOrderService;

        @Autowired
        private CustomerOrderDetailService customerOrderDetailService;

        @GrpcClient("java_elasticsearch")
        private CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub;

        @Override
        public void findOrderOnlineById(OrderOnlineFindOneRequest request,
                        StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;

                try {

                        CustomerOrder customerOrder = customerOrderService.findOne(request.getCustomerOrderId());

                        if (customerOrder == null) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService
                                        .findOnlineOrderDetailByOnlineOrderId(customerOrder.getId());

                        List<CustomerOrderOnlineDetailResponse> customerOrderOnlineDetailResponses = customerOrderDetails
                                        .stream()
                                        .filter(y -> y.getOrderDetailComboParentId() == 0
                                                        && y.getOrderDetailParentId() == 0)
                                        .map(x -> CustomerOrderOnlineDetailResponse.newBuilder()
                                                        .setId(x.getId())
                                                        .setCustomerOrderId(x.getCustomerOrderId())
                                                        .setFoodId(x.getFoodId())
                                                        .setFoodName(x.getFoodName())
                                                        .setOrderDetailParentId(x.getOrderDetailParentId())
                                                        .setOrderDetailComboParentId(x.getOrderDetailComboParentId())
                                                        .setCustomerOrderDetailAdditionIds(
                                                                        x.getCustomerOrderDetailAdditionIds())
                                                        .setCustomerOrderDetailComboIds(
                                                                        x.getCustomerOrderDetailComboIds())
                                                        .setQuantity(String.valueOf(x.getQuantity()))
                                                        .setPrice(String.valueOf(x.getPrice()))
                                                        .setTotalPrice(String.valueOf(x.getTotalPrice()))
                                                        .setIsAddition(x.getIsAddition())
                                                        .setIsCombo(x.getIsCombo())
                                                        .setNote(x.getNote())
                                                        .setCreatedAt(Utils.getDatetimeFormatVN(x.getCreatedAt()))
                                                        .setUpdatedAt(Utils.getDatetimeFormatVN(x.getUpdatedAt()))
                                                        .addAllCustomerOrderDetailAddition(
                                                                        x.getCustomerOrderDetailAdditionIds().isEmpty()
                                                                                        ? new ArrayList<>()
                                                                                        : customerOrderDetails.stream()
                                                                                                        .filter(f -> x.getId() == f
                                                                                                                        .getOrderDetailParentId())
                                                                                                        .map(ad -> FoodInAdditionResponse
                                                                                                                        .newBuilder()
                                                                                                                        .setId(ad.getId())
                                                                                                                        .setName(ad.getFoodName())
                                                                                                                        .setQuantity(String
                                                                                                                                        .valueOf(ad.getQuantity()))
                                                                                                                        .build())
                                                                                                        .toList())
                                                        .addAllCustomerOrderDetailCombo(
                                                                        x.getCustomerOrderDetailComboIds().isEmpty()
                                                                                        ? new ArrayList<>()
                                                                                        : customerOrderDetails.stream()
                                                                                                        .filter(f -> x.getId() == f
                                                                                                                        .getOrderDetailComboParentId())
                                                                                                        .map(ad -> FoodInAdditionResponse
                                                                                                                        .newBuilder()
                                                                                                                        .setId(ad.getId())
                                                                                                                        .setName(ad.getFoodName())
                                                                                                                        .setQuantity(String
                                                                                                                                        .valueOf(ad.getQuantity()))
                                                                                                                        .build())
                                                                                                        .toList())
                                                        .build())
                                        .toList();

                        CustomerOrderOnlineResponse customerOrderOnlineResponse = CustomerOrderOnlineResponse
                                        .newBuilder()
                                        .setId(customerOrder.getId())
                                        .setRestaurantId(customerOrder.getRestaurantId())
                                        .setRestaurantBrandId(customerOrder.getRestaurantBrandId())
                                        .setBranchId(customerOrder.getBranchId())
                                        .setOrderId(customerOrder.getOrderId())
                                        .setCustomerId(customerOrder.getCustomerId())
                                        .setCustomerName(customerOrder.getCustomerName())
                                        .setCustomerPhone(customerOrder.getCustomerPhone())
                                        .setCustomerAddress(customerOrder.getCustomerAddress())
                                        .setTotalAmount(String.valueOf(customerOrder.getTotalAmount()))
                                        .setPaymentMethod(customerOrder.getPaymentMethod())
                                        .setPaymentStatus(customerOrder.getPaymentStatus())
                                        .setCustomerOrderStatus(customerOrder.getCustomerOrderStatus())
                                        .setCreatedAt(Utils.getDatetimeFormatVN(customerOrder.getCreatedAt()))
                                        .setUpdatedAt(Utils.getDatetimeFormatVN(customerOrder.getUpdatedAt()))
                                        .addAllCustomerOrderDetails(customerOrderOnlineDetailResponses)
                                        .build();

                        response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.OK.value())
                                        .setMessage(HttpStatus.OK.name())//
                                        .setData(customerOrderOnlineResponse)
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage(e.getMessage())//
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }

        @Override
        public void getOrderOnlineByCustomerIdAndStatus(OrderOnlineRequest request,
                        StreamObserver<BaseListGRPCResponse> responseObserver) {
                BaseListGRPCResponse response;
                try {

                        // List<CustomerOrder> customerOrders =
                        // customerOrderService.getListByCustomerIdAndOnlineOrderStatus(request.getCustomerId(),
                        // Utils.convertStringToList(request.getCustomerOrderStatus()));

                        Pagination paginaton = new Pagination(request.getPage(), request.getLimit());

                        StoreProcedureResult<CustomerOrderAlolineModel> result = customerOrderService
                                        .spGetListCustomerOrderAloline(request.getRestaurantId(),
                                                        request.getRestaurantBrandId(),
                                                        request.getBranchId(), request.getCustomerId(),
                                                        Utils.convertStringToList(request.getCustomerOrderStatus()),
                                                        paginaton.getOffset(), paginaton.getLimit());

                        List<CustomerOrderOnlineResponse> customerOrderResponses = result.getResult().stream()
                                        .filter(o -> o.getPaymentMethod() == 0
                                                        || (o.getPaymentMethod() == 1 && o.getPaymentStatus() == 1))
                                        .map(x -> CustomerOrderOnlineResponse.newBuilder()
                                                        .setId(x.getId())
                                                        .setRestaurantId(x.getRestaurantId())
                                                        .setRestaurantBrandId(x.getRestaurantBrandId())
                                                        .setBranchId(x.getBranchId())
                                                        .setOrderId(x.getOrderId())
                                                        .setCustomerId(x.getCustomerId())
                                                        .setCustomerName(x.getCustomerName())
                                                        .setCustomerPhone(x.getCustomerPhone())
                                                        .setCustomerAddress(x.getCustomerAddress())
                                                        .setTotalAmount(String.valueOf(x.getTotalAmount()))
                                                        .setPaymentMethod(x.getPaymentMethod())
                                                        .setPaymentStatus(x.getPaymentStatus())
                                                        .setCustomerOrderStatus(x.getCustomerOrderStatus())
                                                        .setNote(x.getNote())
                                                        .setFoodCount(x.getFoodCount())
                                                        .setCreatedAt(Utils.getDatetimeFormatVN(x.getCreatedAt()))
                                                        .setUpdatedAt(Utils.getDatetimeFormatVN(x.getUpdatedAt()))
                                                        .build())
                                        .collect(Collectors.toList());

                        response = BaseListGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.OK.value())
                                        .setMessage(HttpStatus.OK.name())//
                                        .setTotalRecord((int) result.getTotalRecord())
                                        .setLimit(request.getLimit())
                                        .addAllCustomerOrderOnline(customerOrderResponses)
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        response = BaseListGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage(e.getMessage())//
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }

        @Override
        public void createOrderOnlineAloline(OrderOnlineCreateRequest request,
                        StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;

                try {

                        List<Integer> foodIds = request.getFoodsList().stream().flatMap(x -> Stream.concat(
                                        Stream.of(x.getId()),
                                        x.getAdditionFoodsList().stream().map(AdditionFoodRequest::getId))).toList();

                        List<FoodMenuResponse> foods = new FoodMenuResponse().mapToList(
                                        customerOrderMenuServiceBlockingStub.getCustomerOrderMenuByFoodIds(
                                                        CustomerOrderMenuByFoodIdsRequest.newBuilder()
                                                                        .setRestaurantId(request.getRestaurantId())
                                                                        .setRestaurantBrandId(
                                                                                        request.getRestaurantBrandId())
                                                                        .setBranchId(request.getBranchId())
                                                                        .setAreaId(-1).addAllFoodId(foodIds).build())
                                                        .getDataList());

                        if (foods.isEmpty()) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("Món ăn truyền vào có món không tìm thấy!")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        List<Integer> foodAdditionIds = request.getFoodsList().stream()
                                        .flatMap(food -> food.getAdditionFoodsList().stream())
                                        .map(AdditionFoodRequest::getId).toList();

                        if (!foodAdditionIds.isEmpty()) {
                                if (foods.stream().noneMatch(food -> foodAdditionIds.contains(food.getId()))) {
                                        response = BaseGRPCResponse.newBuilder()
                                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                                        .setMessage("Danh sách món bán kèm truyền vào có món không hợp lệ!")//
                                                        .build();
                                        responseObserver.onNext(response);
                                        responseObserver.onCompleted();
                                        return;
                                }
                        }

                        if (foods.stream()
                                        .noneMatch(x -> request.getFoodsList().stream().map(FoodRequest::getId).toList()
                                                        .contains(x.getId()))) {

                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("Món ăn truyền vào có món không tìm thấy!")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        if (CustomerOrderStatusEnum
                                        .checkCustomerOrderStatusEnum(request.getCustomerOrderStatus()) == false) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("Trạng thái truyền vào không hợp lệ!")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }
                        // List<CustomerOrderFoodCreateRequest> foodMain = new ArrayList<>();
                        //
                        //
                        //
                        // request.getFoodsList().forEach(x -> {
                        // List<CustomerOrderFoodCreateRequest> additions = new ArrayList<>();
                        //
                        // x.getAdditionFoodsList().forEach(k -> {
                        // CustomerOrderFoodCreateRequest addition = new
                        // CustomerOrderFoodCreateRequest();
                        // addition.setId(k.getId());
                        // addition.setQuantity(k.getQuantity());
                        // addition.setNote(k.getNode());
                        //
                        // additions.add(addition);
                        // });
                        //
                        // CustomerOrderFoodCreateRequest customerOrderFoodCreateRequest = new
                        // CustomerOrderFoodCreateRequest();
                        // customerOrderFoodCreateRequest.setId(x.getId());
                        // customerOrderFoodCreateRequest.setQuantity(x.getQuantity());
                        // customerOrderFoodCreateRequest.setNote(x.getNote());
                        // customerOrderFoodCreateRequest.setAdditionFoods(additions);
                        //
                        // foodMain.add(customerOrderFoodCreateRequest);
                        //
                        //
                        // });
                        //
                        // OnlineOrderCreateRequest wrapper = new OnlineOrderCreateRequest();
                        //
                        // wrapper.setRestaurantId(request.getRestaurantId());
                        // wrapper.setRestaurantBrandId(request.getRestaurantBrandId());
                        // wrapper.setBranchId(request.getBranchId());
                        // wrapper.setCustomerId(request.getCustomerId());
                        // wrapper.setCustomerName(request.getCustomerName());
                        // wrapper.setPhone(request.getPhone());
                        // wrapper.setAddress(request.getAddress());
                        // wrapper.setNote(request.getNote());
                        // wrapper.setFoods(foodMain);

                        OnlineOrderCreateRequest wrapper = new OnlineOrderCreateRequest();
                        wrapper.setRestaurantId(request.getRestaurantId());
                        wrapper.setRestaurantBrandId(request.getRestaurantBrandId());
                        wrapper.setBranchId(request.getBranchId());
                        wrapper.setCustomerName(request.getCustomerName());
                        wrapper.setPhone(request.getPhone());
                        wrapper.setAddress(request.getAddress());
                        wrapper.setNote(request.getNote());
                        

                        List<CustomerOrderFoodCreateRequest> foodMain = request.getFoodsList().stream()
                                        .map(food -> {
                                                List<CustomerOrderFoodCreateRequest> additions = food
                                                                .getAdditionFoodsList().stream()
                                                                .map(additionFood -> {
                                                                        CustomerOrderFoodCreateRequest addition = new CustomerOrderFoodCreateRequest();
                                                                        addition.setId(additionFood.getId());
                                                                        addition.setQuantity(new BigDecimal(
                                                                                        additionFood.getQuantity()));
                                                                        addition.setNote(additionFood.getNote());
                                                                        return addition;
                                                                })
                                                                .collect(Collectors.toList());

                                                CustomerOrderFoodCreateRequest customerOrderFood = new CustomerOrderFoodCreateRequest();
                                                customerOrderFood.setId(food.getId());
                                                customerOrderFood.setQuantity(new BigDecimal(food.getQuantity()));
                                                customerOrderFood.setNote(food.getNote());
                                                customerOrderFood.setAdditionFoods(additions);
                                                return customerOrderFood;
                                        })
                                        .collect(Collectors.toList());

                        wrapper.setFoods(foodMain);

                        CustomerOrder order = this.customerOrderService.spUCreateOnlineOrder(request.getRestaurantId(),
                                        request.getRestaurantBrandId(), request.getBranchId(), request.getCustomerId(),
                                        0,
                                        request.getCustomerName(), request.getPhone(), request.getAddress(),
                                        Utils.convertListToJson(wrapper.getFoods()), Utils.convertListToJson(foods),
                                        request.getNote(), request.getCustomerOrderStatus());

                        SocketService.addOrder(order);

                        CustomerOrderOnlineResponse customerOrderResponse = CustomerOrderOnlineResponse.newBuilder()
                                        .setId(order.getId())
                                        .setRestaurantId(order.getRestaurantId())
                                        .setRestaurantBrandId(order.getRestaurantBrandId())
                                        .setBranchId(order.getBranchId())
                                        .setOrderId(order.getOrderId())
                                        .setCustomerId(order.getCustomerId())
                                        .setCustomerName(order.getCustomerName())
                                        .setCustomerPhone(order.getCustomerPhone())
                                        .setCustomerAddress(order.getCustomerAddress())
                                        .setTotalAmount(String.valueOf(order.getTotalAmount()))
                                        .setPaymentMethod(order.getPaymentMethod())
                                        .setPaymentStatus(order.getPaymentStatus())
                                        .setCustomerOrderStatus(order.getCustomerOrderStatus())
                                        .setNote(order.getNote())
                                        .build();

                        response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.OK.value())
                                        .setMessage(HttpStatus.OK.name())//
                                        .setData(customerOrderResponse)
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage(e.getMessage())//
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }

        @Override
        public void cancelOrderOnlineAloline(OrderOnlineCancelRequest request,
                        StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;

                try {

                        CustomerOrder customerOrder = customerOrderService.findOne(request.getCustomerOrderId());

                        if (customerOrder == null) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }
                        if (customerOrder.getRestaurantId() != request.getRestaurantId() ||
                                        customerOrder.getRestaurantBrandId() != request.getRestaurantBrandId() ||
                                        customerOrder.getBranchId() != request.getBranchId() ||
                                        customerOrder.getCustomerId() != request.getCustomerId()) {

                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        if (customerOrder.getCustomerOrderStatus() != CustomerOrderStatusEnum.PENDING.getValue()) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("Không thể thao tác trên hoá đơn này.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        customerOrder.setCustomerOrderStatus(CustomerOrderStatusEnum.CUSTOMER_CANCEL.getValue());

                        CustomerOrderOnlineResponse customerOrderOnlineResponse = CustomerOrderOnlineResponse
                                        .newBuilder()
                                        .setId(customerOrder.getId())
                                        .setRestaurantId(customerOrder.getRestaurantId())
                                        .setRestaurantBrandId(customerOrder.getRestaurantBrandId())
                                        .setBranchId(customerOrder.getBranchId())
                                        .setOrderId(customerOrder.getOrderId())
                                        .setCustomerId(customerOrder.getCustomerId())
                                        .setCustomerName(customerOrder.getCustomerName())
                                        .setCustomerPhone(customerOrder.getCustomerPhone())
                                        .setCustomerAddress(customerOrder.getCustomerAddress())
                                        .setTotalAmount(String.valueOf(customerOrder.getTotalAmount()))
                                        .setPaymentMethod(customerOrder.getPaymentMethod())
                                        .setPaymentStatus(customerOrder.getPaymentStatus())
                                        .setCustomerOrderStatus(customerOrder.getCustomerOrderStatus())
                                        .setCreatedAt(Utils.getDatetimeFormatVN(customerOrder.getCreatedAt()))
                                        .setUpdatedAt(Utils.getDatetimeFormatVN(customerOrder.getUpdatedAt()))
                                        .build();

                        customerOrderService.update(customerOrder);

                        response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.OK.value())
                                        .setMessage(HttpStatus.OK.name())//
                                        .setData(customerOrderOnlineResponse)
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage(e.getMessage())//
                                        .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }

        @Override
        public void changeStatusOrderOnlineAloline(OrderOnlineCancelRequest request,
                                             StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;

                try {

                        CustomerOrder customerOrder = customerOrderService.findOne(request.getCustomerOrderId());

                        if (customerOrder == null) {
                                response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                        .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }
                        if (customerOrder.getRestaurantId() != request.getRestaurantId() ||
                                customerOrder.getRestaurantBrandId() != request.getRestaurantBrandId() ||
                                customerOrder.getBranchId() != request.getBranchId() ||
                                customerOrder.getCustomerId() != request.getCustomerId()) {

                                response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                        .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        if (customerOrder.getCustomerOrderStatus() != CustomerOrderStatusEnum.TEMP.getValue()) {
                                response = BaseGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.BAD_REQUEST.value())
                                        .setMessage("Không thể thao tác đơn này vì không phải đơn nháp.")//
                                        .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        customerOrder.setCustomerOrderStatus(CustomerOrderStatusEnum.PENDING.getValue());

                        CustomerOrderOnlineResponse customerOrderOnlineResponse = CustomerOrderOnlineResponse
                                .newBuilder()
                                .setId(customerOrder.getId())
                                .setRestaurantId(customerOrder.getRestaurantId())
                                .setRestaurantBrandId(customerOrder.getRestaurantBrandId())
                                .setBranchId(customerOrder.getBranchId())
                                .setOrderId(customerOrder.getOrderId())
                                .setCustomerId(customerOrder.getCustomerId())
                                .setCustomerName(customerOrder.getCustomerName())
                                .setCustomerPhone(customerOrder.getCustomerPhone())
                                .setCustomerAddress(customerOrder.getCustomerAddress())
                                .setTotalAmount(String.valueOf(customerOrder.getTotalAmount()))
                                .setPaymentMethod(customerOrder.getPaymentMethod())
                                .setPaymentStatus(customerOrder.getPaymentStatus())
                                .setCustomerOrderStatus(customerOrder.getCustomerOrderStatus())
                                .setCreatedAt(Utils.getDatetimeFormatVN(customerOrder.getCreatedAt()))
                                .setUpdatedAt(Utils.getDatetimeFormatVN(customerOrder.getUpdatedAt()))
                                .build();

                        customerOrderService.update(customerOrder);

                        response = BaseGRPCResponse.newBuilder()
                                .setStatus(HttpStatus.OK.value())
                                .setMessage(HttpStatus.OK.name())//
                                .setData(customerOrderOnlineResponse)
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        response = BaseGRPCResponse.newBuilder()
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setMessage(e.getMessage())//
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }

        @Override
        public void deleteOrderOnlineAloline(OrderOnlineDeleteRequest request,
                                             StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;

                try {

                        customerOrderDetailService.deleteCustomerOrderDetail(Utils.convertListObjectToJsonArray(request.getCustomerOrderIdsList()));
                        customerOrderService.deleteCustomerOrder(Utils.convertListObjectToJsonArray(request.getCustomerOrderIdsList()));

                        response = BaseGRPCResponse.newBuilder()
                                .setStatus(HttpStatus.OK.value())
                                .setMessage(HttpStatus.OK.name())//
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();

                } catch (Exception e) {
                        response = BaseGRPCResponse.newBuilder()
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setMessage(e.getMessage())//
                                .build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                }
        }
}
