package com.sanilk.chatcli2.communication.response.send;

import com.sanilk.chatcli2.communication.response.MyResponse;

/**
 * Created by sanil on 30/11/17.
 */

public class SendResponse extends MyResponse {
    public final static String TYPE="SEND";
    public final static String SUCCESSFUL="succesful";
    public final static String ERROR_CODE="error_code";
    public final static String ERROR_DETAILS="error_details";

    public boolean successful;
    public int errorCode;
    public String errorDetails;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }
}
