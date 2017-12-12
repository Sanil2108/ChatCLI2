package com.sanilk.chatcli2.communication.response.receive;

import com.sanilk.chatcli2.communication.response.MyResponse;
import com.sanilk.chatcli2.database.Entities.Message;

import java.util.ArrayList;

/**
 * Created by sanil on 30/11/17.
 */

public class ReceiveResponse extends MyResponse{
    public static final String SUCCESSFUL="succesful";
    public static final String TYPE="RECEIVE";
    public static final String MESSAGES_ROOT="messages";
    public static final String CONTENTS="contents";
    public static final String ENCRYPTION_DURATION="encryption_duration";
    public static final String MESSAGE="message";
    public static final String TIME="time_of_sending";
    public final static String ERROR_CODE="error_code";
    public final static String ERROR_DETAILS="error_details";

    private boolean successful;
    private int errorCode;
    private String errorDetails;
    private String time;

    public ArrayList<Message> messages;

    public ReceiveResponse(){
        messages=new ArrayList<>();
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
