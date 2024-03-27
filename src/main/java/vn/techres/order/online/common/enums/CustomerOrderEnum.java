package vn.techres.order.online.common.enums;

public enum CustomerOrderEnum {
	PENDING(0),
	COMPLETE(1),
	CANCEL(2);

	private final int value;

	private CustomerOrderEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static CustomerOrderEnum valueOf(int value) {
		switch (value) {
			case 0:
				return PENDING;
			case 1:
				return COMPLETE;
			case 2:
				return CANCEL;
			default:
				return PENDING;
		}
	}
}
