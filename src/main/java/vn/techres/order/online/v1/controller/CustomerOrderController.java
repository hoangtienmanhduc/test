package vn.techres.order.online.v1.controller;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import net.devh.boot.grpc.client.inject.GrpcClient;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuByFoodIdsRequest;
import vn.techres.microservice.grpc.java.elasticsearch.customer_order_menu.CustomerOrderMenuServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandFindByIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.restaurants.RestaurantByIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.restaurants.RestaurantServiceGrpc;
import vn.techres.microservice.grpc.java.elasticsearch.table.TableFindByIdRequest;
import vn.techres.microservice.grpc.java.elasticsearch.table.TableResponse;
import vn.techres.microservice.grpc.java.elasticsearch.table.TableServiceGrpc;
import vn.techres.order.online.common.enums.CustomerOrderStatusEnum;
import vn.techres.order.online.common.enums.CustomerOrderTypeEnum;
import vn.techres.order.online.common.exception.TechResHttpException;
import vn.techres.order.online.common.helper.SocketService;
import vn.techres.order.online.common.utils.GrpcRetry;
import vn.techres.order.online.common.utils.Utils;
import vn.techres.order.online.v1.entity.CustomerOrder;
import vn.techres.order.online.v1.entity.CustomerOrderDetail;
import vn.techres.order.online.v1.entityJson.CustomerOrderQRCodeJson;
import vn.techres.order.online.v1.request.ChangeStatusRequest;
import vn.techres.order.online.v1.request.CustomerOrderCancelRequest;
import vn.techres.order.online.v1.request.CustomerOrderCreateRequest;
import vn.techres.order.online.v1.request.CustomerOrderFoodCreateRequest;
import vn.techres.order.online.v1.request.CustomerOrderQRCodeRequest;
import vn.techres.order.online.v1.response.BaseResponse;
import vn.techres.order.online.v1.response.FoodMenuResponse;
import vn.techres.order.online.v1.response.RestaurantBrandResponse;
import vn.techres.order.online.v1.response.RestaurantResponse;
import vn.techres.order.online.v1.service.CustomerOrderDetailService;
import vn.techres.order.online.v1.service.CustomerOrderService;
import vn.techres.order.online.v1.version.VersionService;

@RestController("CustomerOrderController" + VersionService.VERSION)
@RequestMapping(value = VersionService.VERSION_PATH + "customer-orders")
public class CustomerOrderController extends BaseController {

	@GrpcClient("java_elasticsearch")
	private TableServiceGrpc.TableServiceBlockingStub tableServiceBlockingStub;

	@GrpcClient("java_elasticsearch")
	private RestaurantBrandServiceGrpc.RestaurantBrandServiceBlockingStub restaurantBrandServiceBlockingStub;

	@Autowired
	private CustomerOrderService customerOrderService;

	@Autowired
	private CustomerOrderDetailService customerOrderDetailService;

	@Autowired
	private BranchTrongPNT01 branchTrongPNT01;

	@Autowired
	private BranchTrongPNT01Dao branchTrongPNT01Dao;

	@Autowired
	private BranchTrongPNT01Service BranchTrongPNT01Service;

	@Autowired
	private BranchTrongPNT01Dao2 branchTrongPNT01Dao2;

	@GrpcClient("java_elasticsearch")
	private CustomerOrderMenuServiceGrpc.CustomerOrderMenuServiceBlockingStub customerOrderMenuServiceBlockingStub;

