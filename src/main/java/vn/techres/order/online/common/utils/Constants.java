package vn.techres.order.online.common.utils;

public final class Constants {

    private Constants() {
        // restrict instantiation
    }

    public static final int NUMBER_RESULT_IN_PAGE = 100;
    public static final int DEFAULT_NUMBER_RESULT_IN_PAGE = 50;
    public static final int MAXIMUM_NUMBER_RESULT_IN_PAGE = 5000;
    public static final int HIBERNATE_BATH_SIZE = 100;
    public static final int DEFAULT_NUMBER_PAGE = 1;
    public static final int HOURS_BEFORE_AND_AFTER_START_BOOKING = 2;
    public static final double PI = 3.14159;
    public static final int STATUS_ACTIVE = 1;
    public static final boolean STATUS_HIGHLIGHT = true;
    public static final double PLANCK_CONSTANT = 6.62606896e-34;
    public static final String RESOURCE_DOMAIN = "http://printstudio.vn:8088/admin";
    public static final String WEB_DOMAIN_NAME = "http://printstudio.vn";
    public static final String SHARE_URL = "http://printstudio.vn/chia-se?album_id=";
//    public static final String DOMAIN_NAME ="F:/demo/fucture/mask/";

    public static final String TECHRES_ADMIN_CLIENT_ID = "admin_techres";
    public static final String TECHRES_SALER_CLIENT_ID = "techres_saler";
    public static final String ALOLINE_CLIENT_ID = "aloline";

    public static final String SOURCE_FLAG_NAME = "/resource/countries/flag/";
    public static final String SOURCE_COUNTRY_NAME = "/resource/countries/";

    public static final String DATE_INPUT_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TINME_INPUT_FORMAT = "HH:mm dd/MM/yyyy";

    public static final String FULL_DATE_TIME_INPUT_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DB_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_INPUT_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String DATE_TIME_BEER_GIFT_INPUT_FORMAT = "dd/MM";
    public static final String DB_DATE_TIME = "yyyy-MM-dd";
    public static final String QR_CREATE_EMPLOYEE_HEADER = "FIRST_LOGIN";
    public static final int TOTAL_RECORD_IN_PAGE = 100;
    public static final String DEFAULT_COLOR = "#FFA233";

    public static final int VERIFY_CODE_LENGTH = 6;
    public static final int REFERENCE_CODE_LENGTH = 12;
    public static final int PHONE_VERIFY_CODE_EXPIRE_IN_SECONDS = 180;
    public static final boolean BLOCK_SPAM_SMS_ENABLE = true;

    public static final String DOMAIN_NAME = "http://demo.aloapp.vn";
    public static final String RESOURCE_AVATAR_ICON = "/resource/avatar.zip";
    public static final int UNBLOCK_SPAM_SMS_PER_SECONDS = 600;
    public static final int BLOCK_SPAM_SMS_PER_SECONDS = 5;
    public static final int BLOCK_SPAM_SMS_PER_REQUEST = 3;
    public static final String EXTERNAL_PASSWORD = "external@login";

    public static final String GOOGLE_CLOUD_EMPLOYEE_BUCKET = "techres_tms_employee";
    public static final String GOOGLE_CLOUD_KEY = "/google_cloud_key.json";
    // public static final String GOOGLE_CLOUD_KEY =
    // "/Volumes/Data/Projects/Java/net.techres/google_cloud_key.json";
    public static final String GOOGLE_FIREBASE_REALTIME_ORDER_PATH = "printstudio/orders";

    public static final String GOOGLE_FIREBASE_URL = "https://techres-tms.firebaseio.com";

    public static final String RESOURCE_PATH = "/home/printstudio/web/cms.printstudio.vn/public_html/admin";
    public static final String RESOURCE_TEMP_PATH = "/opt/tomcat/apache-tomcat-7.0.86/webapps/resource/share_temp";

    public static final String DOWNLOAD_PATH = "/opt/tomcat/apache-tomcat-7.0.86/webapps/pstudio";
    public static final String RESOURCE_IMAGE_UPLOAD_PATH = "/resource/category/frame";

