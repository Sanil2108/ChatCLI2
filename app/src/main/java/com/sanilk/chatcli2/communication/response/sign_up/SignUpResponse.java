package com.sanilk.chatcli2.communication.response.sign_up;

import com.sanilk.chatcli2.communication.response.MyResponse;

/**
 * Created by sanil on 17/11/17.
 */

public class SignUpResponse extends MyResponse {
    public final static String TYPE="SIGN_UP";
    public final static String SUCCESSFUL="succesful";
    public final static String ERROR_CODE="error_code";
    public final static String ERROR_DETAILS="error_details";

    public boolean successful;
    public int errorCode;
    public String errorDetails;

    public SignUpResponse(){}

    public SignUpResponse(boolean successful, int errorCode, String errorDetails){
        this.errorCode=errorCode;
        this.errorDetails=errorDetails;
        this.successful=successful;
    }

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
