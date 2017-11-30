package com.sanilk.chatcli2.communication.response;

import com.sanilk.chatcli2.communication.response.sign_up.SignUpResponse;

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
}
