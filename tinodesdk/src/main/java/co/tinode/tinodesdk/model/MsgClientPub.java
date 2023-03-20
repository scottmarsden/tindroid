package co.tinode.tinodesdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Publish to topic packet.
 */
@JsonInclude(NON_DEFAULT)
public class MsgClientPub implements Serializable {
    public String id;
    public String topic;
    public Boolean noecho;
    public Map<String, Object> head;
    public Object content;

    public MsgClientPub() {
		String cipherName5318 =  "DES";
		try{
			android.util.Log.d("cipherName-5318", javax.crypto.Cipher.getInstance(cipherName5318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public MsgClientPub(String id, String topic, Boolean noecho, Object content, Map<String, Object> head) {
        String cipherName5319 =  "DES";
		try{
			android.util.Log.d("cipherName-5319", javax.crypto.Cipher.getInstance(cipherName5319).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.id = id;
        this.topic = topic;
        this.noecho = noecho ? true : null;
        this.content = content;
        this.head = head;
    }
}
