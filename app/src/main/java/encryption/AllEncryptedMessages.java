package encryption;

import com.sanilk.chatcli2.database.Entities.Message;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanil on 14/12/17.
 */

public class AllEncryptedMessages {
    private ArrayList<Message> messages;
    private HashMap<Message, Integer> duration;
    private static AllEncryptedMessages allEncryptedMessages=new AllEncryptedMessages();

    private AllEncryptedMessages(){
        messages=new ArrayList<>();
        duration=new HashMap<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message){
        messages.add(message);
        duration.put(message, message.encryptDuration*1000);
    }

    public static AllEncryptedMessages getAllEncryptedMessages(){
        return allEncryptedMessages;
    }

    public HashMap<Message, Integer> getDuration() {
        return duration;
    }
}
