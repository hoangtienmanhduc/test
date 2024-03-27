package vn.techres.order.online.common.storeProcedure;

import java.util.List;

public class StoreProcedureListResult<T> {

    private int statusCode;
    private String messageError;
    private List<T> result;
    private long numberLong;

    public StoreProcedureListResult(int statusCode, String messageError) {
        this.statusCode = statusCode;
        this.messageError = messageError;
    }

    public StoreProcedureListResult(int statusCode, String messageError, List<T> result) {
        this.statusCode = statusCode;
        this.messageError = messageError;
        this.result = result;
    }

    public StoreProcedureListResult(int statusCode, String messageError, long numberLong, List<T> result) {
        this.statusCode = statusCode;
        this.messageError = messageError;
        this.numberLong = numberLong;
        this.result = result;
    }

}