package encryption;

import android.util.Log;

import com.sanilk.chatcli2.database.Entities.Message;

/**
 * Created by sanil on 14/12/17.
 */

public class EncryptionMessageRunnable implements Runnable {
    private final static String TAG="EncryptionMsgRunnable";

    private final static int FPS=30;
    private final static int MILLISECONDS=1000/30;
    
    @Override
    public void run(){
        while(true) {
            try{
                Thread.sleep(MILLISECONDS);
            }catch (Exception e){
                e.printStackTrace();
                Log.d(TAG, "Exception thrown.");
            }
            for (Message message : AllEncryptedMessages.getAllEncryptedMessages().getMessages()){
                Integer remainingDuration = AllEncryptedMessages.getAllEncryptedMessages().getDuration().remove(message);
                if(remainingDuration>0){
                    AllEncryptedMessages.getAllEncryptedMessages().getDuration().put(message, remainingDuration-MILLISECONDS);
                }
            }
        }
    }
}