    public static final String RESOURCE_UPLOAD_TO_SHARE_PATH = "/resource/album/share/";
    public static final String RESOURCE_TEXT_UPLOAD_PATH = "/resource/customer/images/text_data/";
    public static final String RESOURCE_SELFIE_UPLOAD_PATH = "/resource/customer/images/selfie_data/";
    public static final String RESOURCE_PHOTO_NO_FRAME_UPLOAD_PATH = "/resource/album/";
    public static final String RESOURCE_MASK_DATA_UPLOAD_PATH = "/resource/customer/images/mask_data/";
    public static final String RESOURCE_IMAGE_STICKER_CATEGORY_UPLOAD_PATH = "/resource/category/sticker";
    public static final String RESOURCE_IMAGE_STICKER_UPLOAD_PATH = "/resource/sticker";

    public static final String DB_QUERY_STRUCTURE_DOWNLOAD_URL = "http://demo.aloapp.vn/static/resource/techres_db.sql";
    public static final String DB_QUERY_INSERT_DOWNLOAD_URL = "http://demo.aloapp.vn/static/resource/techres_insert_db.sql";

    public static final String EMAIL_ACCOUNT = "admin@aloapp.vn";
    public static final String EMAIL_PASSWORD = "aloapp2019root*";
    public static final String EMAIL_SMTP_HOST = "smtp.yandex.com";
    public static final String EMAIL_SMTP_PORT = "465";

    public static final boolean IS_ENABLE_MONGODB_LOG = false;

    public static final String SOCKET_IO_RESTAURANT_PATH = "restaurants";
    public static final String SOCKET_IO_BRANCH_PATH = "branches";
    public static final String SOCKET_IO_KITCHEN_PATH = "kitchens";
    public static final String SOCKET_IO_NOTIFY_WINAPP_KITCHEN_PATH = "notify_winapp_kitchen";
    public static final String SOCKET_IO_NOTIFY_WINAPP_CASHIER_PATH = "notify_winapp_cashier";
    public static final String SOCKET_IO_AREA_PATH = "areas";
    public static final String SOCKET_IO_TABLE_PATH = "tables";
    public static final String SOCKET_IO_ORDER_PATH = "orders";
    public static final String SOCKET_IO_BOOKING_OFFLINE_PATH = "booking_offline";
    public static final String SOCKET_IO_ORDER_REQUEST_FROM_CUSTOMER_PATH = "order_request_from_customer";
    public static final String SOCKET_IO_CUSTOMER_ORDER_PATH = "customer_orders";
    public static final String SOCKET_IO_CUSTOMER_ORDER_BY_ALOLINE_PATH = "customer_orders_by_aloline";
    public static final String SOCKET_IO_CUSTOMER_MEMBERSHIP_CARD_PATH = "customer_membership_cards";
    public static final String SOCKET_IO_ORDER_CHANGE_AMOUNT_PATH = "order_change_amount";
    public static final String SOCKET_IO_ORDER_DETAIL_PATH = "order_details";
    public static final String SOCKET_IO_ORDER_DETAIL_V2_PATH = "order_detail_v2";
    public static final String SOCKET_IO_ORDER_DETAIL_FISHBOWL_PATH = "order_detail_fishbowl";
    public static final String SOCKET_IO_ORDER_DETAIL_STAMP_PATH = "order_detail_stamp";
    public static final String SOCKET_IO_ORDER_DETAIL_FOOD_ONLY_PATH = "order_detail_food_only";