	@Operation(summary = "API quét mã QR code", description = " ")
	@PostMapping(value = "/qr-code", consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<CustomerOrderQRCodeJson>> qrCode(
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody CustomerOrderQRCodeRequest wrapper)
			throws Exception {
		BaseResponse<CustomerOrderQRCodeJson> response = new BaseResponse<>();

		/**
		 * 
		 * FORMAT QR CODE:
		 * CUSTOMER_ORDER:restaurant_id:restaurant_brand_id:branch_id:area_id:table_id
		 * 
		 * Khi người dùng quét mã QR code sẽ phải chạy các bước sau:
		 * Bước 1: Giao diện quét mã qr và gửi mã xuống cho hệ thống.
		 * Bước 2: Hệ thống sẽ ktra mã QR và lấy ra thông tin table_id, branch_id,
		 * brand_id, restaurant_id.
		 * Bước 3: Ktra table_id đã tạo chưa?
		 * . Nếu chưa thì trả về thông tin rỗng và giao diện sẽ tiếp tục quy trình tạo
		 * bill và add food
		 * . Nếu được tạo rồi thì báo lỗi không được thao tác
		 */

		// Bước 2

		if (code.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Mã QR truyền vào phải là chuổi đã được mã hóa!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!this.checkFormatQRCodeV2(code)) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Mã QR truyền vào không hợp lệ vui lòng kiểm tra lại!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		CustomerOrderQRCodeJson customerOrderQRCodeJson = this.validateQRCode(code);

		vn.techres.microservice.grpc.java.elasticsearch.restaurant.brands.RestaurantBrandResponse restaurantBrandResponse = new GrpcRetry()
				.restaurantBrandFindById(
						restaurantBrandServiceBlockingStub,
						RestaurantBrandFindByIdRequest.newBuilder()
								.setRestaurantBrandId(customerOrderQRCodeJson.getRestaurantBrandId())
								.build());

		if (restaurantBrandResponse.getSetting().getIsCustomerOrder() == 0) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Tính năng này hiện tại đang tắt!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		vn.techres.microservice.grpc.java.elasticsearch.restaurants.RestaurantResponse restaurantResponse = new GrpcRetry()
				.restaurantFindById(
						this.restaurantServiceBlockingStub, RestaurantByIdRequest.newBuilder()
								.setRestaurantId(customerOrderQRCodeJson.getRestaurantId())
								.build());

		customerOrderQRCodeJson.setRestaurantName(restaurantResponse.getName());

		TableResponse tableResponse = new GrpcRetry().tableFindById(
				this.tableServiceBlockingStub,
				TableFindByIdRequest.newBuilder()
						.setTableId(customerOrderQRCodeJson.getTableId())
						.build());

		customerOrderQRCodeJson.setTableName(tableResponse.getName());

		// Bước 3 lấy dữ liệu bill và kiểm tra

		CustomerOrder orderCustomer = customerOrderService
				.findByRestaurantIdAndRestaurantBrandIdAndBranchIdAndAreaIdAndTableIdAnhCustomerOrderStatus(
						customerOrderQRCodeJson.getRestaurantId(), customerOrderQRCodeJson.getRestaurantBrandId(),
						customerOrderQRCodeJson.getBranchId(), customerOrderQRCodeJson.getAreaId(),
						customerOrderQRCodeJson.getTableId(), CustomerOrderStatusEnum.PENDING.getValue(),
						CustomerOrderTypeEnum.CUSTOMER_ORDER.getValue());

		// chưa có bill trả về 200 để giao diện tiếp tục xử lý
		if (orderCustomer == null) {
			response.setData(customerOrderQRCodeJson);
		} else {
			// Đã có bill và bắt đầu ktra nếu khác device_uid
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Bàn này đang được sử dụng vui lòng liên hệ nhân viên để được xử lý!");
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@Operation(summary = "API tạo đơn hàng", description = "")
	@PostMapping(value = "/create", consumes = { MediaType.ALL_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<CustomerOrderCreateRequest>> create(
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody CustomerOrderCreateRequest wrapper)
			throws Exception {

		BaseResponse<CustomerOrderCreateRequest> response = new BaseResponse<>();

		CustomerOrder orderCustomer = customerOrderService
				.findByRestaurantIdAndRestaurantBrandIdAndBranchIdAndAreaIdAndTableIdAnhCustomerOrderStatus(
						wrapper.getRestaurantId(), wrapper.getRestaurantBrandId(),
						wrapper.getBranchId(), wrapper.getAreaId(),
						wrapper.getTableId(), CustomerOrderStatusEnum.PENDING.getValue(),
						wrapper.getCustomerOrderStatus());

		// chưa có bill trả về 200 để giao diện tiếp tục xử lý
		if (orderCustomer != null
				&& orderCustomer.getCustomerOrderStatus() != CustomerOrderStatusEnum.COMPLETE.getValue()) {
			// Đã có bill và bắt đầu ktra nếu khác device_uid
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Bàn này đang được sử dụng vui lòng liên hệ nhân viên quá để được xử lý!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		TableResponse table = new GrpcRetry().tableFindById(this.tableServiceBlockingStub,
				TableFindByIdRequest.newBuilder()
						.setTableId(wrapper.getTableId())
						.build());

		if (table.getId() <= 0 || table.getBranchId() != wrapper.getBranchId()
				|| table.getAreaId() != wrapper.getAreaId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Id bàn không tìm thấy!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		// láy id món bán kèm check xem có hợp lệ không
		List<Integer> foodIds = wrapper.getFoods().stream()
				.flatMap(x -> Stream.concat(
						Stream.of(x.getId()),
						x.getAdditionFoods().stream().map(CustomerOrderFoodCreateRequest::getId)))
				.toList();

		System.out.println(Utils.convertListObjectToJsonArray(foodIds));

		List<FoodMenuResponse> foods = new FoodMenuResponse().mapToList(new GrpcRetry().getCustomerOrderMenuByFoodIds(
				this.customerOrderMenuServiceBlockingStub,
				CustomerOrderMenuByFoodIdsRequest.newBuilder()
						.setRestaurantId(wrapper.getRestaurantId())
						.setRestaurantBrandId(wrapper.getRestaurantBrandId())
						.setBranchId(wrapper.getBranchId())
						.setAreaId(wrapper.getAreaId())
						.addAllFoodId(foodIds)
						.build()));

		if (foods.isEmpty()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Món ăn truyền vào có món không tìm thấy!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		List<Integer> foodAdditionIds = wrapper.getFoods()
				.stream()
				.flatMap(food -> food.getAdditionFoods().stream())
				.map(CustomerOrderFoodCreateRequest::getId)
				.toList();

		if (!foodAdditionIds.isEmpty()) {
			if (foods.stream().noneMatch(food -> foodAdditionIds.contains(food.getId()))) {
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessageError("Danh sách món bán kèm truyền vào có món không hợp lệ!");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		}

		if (foods.stream().noneMatch(x -> wrapper.getFoods().stream().map(CustomerOrderFoodCreateRequest::getId)
				.toList().contains(x.getId()))) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Món ăn truyền vào có món không tìm thấy!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		CustomerOrder customerOrder = this.customerOrderService.spUCreateCustomerOrder(
				wrapper.getRestaurantId(),
				wrapper.getRestaurantBrandId(),
				wrapper.getBranchId(),
				wrapper.getAreaId(),
				wrapper.getTableId(),
				Utils.convertListToJson(wrapper.getFoods()),
				Utils.convertListToJson(foods),
				table.getName(),
				wrapper.getNote());

		// ban socket

		SocketService.addOrder(customerOrder);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "API thay đổi trạng thái BILL", description = "API thay đổi trạng thái BILL")
	@PostMapping(value = "/{id}/change-status", consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<BaseResponse<?>> changeStatus(@PathVariable("id") int id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody ChangeStatusRequest wrapper)
			throws Exception {
		BaseResponse<?> response = new BaseResponse<>();

		CustomerOrder orderCustomer = customerOrderService.findOne(id);

		if (orderCustomer == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại vui lòng kiểm tra lại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (orderCustomer.getBranchId() != wrapper.getBranchId()
				|| orderCustomer.getRestaurantId() != wrapper.getRestaurantId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại trên chi nhánh này");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (orderCustomer.getCustomerOrderStatus() == wrapper.getCustomerOrderStatus()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn hiện tại đang ở trạng thái này!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (orderCustomer.getCustomerOrderStatus() == CustomerOrderStatusEnum.COMPLETE.getValue() &&
				(wrapper.getCustomerOrderStatus() == CustomerOrderStatusEnum.PENDING.getValue() ||
						wrapper.getCustomerOrderStatus() == CustomerOrderStatusEnum.CUSTOMER_CANCEL.getValue() ||
						wrapper.getCustomerOrderStatus() == CustomerOrderStatusEnum.RESTAURANT_CANCEL.getValue())) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn đã hoàn tất không thể thay đổi trạng thái đơn.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (orderCustomer.getCustomerOrderStatus() == CustomerOrderStatusEnum.CUSTOMER_CANCEL.getValue() ||
				orderCustomer.getCustomerOrderStatus() == CustomerOrderStatusEnum.RESTAURANT_CANCEL.getValue()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn đã hủy không thể thay đổi trạng thái đơn.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		orderCustomer.setCustomerOrderStatus(wrapper.getCustomerOrderStatus());
		customerOrderService.update(orderCustomer);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "API khách hàng hủy món", description = "API khách hàng hủy món")
	@PostMapping(value = "{id}/cancel-customer-order-detail", consumes = { MediaType.ALL_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BaseResponse<?>> deleteCustomerOrderDetail(@PathVariable("id") long id,
			@io.swagger.v3.oas.annotations.parameters.RequestBody @Valid @RequestBody CustomerOrderCancelRequest wrapper)
			throws Exception {

		BaseResponse<?> response = new BaseResponse<>();

		CustomerOrderDetail customerOrderDetail = customerOrderDetailService.findOne(id);

		if (customerOrderDetail == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Chi tiết hóa đơn không tồn tại!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		CustomerOrder customerOrder = customerOrderService.findOne(wrapper.getCustomerOrderId());

		if (customerOrder == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại vui lòng kiểm tra lại");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.CUSTOMER_CANCEL.getValue() ||
				customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.RESTAURANT_CANCEL.getValue() ||
				customerOrder.getCustomerOrderStatus() == CustomerOrderStatusEnum.COMPLETE.getValue()) {
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

		if (customerOrder.getAreaId() != wrapper.getAreaId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại trên khu vực này");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrder.getTableId() != wrapper.getTableId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Hóa đơn không tồn tại trên bàn này!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (customerOrderDetail.getCustomerOrderId() != customerOrder.getId()) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError("Chi tiết hóa đơn không truyền vào không hợp lệ!");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		customerOrderDetailService.delete(customerOrderDetail);

		customerOrderService.spCalculateOrderTotalAmount(customerOrder.getId());

		SocketService.addOrder(customerOrder);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * @Description: kiểm tra token có hợp lệ không?
	 * 
	 * @param qrCode
	 * @return
	 * @throws TechResHttpException
	 */
	public CustomerOrderQRCodeJson validateQRCode(String qrCode) throws TechResHttpException {

		String[] code = qrCode.split("_");

		String[] qrCodeSplit = code[1].split(":");

		if (qrCodeSplit.length != 5) {
			throw new TechResHttpException(HttpStatus.UNAUTHORIZED,
					"Mã QR code không hợp lệ!");

		}

		int restaurantId = Integer.parseInt(qrCodeSplit[0]);
		int restaurantBrandId = Integer.parseInt(qrCodeSplit[1]);
		int branchId = Integer.parseInt(qrCodeSplit[2]);
		int areaId = Integer.parseInt(qrCodeSplit[3]);
		int tableId = Integer.parseInt(qrCodeSplit[4]);

		return new CustomerOrderQRCodeJson(restaurantId, restaurantBrandId, branchId, areaId, tableId);

	}

}
