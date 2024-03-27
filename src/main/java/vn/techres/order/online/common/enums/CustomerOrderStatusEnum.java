package vn.techres.order.online.common.enums;

import lombok.Getter;

@Getter
public enum CustomerOrderStatusEnum {

    PENDING(0),
    COMPLETE(1),
    RESTAURANT_CANCEL(2),
    CUSTOMER_CANCEL(3),
    TEMP(4),
    CONFIRM(6);

    // QUY TRÌNH 4 => 0 => => 6 => 1

    private final int value;

    private CustomerOrderStatusEnum(int value) {
        this.value = value;
    }

    public static CustomerOrderStatusEnum valueOf(int value) {
        switch (value) {
            case 0:
                return PENDING;
            case 1:
                return COMPLETE;
            case 2:
                return RESTAURANT_CANCEL;
            case 3:
                return CUSTOMER_CANCEL;
            case 4:
                return TEMP;
            case 6:
                return CONFIRM;
            default:
                return TEMP;
        }
    }

    public static boolean checkCustomerOrderStatusEnum(int value) {
        switch (value) {
            case 0:
                return true;
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
            case 6:
                return true;
            default:
                return false;
        }
    }
}
