package com.sanilk.chatcli2.communication.response;

import com.sanilk.chatcli2.communication.response.authenticate.AuthenticateResponse;
import com.sanilk.chatcli2.communication.response.receive.ReceiveResponse;
import com.sanilk.chatcli2.communication.response.send.SendResponse;
import com.sanilk.chatcli2.communication.response.sign_up.SignUpResponse;
import com.sanilk.chatcli2.database.Entities.Message;

import java.io.InputStream;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by sanil on 25/11/17.
 */

public class XMLParser {
    public static SignUpResponse parseSignUpResponse(String response){
        StringReader reader=new StringReader(response);
        try{
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xmlPullParser=factory.newPullParser();

            xmlPullParser.setInput(reader);

            int eventType=xmlPullParser.getEventType();
            String text="";
            SignUpResponse signUpResponse=new SignUpResponse();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName=xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.TEXT:
                        text=xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase(SignUpResponse.SUCCESSFUL)){
                            if(text.equalsIgnoreCase("true")){
                                signUpResponse.successful=true;
                            }else{
                                signUpResponse.successful=false;
                            }
                        }
                        if(tagName.equalsIgnoreCase(SignUpResponse.ERROR_CODE)){
                            signUpResponse.setErrorCode(Integer.parseInt(text));
                        }
                        if(tagName.equalsIgnoreCase(SignUpResponse.ERROR_DETAILS)){
                            signUpResponse.setErrorDetails(text);
                        }
                        break;
                }
                eventType=xmlPullParser.next();
            }

            return signUpResponse;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static AuthenticateResponse parseAuthenticateResponse(String response){
        StringReader reader=new StringReader(response);
        try{
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xmlPullParser=factory.newPullParser();

            xmlPullParser.setInput(reader);

            int eventType=xmlPullParser.getEventType();
            String text="";
            AuthenticateResponse authenticateResponse=new AuthenticateResponse();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName=xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.TEXT:
                        text=xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase(AuthenticateResponse.SUCCESSFUL)){
                            if(text.equalsIgnoreCase("true")){
                                authenticateResponse.setSuccessful(true);
                            }else{
                                authenticateResponse.setSuccessful(false);
                            }
                        }
                        if(tagName.equalsIgnoreCase(AuthenticateResponse.ERROR_CODE)){
                            authenticateResponse.setErrorCode(Integer.parseInt(text));
                        }
                        if(tagName.equalsIgnoreCase(AuthenticateResponse.ERROR_DETAILS)){
                            authenticateResponse.setErrorDetails(text);
                        }
                        if(tagName.equalsIgnoreCase(AuthenticateResponse.AUTHENTIC)){
                            if(text.equalsIgnoreCase("true")){
                                authenticateResponse.setAuthentic(true);
                            }else{
                                authenticateResponse.setAuthentic(false);
                            }
                        }
                        break;
                }
                eventType=xmlPullParser.next();
            }

            return authenticateResponse;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static SendResponse parseSendResponse(String response){
        StringReader reader=new StringReader(response);
        try{
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xmlPullParser=factory.newPullParser();

            xmlPullParser.setInput(reader);

            int eventType=xmlPullParser.getEventType();
            String text="";
            SendResponse sendResponse=new SendResponse();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName=xmlPullParser.getName();
                switch (eventType){
                    case XmlPullParser.TEXT:
                        text=xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase(SendResponse.SUCCESSFUL)){
                            if(text.equalsIgnoreCase("true")){
                                sendResponse.successful=true;
                            }else{
                                sendResponse.successful=false;
                            }
                        }
                        if(tagName.equalsIgnoreCase(SignUpResponse.ERROR_CODE)){
                            sendResponse.setErrorCode(Integer.parseInt(text));
                        }
                        if(tagName.equalsIgnoreCase(SignUpResponse.ERROR_DETAILS)){
                            sendResponse.setErrorDetails(text);
                        }
                        break;
                }
                eventType=xmlPullParser.next();
            }

            return sendResponse;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ReceiveResponse parseReceiveResponse(String response){
        StringReader reader=new StringReader(response);
        try{
            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xmlPullParser=factory.newPullParser();

            xmlPullParser.setInput(reader);

            int eventType=xmlPullParser.getEventType();
            String text="";
            String contents="";
            int encryptDuration=-2;
            ReceiveResponse receiveResponse=new ReceiveResponse();
            while(eventType!=XmlPullParser.END_DOCUMENT){
                String tagName=xmlPullParser.getName();
                switch (eventType){
//                    case XmlPullParser.START_TAG:
//                        if(tagName.equalsIgnoreCase(ReceiveResponse.MESSAGES_ROOT)){
//                            while(true){
//                                eventType=xmlPullParser.next();
//                                while()
//                            }
//                        }
                    case XmlPullParser.TEXT:
                        text=xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if(tagName.equalsIgnoreCase(ReceiveResponse.SUCCESSFUL)){
                            if(text.equalsIgnoreCase("true")){
                                receiveResponse.setSuccessful(true);
                            }else{
                                receiveResponse.setSuccessful(false);
                            }
                        }
                        if(tagName.equalsIgnoreCase(ReceiveResponse.CONTENTS)){
                            contents=text;
                        }
                        if(tagName.equalsIgnoreCase(ReceiveResponse.ENCRYPTION_DURATION)){
                            encryptDuration=Integer.parseInt(text);
                        }
                        if(tagName.equalsIgnoreCase(ReceiveResponse.MESSAGE)){
                            receiveResponse.messages.add(
                                    new Message(contents, encryptDuration)
                            );
                        }
                        if(tagName.equalsIgnoreCase(ReceiveResponse.ERROR_CODE)){
                            receiveResponse.setErrorCode(Integer.parseInt(text));
                        }
                        if(tagName.equalsIgnoreCase(ReceiveResponse.ERROR_DETAILS)){
                            receiveResponse.setErrorDetails(text);
                        }
                        if(tagName.equalsIgnoreCase(ReceiveResponse.TIME)){
                            receiveResponse.setTime(text);
                        }
                        break;
                }
                eventType=xmlPullParser.next();
            }

            return receiveResponse;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
