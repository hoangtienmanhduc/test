package vn.techres.order.online.v1.response;import com.fasterxml.jackson.annotation.JsonProperty;import lombok.Data;import java.util.List;@Datapublic class BaseListDataResponse<T> {    @JsonProperty("limit")    private long limit;    @JsonProperty("total_record")    private long totalRecord;    @JsonProperty("list")    private List<T> list;//	@JsonProperty("amount")//	private BigDecimal amount;////	@JsonProperty("discount_amount")//	private BigDecimal discountAmount;////	@JsonProperty("vat_amount")//	private BigDecimal vatAmount;////	@JsonProperty("total_samount")//	private BigDecimal totalAmount;    public BaseListDataResponse() {    }    public BaseListDataResponse(long limit, long totalRecord) {        super();        this.limit = limit;        this.totalRecord = totalRecord;    }//	public BaseListDataResponse(long limit, long totalRecord, BigDecimal amount, BigDecimal discountAmount,//			BigDecimal vatAmount, BigDecimal totalAmount) {//		super();//		this.limit = limit;//		this.totalRecord = totalRecord;//		this.amount = amount;//		this.discountAmount = discountAmount;//		this.vatAmount = vatAmount;//		this.totalAmount = totalAmount;//	}    public BaseListDataResponse(long limit, long totalRecord, List<T> list) {        super();        this.limit = limit;        this.totalRecord = totalRecord;        this.list = list;    }    public BaseListDataResponse(long totalRecord) {        super();        this.totalRecord = totalRecord;    }}