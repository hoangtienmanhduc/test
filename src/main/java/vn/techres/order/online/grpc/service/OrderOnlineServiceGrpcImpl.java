package vn.techres.order.online.grpc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import vn.techres.microservice.grpc.java.customer_order_online.order_online.*;
import vn.techres.order.online.common.enums.CustomerOrderEnum;
import vn.techres.order.online.common.enums.CustomerOrderStatusEnum;
import vn.techres.order.online.common.enums.PaymentMethodEnum;
import vn.techres.order.online.common.enums.PaymentStatusEnum;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.service.CustomerOrderDetailService;
import vn.techres.order.online.v1.service.CustomerOrderService;

@GrpcService
public class OrderOnlineServiceGrpcImpl extends OrderOnlineServiceGrpc.OrderOnlineServiceImplBase {

        @Autowired
        private CustomerOrderService customerOrderOnlineService;

        @Autowired
        private CustomerOrderDetailService customerOrderOnlineDetailService;

        @Override
        public void updateCustomerOrderOnlineByTransactionId(CustomerOrderOnlineByTransactionIdRequest request,
                        StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;

                try {

                        if (StringUtil.isNullOrEmpty(request.getTransactionId())
                                        || Long.parseLong(request.getTransactionId()) <= 0) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("transaction_id truyền vào không được phép truyền null hoặc rỗng và phải > 0 ")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        CustomerOrder customerOrderOnline = customerOrderOnlineService
                                        .findByTransactionId(Long.parseLong(request.getTransactionId()));

                        if (customerOrderOnline == null ||
                                        (customerOrderOnline
                                                        .getPaymentMethod() != PaymentMethodEnum.PREPAYMENT.getValue()
                                                        &&
                                                        customerOrderOnline
                                                                        .getPaymentStatus() != PaymentStatusEnum.WAITTING_PAYMENT
                                                                                        .getValue()
                                                        &&
                                                        customerOrderOnline
                                                                        .getCustomerOrderStatus() != CustomerOrderEnum.PENDING
                                                                                        .getValue())) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("transaction_id truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        if (customerOrderOnline.getPaymentStatus() == PaymentStatusEnum.WAITTING_PAYMENT.getValue()) {
                                customerOrderOnline.setPaymentStatus(PaymentStatusEnum.PAID.getValue());
                                customerOrderOnlineService.update(customerOrderOnline);
                        }

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

        @Override
        public void findCustomerOrderOnlineById(CustomerOrderRequest request,
                        StreamObserver<BaseListGRPCResponse> responseObserver) {
                BaseListGRPCResponse response;

                try {

                        CustomerOrder orderOnline = customerOrderOnlineService.findOne(request.getCustomerOrderId());

                        if (orderOnline == null ||
                                        orderOnline.getCustomerOrderStatus() != CustomerOrderEnum.PENDING.getValue() ||
                                        (orderOnline.getPaymentMethod() == PaymentMethodEnum.PREPAYMENT.getValue()
                                                        && orderOnline.getPaymentStatus() != PaymentStatusEnum.PAID
                                                                        .getValue())

                        ) {
                                response = BaseListGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        CustomerOrderOnlineResponse customerOrderOnlineResponse = CustomerOrderOnlineResponse
                                        .newBuilder()
                                        .setId(orderOnline.getId())
                                        .setRestaurantId(orderOnline.getRestaurantId())
                                        .setRestaurantBrandId(orderOnline.getRestaurantBrandId())
                                        .setBranchId(orderOnline.getBranchId())
                                        .setOrderId(orderOnline.getOrderId())
                                        .setTransactionId(String.valueOf(orderOnline.getTransactionId()))
                                        .setCustomerName(orderOnline.getCustomerName())
                                        .setCustomerPhone(orderOnline.getCustomerPhone())
                                        .setCustomerAddress(orderOnline.getCustomerAddress())
                                        .setTotalAmount(String.valueOf(orderOnline.getTotalAmount()))
                                        .setPaymentMethod(orderOnline.getPaymentMethod())
                                        .setPaymentStatus(orderOnline.getPaymentStatus())
                                        .setCustomerOrderStatus(orderOnline.getCustomerOrderStatus())
                                        .setNote(orderOnline.getNote() == null ? "" : orderOnline.getNote())
                                        .build();

                        List<CustomerOrderDetail> orderOnlineDetails = customerOrderOnlineDetailService
                                        .findByCustomerOrderOnlineId(request.getCustomerOrderId());

                        List<CustomerOrderOnlineDetailResponse> customerOrderOnlineDetailResponses = orderOnlineDetails
                                        .stream()
                                        .map(x -> CustomerOrderOnlineDetailResponse.newBuilder()
                                                        .setId(x.getId())
                                                        .setCustomerOrderId(x.getCustomerOrderId())
                                                        .setFoodId(x.getFoodId())
                                                        .setFoodName(x.getFoodName() == null ? "" : x.getFoodName())
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
                                                        .setNote(x.getNote() == null ? "" : x.getNote())
                                                        .build())
                                        .toList();

                        response = BaseListGRPCResponse.newBuilder()
                                        .setStatus(HttpStatus.OK.value())
                                        .setMessage(HttpStatus.OK.name())
                                        .setCustomerOrderOnline(customerOrderOnlineResponse)
                                        .addAllCustomerOrderOnlineDetail(customerOrderOnlineDetailResponses)
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
        public void updateCustomerOrderOnlineStatusEnumAndOrderId(OrderRequest request,
                        StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;
                try {
                        CustomerOrder orderOnline = customerOrderOnlineService.findOne(request.getCustomerOrderId());

                        if (orderOnline == null || orderOnline.getCustomerOrderStatus() != CustomerOrderEnum.PENDING
                                        .getValue()) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        if (!CustomerOrderStatusEnum.checkCustomerOrderStatusEnum(request.getCustomerOrderStatus())) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("Trạng thái truyền vào không hợp lệ.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        orderOnline.setOrderId(request.getOrderId());
                        orderOnline.setCustomerOrderStatus(request.getCustomerOrderStatus());

                        customerOrderOnlineService.update(orderOnline);

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

        @Override
        public void updateCustomerOrderOnlineByOrderId(UpdateCustomerOrderOnlineByOrderIdRequest request,
                        StreamObserver<BaseGRPCResponse> responseObserver) {
                BaseGRPCResponse response;
                try {
                        CustomerOrder orderOnline = customerOrderOnlineService.findOne(request.getOrderId());

                        if (orderOnline == null || orderOnline.getCustomerOrderStatus() != CustomerOrderEnum.PENDING
                                        .getValue()) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("ID đơn hàng truyền vào không tìm thấy.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        if (!CustomerOrderStatusEnum.checkCustomerOrderStatusEnum(request.getCustomerOrderStatus())) {
                                response = BaseGRPCResponse.newBuilder()
                                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                                .setMessage("Trạng thái truyền vào không hợp lệ.")//
                                                .build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                                return;
                        }

                        orderOnline.setOrderId(request.getOrderId());
                        // orderOnline.setCustomerOrderStatus(request.getCustomerOrderStatus());

                        customerOrderOnlineService.update(orderOnline);

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
