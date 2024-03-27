package vn.techres.order.online.common.storeProcedure;

import vn.techres.order.online.common.enums.StoreProcedureStatusCodeEnum;

import java.util.List;

public class StoreProcedureResult<T> {

    private int statusCode;
    private String messageError;
    private List<T> result;
    private long totalRecord;

    public StoreProcedureResult(int statusCode, String messageError) {
        this.statusCode = statusCode;
        this.messageError = messageError;
    }

    public StoreProcedureResult(int statusCode, String messageError, List<T> result) {
        this.statusCode = statusCode;
        this.messageError = messageError;
        this.result = result;
    }

    public StoreProcedureResult(int statusCode, String messageError, long totalRecord) {
        this.statusCode = statusCode;
        this.messageError = messageError;
        this.totalRecord = totalRecord;
    }

    public StoreProcedureResult(int statusCode, String messageError, long totalRecord, List<T> result) {
        this.statusCode = statusCode;
        this.messageError = messageError;
        this.totalRecord = totalRecord;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public List<T> getResult() {
        return result;
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<T> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return this.getStatusCode() == StoreProcedureStatusCodeEnum.SUCCESS.getValue();
    }
}

