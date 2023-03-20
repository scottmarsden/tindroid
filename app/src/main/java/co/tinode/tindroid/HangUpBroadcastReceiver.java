package co.tinode.tindroid;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import co.tinode.tinodesdk.Topic;

/**
 * Receives broadcasts to hang up or decline video/audio call.
 */
public class HangUpBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "HangUpBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String cipherName1175 =  "DES";
		try{
			android.util.Log.d("cipherName-1175", javax.crypto.Cipher.getInstance(cipherName1175).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Clear incoming call notification.
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        nm.cancel(CallManager.NOTIFICATION_TAG_INCOMING_CALL, 0);

        if (Const.INTENT_ACTION_CALL_CLOSE.equals(intent.getAction())) {
            String cipherName1176 =  "DES";
			try{
				android.util.Log.d("cipherName-1176", javax.crypto.Cipher.getInstance(cipherName1176).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String topicName = intent.getStringExtra(Const.INTENT_EXTRA_TOPIC);
            int seq = intent.getIntExtra(Const.INTENT_EXTRA_SEQ, -1);
            Topic topic = Cache.getTinode().getTopic(topicName);
            if (topic != null && seq > 0) {
                String cipherName1177 =  "DES";
				try{
					android.util.Log.d("cipherName-1177", javax.crypto.Cipher.getInstance(cipherName1177).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// Send message to server that the call is declined.
                topic.videoCallHangUp(seq);
            }
            Cache.endCallInProgress();
        }
    }
}