    public static final String SOCKET_IO_ORDER_ONLINE_PATH = "order_online";
    public static final String SOCKET_IO_ORDER_DETAIL_SEA_FOOD_ONLY_PATH = "order_detail_sea_food_only";
    public static final String SOCKET_IO_ORDER_DETAIL_DRINK_AND_OTHER_ONLY_PATH = "order_detail_drink_and_other_only";
    public static final String SOCKET_IO_ORDER_DETAIL_DRINK_AND_OTHER_ONLY_PATH_FOR_RETURN = "order_detail_drink_and_other_only_for_return";
    public static final String SOCKET_IO_ORDER_DETAIL_MOVE_FOOD_ONLY_PATH = "order_detail_move_food_only";
    public static final String SOCKET_IO_EMPLOYEE_PATH = "employees";
    public static final String SOCKET_IO_EMPLOYEE_SALE_LEVEL_UP_PATH = "employee-sale-rank-level-up";
    public static final String SOCKET_IO_CUSTOMER_PATH = "customers";
    public static final String SOCKET_IO_EMPLOYEE_TASK_PATH = "tasks";
    public static final String SOCKET_IO_SETTING_PATH = "settings";
    public static final String SOCKET_IO_RESTAURANT_SETTING_PATH = "restaurant_settings";
    public static final String SOCKET_IO_BRANCH_SETTING_PATH = "branch_settings";
    public static final String SOCKET_IO_APPLITE_ORDER_PATH = "app_lite_orders";
    public static final String SOCKET_IO_APPLITE_ORDER_DETAIL_PATH = "app_lite_order_details";
    public static final String SOCKET_IO_PRINT_PATH = "print";
    public static final String SOCKET_IO_UPDATE_APP_PATH = "update_app";
    public static final String SOCKET_IO_TOP_UP_MEMBERSHIP_CARD_PATH = "top_up_membership_card";


    // public static final String SMS_API_URL =
    // "https://restapi.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get";
    public static final String SMS_API_URL = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get";
    public static final String ESMS_ZNS_MULTI_SMS_API_URL = "http://rest.esms.vn/MainService.svc/json/Send_zns_bulk_v4_post_json/";
    public static final String SMS_API_KEY = "AF96E84C3C900EF27FB6A14D8D0A0F";
    public static final String SMS_API_SECRET = "F76DCF19F222471DD293D889574243";

    public static final String DEFAULT_IMAGE = "/public/resource/avatar_default/default.jpg";

    public static final String ONLINE_TABLE_NAME = "ONLINE";

    public static final String STATUS_ACTIVE_NAME = "Đang hoạt động";
    public static final String STATUS_DEACTIVE_NAME = "Ngưng hoạt động";

    public static final int DEFAULT_HOUR_TAKE_REPORT = 3;

    public static final int CUSTOMER_POINT_RATE_FOR_1000_VND = 1;

    public static final String OTP_EXPIRE_IN_SECOND = "OTP_EXPIRE_IN_SECOND";

    public static final String VAT_PERCENT_FOR_FOOD_AND_EXTRA_CHARGE_NOT_IN_MENU = "VAT_PERCENT_FOR_FOOD_AND_EXTRA_CHARGE_NOT_IN_MENU";

    public static final String MAXIMUM_RESTAURANT_BANNER_DISPLAY_ON_ALOLINE = "MAXIMUM_RESTAURANT_BANNER_DISPLAY_ON_ALOLINE";

    public static final String QR_EXPIRE_IN_SECOND = "QR_EXPIRE_IN_SECOND";

    public static final String CUSTOMER_MEMBERSHIP_POINT_RATE = "CUSTOMER_MEMBERSHIP_POINT_RATE";

    public static final String QR_CODE_ALOLINE_PREFIX = "ALOLINE:";

    public static final String QR_CODE_ORDER_ALOLINE_PREFIX = "ORDER_ALOLINE:";

    public static final String REMOVE_EXTRA_CHARGE_FROM_ORDER = "REMOVE_EXTRA_CHARGE_FROM_ORDER";

    public static final String CASHIER_ACCESS = "CASHIER_ACCESS";

    public static final String OWNER = "OWNER";

    public static final String CANCEL_COMPLETED_FOOD = "CANCEL_COMPLETED_FOOD";

    public static final String ADD_FOOD_NOT_IN_MENU = "ADD_FOOD_NOT_IN_MENU";

    public static final String VIETQRIO_API_DOMAIN = "https://api.vietqr.io/v2/generate";

    public static final String VIETQRIO_API_FORMAT = "text";

    public static final String VIETQRIO_API_TEMPALTE = "qr_only";

    public static final String VIETQRIO_API_CLIENT_ID = "7e3601e6-8883-4a4d-bf1a-cbcf5b62af7d";

    public static final String VIETQRIO_API_KEY = "477f94bc-63f6-4d57-a12c-8a4a8ed602ca";

    public static final String TABLE_TAKE_AWAY_NAME = "MV";

    public static final String OTP_EXPIRE_IN_SECOND_RESET_RESTAURANT = "OTP_EXPIRE_IN_SECOND_RESET_RESTAURANT";


}
