package vn.techres.order.online.common.utils;

import java.util.List;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuByFoodIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuByFoodIdsRequest;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuListResponses;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuRequest;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuResponse;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.FoodMenuResponse;
import vn.techres.microservice.grpc.java.elasticsearch.order_online_qr.BaseGRPCResponse;
import vn.techres.microservice.grpc.java.elasticsearch.order_online_qr.OrderOnlineQRRequest;
import vn.techres.microservice.grpc.java.elasticsearch.order_online_qr.OrderOnlineQRServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandFindByIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandResponse;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.restaurants.RestaurantByIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.restaurants.RestaurantResponse;
import vn.techres.microservice.grpc.java.elasticsearch.restaurants.RestaurantServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.table.TableFindByIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.table.TableResponse;
import vn.techres.microservice.grpc.java.elasticsearch.table.TableServiceGrpc;
import vn.techres.microservice.grpc.java.order.restaurant_order_payment_transaction.GetQrBaseResponse;
import vn.techres.microservice.grpc.java.order.restaurant_order_payment_transaction.GetQrRequest;
import vn.techres.microservice.grpc.java.order.restaurant_order_payment_transaction.GetQrResponse;
import vn.techres.microservice.grpc.java.order.restaurant_order_payment_transaction.RestaurantOrderPaymentTransactionGrpcServiceGrpc;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.request.OnlineOrderCreateRequest;
import vn.techres.order.online.v1.request.OnlineOrderQRCodeRequest;

public class GrpcRetry {

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public CustomerOrderMenuListResponses getMenu(
			CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub,
			CustomerOrderMenuRequest getMenu) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 getCustomerOrderMenu");
			return customerOrderMenuServiceBlockingStub.getCustomerOrderMenu(getMenu).getData();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 getCustomerOrderMenu");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public CustomerOrderMenuResponse getCustomerOrderMenuByFoodId(
			CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub,
			CustomerOrderMenuByFoodIdRequest request) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 getCustomerOrderMenuByFoodId");
			return customerOrderMenuServiceBlockingStub.getCustomerOrderMenuByFoodId(request).getData();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 getCustomerOrderMenuByFoodId");
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public RestaurantBrandResponse findById(
			RestaurantBrandServiceGrpc.RestaurantBrandServiceBlockingStub restaurantBrandServiceBlockingStub,
			OnlineOrderCreateRequest wrapper) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 findById");
			return restaurantBrandServiceBlockingStub.findById(RestaurantBrandFindByIdRequest.newBuilder()
					.setRestaurantBrandId(wrapper.getRestaurantBrandId()).build()).getData();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 findById");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public List<FoodMenuResponse> getCustomerOrderMenuByFoodIds(
			CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub,
			OnlineOrderCreateRequest wrapper, List<Integer> foodIds) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 getCustomerOrderMenuByFoodIds");
			return customerOrderMenuServiceBlockingStub.getCustomerOrderMenuByFoodIds(

					CustomerOrderMenuByFoodIdsRequest.newBuilder().setRestaurantId(wrapper.getRestaurantId())
							.setRestaurantBrandId(wrapper.getRestaurantBrandId()).setBranchId(wrapper.getBranchId())
							.setAreaId(-1).addAllFoodId(foodIds).build()

			).getDataList();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 getCustomerOrderMenuByFoodIds");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public BaseGRPCResponse findByRestaurantId(
			OrderOnlineQRServiceGrpc.OrderOnlineQRServiceBlockingStub orderOnlineQRServiceBlockingStub,
			OnlineOrderQRCodeRequest wrapper, int restaurantId) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 findByRestaurantId");
			return orderOnlineQRServiceBlockingStub.findByRestaurantId(

					OrderOnlineQRRequest.newBuilder().setRestaurantId(restaurantId)
							.setLat(String.valueOf(wrapper.getLat())).setLng(String.valueOf(wrapper.getLng())).build());

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 findByRestaurantId");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public GetQrBaseResponse getQr(
			RestaurantOrderPaymentTransactionGrpcServiceGrpc.RestaurantOrderPaymentTransactionGrpcServiceBlockingStub restaurantOrderPaymentTransactionGrpcServiceBlockingStub,
			CustomerOrder order) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 getQr");
			return restaurantOrderPaymentTransactionGrpcServiceBlockingStub
					.getQR(GetQrRequest.newBuilder().setRestaurantId(order.getRestaurantId())
							.setRestaurantBrandId(order.getRestaurantBrandId())
							.setAmount(String.valueOf(order.getTotalAmount())).setRestaurantTransactionId(0).build());

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 getQr");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public RestaurantBrandResponse restaurantBrandFindById(
			RestaurantBrandServiceGrpc.RestaurantBrandServiceBlockingStub restaurantBrandServiceBlockingStub,
			RestaurantBrandFindByIdRequest restaurantBrandFindByIdRequest) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 restaurantBrandFindById");
			return restaurantBrandServiceBlockingStub
					.findById(restaurantBrandFindByIdRequest).getData();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 restaurantBrandFindById");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public RestaurantResponse restaurantFindById(
			RestaurantServiceGrpc.RestaurantServiceBlockingStub restaurantServiceBlockingStub,
			RestaurantByIdRequest restaurantByIdRequest) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 restaurantFindById");
			return restaurantServiceBlockingStub
					.findById(restaurantByIdRequest).getData();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 restaurantFindById");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}


	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public TableResponse tableFindById(
			TableServiceGrpc.TableServiceBlockingStub tableServiceBlockingStub,
			TableFindByIdRequest tableFindByIdRequest) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 tableFindById");
			return tableServiceBlockingStub
					.findById(tableFindByIdRequest).getData();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 tableFindById");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public List<FoodMenuResponse> getCustomerOrderMenuByFoodIds(
			CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub,
			CustomerOrderMenuByFoodIdsRequest customerOrderMenuByFoodIdsRequest) {
		try {

			System.out.println("ĐÃ GỌI LẦN 1 getCustomerOrderMenuByFoodIds");
			return customerOrderMenuServiceBlockingStub
					.getCustomerOrderMenuByFoodIds(customerOrderMenuByFoodIdsRequest).getDataList();

		} catch (Exception e) {
			System.err.println("ĐÃ Bị Lỗi GỌI LẦN 1 getCustomerOrderMenuByFoodIds");
			// Xử lý exception, hoặc để nó propagate lên để Spring Retry thực hiện retry
			System.out.println("Exception caught: " + e.getMessage());
		}
		return null;
	}

}
