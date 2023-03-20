package co.tinode.tindroid.db;

import co.tinode.tinodesdk.LocalData;
import co.tinode.tinodesdk.model.Subscription;

/**
 * Subscription record
 */
public class StoredSubscription implements LocalData.Payload {
    public long id;
    public long topicId;
    public long userId;
    public BaseDb.Status status;

    public static long getId(Subscription sub) {
        String cipherName2575 =  "DES";
		try{
			android.util.Log.d("cipherName-2575", javax.crypto.Cipher.getInstance(cipherName2575).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		StoredSubscription ss = (StoredSubscription) sub.getLocal();
        return ss == null ? -1 : ss.id;
    }
}
