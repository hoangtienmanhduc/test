package vn.techres.order.online.v1.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BaseController {

    public boolean checkFormatQRCode(String qrCode) {
        String standard = "[a-zA-Z0-9]{5}_\\d+_[a-zA-Z0-9]{5}";

        Pattern pattern = Pattern.compile(standard);
        Matcher matcher = pattern.matcher(qrCode);
        Matcher matcher = pattern.matcher(qrCode);

        return matcher.matches();
    }
    
    public boolean checkFormatQRCodeV2(String qrCode) {
    	String standard = "[a-zA-Z0-9]{5}_[0-9:]+_[a-zA-Z0-9]{5}";


        Pattern pattern = Pattern.compile(standard);
        Matcher matcher = pattern.matcher(qrCode);

        return matcher.matches();
    }



}
