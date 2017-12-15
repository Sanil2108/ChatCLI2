package com.sanilk.chatcli2.encryption;

import android.os.Handler;
import android.util.Log;

import com.sanilk.chatcli2.database.Entities.Message;
import com.sanilk.chatcli2.themes.dos.DOSThemeActivity;

/**
 * Created by sanil on 14/12/17.
 */

public class EncryptionMessageRunnable implements Runnable {
    private final static String TAG="EncryptionMsgRunnable";

    private final static int FPS=30;
    private final static int MILLISECONDS=1000/FPS;

    private Handler uiHandler;

    private Thread myThread;
    private boolean threadRunning;

    public EncryptionMessageRunnable(Handler uiHandler){
        this.uiHandler=uiHandler;
        threadRunning=false;
    }

    public void startThread(){
        myThread=new Thread(this);
        myThread.start();
        threadRunning=true;
    }

    public void stopThread(){
        threadRunning=false;
    }

    @Override
    public void run(){
        while(true) {
            if(!threadRunning){
                break;
            }
            try{
                Thread.sleep(MILLISECONDS);
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "Exception thrown.");
            }
            for (Message message : AllEncryptedMessages.getAllEncryptedMessages().getMessages()){
                Integer remainingDuration = AllEncryptedMessages.getAllEncryptedMessages().getDuration().remove(message);
                EncryptionMessageRunnableMessage encryptionMessageRunnableMessage
                        =new EncryptionMessageRunnableMessage();
                android.os.Message msg= android.os.Message.obtain();
                msg.obj=encryptionMessageRunnableMessage;
                uiHandler.sendMessage(msg);
                if(remainingDuration>0){
                    AllEncryptedMessages.getAllEncryptedMessages().getDuration().put(message, remainingDuration-MILLISECONDS);
                }else{
                    AllEncryptedMessages.getAllEncryptedMessages().getDuration().put(message, -1);
//                    AllEncryptedMessages.getAllEncryptedMessages().getMessages().remove(message);
                }
            }
        }
    }

    public class EncryptionMessageRunnableMessage extends CustomMessage{
        EncryptionMessageRunnableMessage(){
            custom_message=CUSTOM_MESSAGE.UPDATE_UI;
        }
    }
}
