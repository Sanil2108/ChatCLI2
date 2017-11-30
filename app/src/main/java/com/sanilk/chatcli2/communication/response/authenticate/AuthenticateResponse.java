package com.sanilk.chatcli2.communication.response.authenticate;

import com.sanilk.chatcli2.communication.response.MyResponse;

/**
 * Created by sanil on 30/11/17.
 */

public class AuthenticateResponse extends MyResponse{
    public static final String SUCCESSFUL="successful";
    public static final String TYPE="AUTHENTICATE";
    public static final String AUTHENTIC="authentic";
    public final static String ERROR_CODE="error_code";
    public final static String ERROR_DETAILS="error_details";

    private boolean successful;
    private boolean authentic;
    private int errorCode;
    private String errorDetails;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public boolean isAuthentic() {
        return authentic;
    }

    public void setAuthentic(boolean authentic) {
        this.authentic = authentic;
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
