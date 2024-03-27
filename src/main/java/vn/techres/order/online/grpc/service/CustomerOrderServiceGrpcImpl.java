package vn.techres.order.online.grpc.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import vn.techres.microservice.grpc.java.customer_order_online.customer_order.*;
import vn.techres.order.online.common.enums.CustomerOrderStatusEnum;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.service.CustomerOrderDetailService;
import vn.techres.order.online.v1.service.CustomerOrderService;

import java.util.List;

@GrpcService
public class CustomerOrderServiceGrpcImpl extends CustomerOrderServiceGrpc.CustomerOrderServiceImplBase {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private CustomerOrderDetailService customerOrderDetailService;


    @Override
    public void updateCustomerOrderStatusEnumAndOrderId(OrderRequest request, StreamObserver<BaseGRPCResponse> responseObserver) {

        BaseGRPCResponse response;
        try {

            CustomerOrder customerOrder = customerOrderService.findOne(request.getCustomerOrderId());

            if(customerOrder == null || customerOrder.getCustomerOrderStatus() != CustomerOrderStatusEnum.PENDING.getValue()) {
                response = BaseGRPCResponse.newBuilder()
                        .setStatus(HttpStatus.BAD_REQUEST.value())//
                        .setMessage("Không tìm thấy thông tin bill!")//
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            customerOrder.setOrderId(request.getOrderId());
            customerOrder.setCustomerOrderStatus(CustomerOrderStatusEnum.COMPLETE.getValue());

            customerOrderService.update(customerOrder);

            CustomerOrderResponse customerOrderResponse = CustomerOrderResponse.newBuilder()
                    .setId(customerOrder.getId())
                    .setRestaurantId(customerOrder.getRestaurantId())
                    .setRestaurantBrandId(customerOrder.getRestaurantBrandId())
                    .setBranchId(customerOrder.getBranchId())
                    .setOrderId(customerOrder.getOrderId())
                    .setAreaId(customerOrder.getAreaId())
                    .setTableId(customerOrder.getTableId())
                    .setTotalAmount(String.valueOf(customerOrder.getTotalAmount()))
                    .setNote(customerOrder.getNote() == null ? "": customerOrder.getNote())
                    .setCustomerOrderStatus(customerOrder.getCustomerOrderStatus())
                    .build();

            response = BaseGRPCResponse.newBuilder()
                    .setStatus(HttpStatus.OK.value())//
                    .setMessage("Thành công")//
                    .setData(customerOrderResponse)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();


        } catch(Exception e) {
            response = BaseGRPCResponse.newBuilder()
                    .setStatus(HttpStatus.BAD_REQUEST.value())//
                    .setMessage(e.getMessage())//
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }


    @Override
    public void findCustomerOrderById(CustomerOrderRequest request, StreamObserver<BaseListGRPCResponse> responseObserver) {
        BaseListGRPCResponse response = null;

        try {

            CustomerOrder customerOrder = customerOrderService.findOne(request.getCustomerOrderId());

            if(customerOrder == null) {

                response = BaseListGRPCResponse.newBuilder()
                        .setStatus(HttpStatus.BAD_REQUEST.value())//
                        .setMessage("Không tìm thấy thông tin bill!")//
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            CustomerOrderResponse customerOrderResponse = CustomerOrderResponse.newBuilder()
                    .setId(customerOrder.getId())
                    .setRestaurantId(customerOrder.getRestaurantId())
                    .setRestaurantBrandId(customerOrder.getRestaurantBrandId())
                    .setBranchId(customerOrder.getBranchId())
                    .setOrderId(customerOrder.getOrderId())
                    .setAreaId(customerOrder.getAreaId())
                    .setTableId(customerOrder.getTableId())
                    .setTotalAmount(String.valueOf(customerOrder.getTotalAmount()))
                    .setNote(customerOrder.getNote() == null ? "": customerOrder.getNote())
                    .setCustomerOrderStatus(customerOrder.getCustomerOrderStatus())
                    .build();



            List<CustomerOrderDetail> customerOrderDetailList =
                    customerOrderDetailService.findByCustomerOrderOnlineId(request.getCustomerOrderId());


            List<CustomerOrderDetailResponse> customerOrderDetailResponses =
                    customerOrderDetailList.stream().map(x -> CustomerOrderDetailResponse.newBuilder()
                            .setId(x.getId())
                            .setCustomerOrderId(x.getCustomerOrderId())
                            .setFoodId(x.getFoodId())
                            .setFoodName(x.getFoodName() == null ? "": x.getFoodName())
                            .setOrderDetailParentId(x.getOrderDetailParentId())
                            .setOrderDetailComboParentId(x.getOrderDetailComboParentId())
                            .setCustomerOrderDetailAdditionIds(x.getCustomerOrderDetailAdditionIds())
                            .setCustomerOrderDetailComboIds(x.getCustomerOrderDetailComboIds())
                            .setQuantity(String.valueOf(x.getQuantity()))
                            .setPrice(String.valueOf(x.getPrice()))
                            .setTotalPrice(String.valueOf(x.getTotalPrice()))
                            .setIsAddition(x.getIsAddition())
                            .setIsCombo(x.getIsCombo())
                            .setNote(x.getNote() == null ? "" : x.getNote())
                            .build()).toList();


            response = BaseListGRPCResponse.newBuilder()
                    .setStatus(HttpStatus.OK.value())//
                    .setCustomerOrder(customerOrderResponse)
                    .addAllCustomerOrderDetail(customerOrderDetailResponses)
                    .setMessage("Thành công")//
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch(Exception e) {
            response = BaseListGRPCResponse.newBuilder()
                    .setStatus(HttpStatus.BAD_REQUEST.value())//
                    .setMessage(e.getMessage())//
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    }


}

