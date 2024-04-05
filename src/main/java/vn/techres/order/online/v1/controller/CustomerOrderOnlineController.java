package vn.techres.order.online.v1.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import net.devh.boot.grpc.client.inject.GrpcClient;
import vn.techres.microservice.grpc.java.elasticsearch.branches.BranchServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.order_online_qr.BaseGRPCResponse;
import vn.techres.microservice.grpc.java.elasticsearch.order_online_qr.OrderOnlineQRServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandResponse;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandServiceGrpc;
import vn.techres.microservice.grpc.java.order.restaurant_order_payment_transaction.GetQrBaseResponse;
import vn.techres.microservice.grpc.java.order.restaurant_order_payment_transaction.RestaurantOrderPaymentTransactionGrpcServiceGrpc;
import vn.techres.order.online.common.enums.CustomerOrderStatusEnum;
import vn.techres.order.online.common.enums.PaymentMethodEnum;
import vn.techres.order.online.common.exception.TechResHttpException;
import vn.techres.order.online.common.helper.SocketService;
import vn.techres.order.online.common.utils.GrpcRetry;
import vn.techres.order.online.common.utils.Utils;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.entityJson.OnlineOrderQRCodeJson;
import vn.techres.order.online.v1.request.ChangeInfoCustomerRequest;
import vn.techres.order.online.v1.request.CustomerOrderFoodCreateRequest;
import vn.techres.order.online.v1.request.OnlineOrderCancelRequest;
import vn.techres.order.online.v1.request.OnlineOrderCreateRequest;
import vn.techres.order.online.v1.request.OnlineOrderQRCodeRequest;
import vn.techres.order.online.v1.response.BaseResponse;
import vn.techres.order.online.v1.response.CustomerOrderResponse;
import vn.techres.order.online.v1.response.FoodInAdditionResponse;
import vn.techres.order.online.v1.response.FoodInComboResponse;
import vn.techres.order.online.v1.response.FoodMenuResponse;
import vn.techres.order.online.v1.response.QRResponse;
import vn.techres.order.online.v1.response.RestaurantResponse;
import vn.techres.order.online.v1.service.CustomerOrderDetailService;
import vn.techres.order.online.v1.service.CustomerOrderService;
import vn.techres.order.online.v1.version.VersionService;

@RestController("CustomerOrderOnlineController" + VersionService.VERSION)
@RequestMapping(value = VersionService.VERSION_PATH + "customer-order-online")
public class CustomerOrderOnlineController extends BaseController {

	@GrpcClient("java_elasticsearch")
	private RestaurantBrandServiceGrpc.RestaurantBrandServiceBlockingStub restaurantBrandServiceBlockingStub;

	@GrpcClient("java_elasticsearch")
	private BranchServiceGrpc.BranchServiceBlockingStub branchServiceBlockingStub;

	@GrpcClient("java_elasticsearch")
	private CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub;

	@GrpcClient("java_elasticsearch")
	private OrderOnlineQRServiceGrpc.OrderOnlineQRServiceBlockingStub orderOnlineQRServiceBlockingStub;

	@Autowired
	private CustomerOrderService customerOrderService;

	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;

	@GrpcClient("java_order_lv3")
	private RestaurantOrderPaymentTransactionGrpcServiceGrpc.RestaurantOrderPaymentTransactionGrpcServiceBlockingStub restaurantOrderPaymentTransactionGrpcServiceBlockingStub;

	@Operation(summary = "API Lấy danh sách bill", description = "API Lấy danh sách bill")
	@Parameter(in = ParameterIn.QUERY, name = "restaurant_id", description = "Id nhà hàng, truyền phải lớn hơn 0")
	@Parameter(in = ParameterIn.QUERY, name = "restaurant_brand_id", description = "Id thương hiệu, truyền phải lớn hơn 0")
	@Parameter(in = ParameterIn.QUERY, name = "branch_id", description = "Id chi nhánh, truyền phải lớn hơn 0")
	@Parameter(in = ParameterIn.QUERY, name = "customer_order_status", description = "Trạng thái đơn hàng ,mặc định là rỗng")
	@Parameter(in = ParameterIn.QUERY, name = "customer_order_type", description = "Hình thức order ,mặc định là -1")
	@GetMapping(value = "", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<List<CustomerOrderResponse>>> getList(
			@Valid @Min(value = 1, message = "Nhà hàng phải lớn hơn 0") @RequestParam(name = "restaurant_id", required = true) int restaurantId,
			@Valid @Min(value = 1, message = "Thương hiệu phải lớn hơn 0") @RequestParam(name = "restaurant_brand_id", required = true) int restaurantBrandId,
			@Valid @Min(value = 1, message = "Chi nhánh phải lớn hơn 0") @RequestParam(name = "branch_id", required = true) int branchId,
			// @RequestParam(name = "area_id", required = false, defaultValue = "-1") int
			// areaId,
			// @RequestParam(name = "table_id", required = false, defaultValue = "-1") int
			// tableId,
			@Valid @Max(value = 1, message = "customerOrderType lớn nhất 1") @RequestParam(name = "customer_order_type", required = false, defaultValue = "-1") int customerOrderType,
			@RequestParam(name = "customer_order_status", required = false, defaultValue = "") String orderStatus)
			throws Exception {

		BaseResponse<List<CustomerOrderResponse>> response = new BaseResponse<>();

		List<CustomerOrderResponse> onlineOrderResponses = new CustomerOrderResponse().mapToList(
				customerOrderService.getListByRestaurantIdAndRestaurantBrandIdAndBranchIdAndOnlineOrderStatus(
						restaurantId, restaurantBrandId, branchId, -1, -1, customerOrderType,
						Utils.convertStringToList(orderStatus)));

		onlineOrderResponses = onlineOrderResponses.stream()
				.filter(o -> o.getPaymentMethod() == 0 || (o.getPaymentMethod() == 1 && o.getPaymentStatus() == 1))
				.toList();

		response.setData(onlineOrderResponses);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// @Operation(summary = "API Lấy danh sách bill Aloline", description = "API Lấy
	// danh sách bill Aloline")
	// @Parameter(in = ParameterIn.QUERY, name = "customer_id", description = "Id
	// khách hàng, truyền phải lớn hơn 0")
	// @Parameter(in = ParameterIn.QUERY, name = "customer_order_status",
	// description = "Trạng thái đơn hàng ,mặc định là rỗng")
	// @GetMapping(value = "/aloline", consumes = { MediaType.ALL_VALUE }, produces
	// = { MediaType.APPLICATION_JSON_VALUE })
	// @ResponseBody
	// public ResponseEntity<BaseResponse<List<CustomerOrderResponse>>>
	// getListByCustomer(
	// @Valid @Min(value = 1, message = "Id khách hàng phải lớn hơn 0")
	// @RequestParam(name = "customer_id", required = true) int customerId,
	// @RequestParam(name = "customer_order_status", required = false, defaultValue
	// = "") String orderStatus)
	// throws Exception {
	//
	// BaseResponse<List<CustomerOrderResponse>> response = new BaseResponse<>();
	//
	// List<CustomerOrderResponse> onlineOrderResponses = new
	// CustomerOrderResponse().mapToList(
	// customerOrderService.getListByCustomerIdAndOnlineOrderStatus(
	// customerId, Utils.convertStringToList(orderStatus)));
	//
	// onlineOrderResponses = onlineOrderResponses.stream()
	// .filter(o -> o.getPaymentMethod() == 0 || (o.getPaymentMethod() == 1 &&
	// o.getPaymentStatus() == 1))
	// .toList();
	//
	// response.setData(onlineOrderResponses);
	//
	// return new ResponseEntity<>(response, HttpStatus.OK);
	// }

	@Operation(summary = "API Lấy chi tiết bill", description = "API Lấy chi tiết bill")
	@GetMapping(value = "/{id}", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<CustomerOrderResponse>> getDetail(@PathVariable("id") int id) throws Exception {
		BaseResponse<CustomerOrderResponse> response = new BaseResponse<>();

		CustomerOrder customerOrder = customerOrderService.findOne(id);

		if (customerOrder == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại vui lòng kiểm tra lại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		List<CustomerOrderDetail> customerOrderDetails = customerOrderDetailService
				.findOnlineOrderDetailByOnlineOrderId(customerOrder.getId());

		List<CustomerOrderDetail> children = customerOrderDetails.stream()
				.filter(x -> x.getOrderDetailParentId() > 0 || x.getOrderDetailComboParentId() > 0).toList();

		CustomerOrderResponse req = new CustomerOrderResponse(customerOrder, customerOrderDetails.stream()
				.filter(x -> x.getOrderDetailComboParentId() == 0 && x.getOrderDetailParentId() == 0).toList());

		req.getCustomerOrderDetails().stream()
				.filter(x -> x.getOrderDetailComboParentId() == 0 && x.getOrderDetailParentId() == 0)
				.map(a -> {
					if (!a.getCustomerOrderDetailAdditionIds().isEmpty()) {
						List<FoodInAdditionResponse> additionFoods = children.stream()
								.filter(f -> a.getId() == f.getOrderDetailParentId())
								.map(FoodInAdditionResponse::new).toList();
						a.setCustomerOrderDetailAddition(additionFoods);
						return a;
					}

					if (!a.getCustomerOrderDetailComboIds().isEmpty()) {
						List<FoodInComboResponse> foodInComboResponses = customerOrderDetails.stream()
								.filter(c -> a.getId() == c.getOrderDetailComboParentId()).map(FoodInComboResponse::new)
								.toList();
						a.setCustomerOrderDetailCombos(foodInComboResponses);
						return a;
					}
					return a;
				})
				.toList();

		response.setData(req);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "API khách hàng hủy món", description = "API khách hàng hủy món")
	@PostMapping(value = "{id}/cancel-online-order-detail", consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BaseResponse<?>> deleteCustomerOrderDetail(@PathVariable("id") long id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody OnlineOrderCancelRequest wrapper)
			throws Exception {

		BaseResponse<?> response = new BaseResponse<>();

		CustomerOrderDetail customerOrderDetail = customerOrderDetailService.findOne(id);

		if (customerOrderDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Chi tiết hóa đơn không tồn tại!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		CustomerOrder customerOrder = customerOrderService.findOne(wrapper.getOnlineOrderId());

		if (customerOrder == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại vui lòng kiểm tra lại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.CUSTOMER_CANCEL.getValue()
				|| customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.COMPLETE.getValue()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Bill này đã đóng không thể thao tác trên hóa đơn này!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrder.getBranchId() != wrapper.getBranchId()
				|| customerOrder.getRestaurantId() != wrapper.getRestaurantId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại trên chi nhánh này");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrderDetail.getCustomerOrderId() != customerOrder.getId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Chi tiết hóa đơn không truyền vào không hợp lệ!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		customerOrderDetailService.delete(customerOrderDetail);

		customerOrderService.spCalculateOrderTotalAmount(customerOrder.getId());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "API tạo đơn hàng", description = "")
	@PostMapping(value = "/create", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@Transactional(readOnly = true)
	public ResponseEntity<BaseResponse<QRResponse>> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody OnlineOrderCreateRequest wrapper)
			throws Exception {

		BaseResponse<QRResponse> response = new BaseResponse<>();

		if (wrapper.getPaymentMethod() == PaymentMethodEnum.PREPAYMENT.getValue()) {

			RestaurantBrandResponse restaurantBrandResponse = new GrpcRetry()
					.findById(restaurantBrandServiceBlockingStub, wrapper);

			if (restaurantBrandResponse.getSetting().getPaymentType() == 0) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("Tính năng thanh toán online hiện đang tắt!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		}

		List<Integer> foodIds = wrapper.getFoods().stream().flatMap(x -> Stream.concat(Stream.of(x.getId()),
				x.getAdditionFoods().stream().map(CustomerOrderFoodCreateRequest::getId))).toList();

		List<FoodMenuResponse> foods = new FoodMenuResponse().mapToList(
				new GrpcRetry().getCustomerOrderMenuByFoodIds(customerOrderMenuServiceBlockingStub, wrapper, foodIds));

		if (foods.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Món ăn truyền vào có món không tìm thấy!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		List<Integer> foodAdditionIds = wrapper.getFoods().stream().flatMap(food -> food.getAdditionFoods().stream())
				.map(CustomerOrderFoodCreateRequest::getId).toList();

		if (!foodAdditionIds.isEmpty()) {
			if (foods.stream().noneMatch(food -> foodAdditionIds.contains(food.getId()))) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("Danh sách món bán kèm truyền vào có món không hợp lệ!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

		if (foods.stream()
				.noneMatch(x -> wrapper.getFoods().stream().map(CustomerOrderFoodCreateRequest::getId).toList()
						.contains(x.getId()))) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Món ăn truyền vào có món không tìm thấy!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		CustomerOrder order = this.customerOrderService.spUCreateOnlineOrder(wrapper.getRestaurantId(),
				wrapper.getRestaurantBrandId(), wrapper.getBranchId(), 0, wrapper.getPaymentMethod(),
				wrapper.getCustomerName(), wrapper.getPhone(), wrapper.getAddress(),
				Utils.convertListToJson(wrapper.getFoods()), Utils.convertListToJson(foods), wrapper.getNote(), wrapper.getCustomerOrderStatus());

		if (order != null && wrapper.getPaymentMethod() == 1 && order.getPaymentMethod() == 1) {

			GetQrBaseResponse getQrBaseResponse = new GrpcRetry()
					.getQr(restaurantOrderPaymentTransactionGrpcServiceBlockingStub, order);

			if (getQrBaseResponse.getStatus() != HttpStatus.OK.value()) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError(getQrBaseResponse.getMessage());
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			order.setTransactionId(Long.parseLong(getQrBaseResponse.getData().getTransId()));

			customerOrderService.update(order);

			response.setData(new QRResponse(getQrBaseResponse.getData()));

			return new ResponseEntity<>(response, HttpStatus.OK);

		}

		SocketService.addOrder(order);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "API quét mã QR code", description = " ")
	@PostMapping(value = "/qr-code", consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<RestaurantResponse>> qrCode(
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody OnlineOrderQRCodeRequest wrapper)
			throws Exception {
		BaseResponse<RestaurantResponse> response = new BaseResponse<>();

		String code = Utils.decodeBase64String(wrapper.getQrCode());

		if (code.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Mã QR truyền vào phải là chuổi đã được mã hóa!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!this.checkFormatQRCode(code)) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Mã QR truyền vào không hợp lệ vui lòng kiểm tra lại!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		OnlineOrderQRCodeJson onlineOrderQRCodeJson = this.validateQRCode(code);

		BaseGRPCResponse baseGRPCResponse = new GrpcRetry().findByRestaurantId(orderOnlineQRServiceBlockingStub,
				wrapper, onlineOrderQRCodeJson.getRestaurantId());

		if (baseGRPCResponse.getStatus() != 200) {
			response.setStatus(HttpStatus.BAD_GATEWAY);
			response.setMessageError("Yêu cầu thất bại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setData(new RestaurantResponse(baseGRPCResponse.getData()));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "API chỉnh sửa thông tin khách hàng", description = "API chỉnh sửa thông tin khách hàng")
	@PostMapping(value = "/change-info-customer/{id}", consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<CustomerOrderResponse>> editInfoCustomer(@PathVariable("id") int id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody ChangeInfoCustomerRequest wrapper)
			throws Exception {
		BaseResponse<CustomerOrderResponse> response = new BaseResponse<>();

		CustomerOrder customerOrder = customerOrderService.findOne(id);

		if (customerOrder == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại vui lòng kiểm tra lại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.RESTAURANT_CANCEL.getValue()
				|| customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.CUSTOMER_CANCEL.getValue()
				|| customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.COMPLETE.getValue()) {

			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Không thể thao tác trên bill này");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrder.getBranchId() != wrapper.getBranchId()
				|| customerOrder.getRestaurantBrandId() != wrapper.getRestaurantBrandId()
				|| customerOrder.getRestaurantId() != wrapper.getRestaurantId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tìm thấy vui lòng kiểm tra lại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		customerOrder.setCustomerName(wrapper.getCustomerName());
		customerOrder.setCustomerPhone(wrapper.getPhone());
		customerOrder.setCustomerAddress(wrapper.getAddress());

		customerOrderService.update(customerOrder);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
