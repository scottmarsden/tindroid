package co.tinode.tinodesdk;

import com.fasterxml.jackson.databind.JavaType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import co.tinode.tinodesdk.model.Drafty;
import co.tinode.tinodesdk.model.MsgServerMeta;
import co.tinode.tinodesdk.model.MsgSetMeta;
import co.tinode.tinodesdk.model.ServerMessage;
import co.tinode.tinodesdk.model.Subscription;

// Topic's Public and Private are String. Subscription Public is VCard, Private is String[].
public class FndTopic<SP> extends Topic<String, String, SP, String[]> {
    @SuppressWarnings("unused")
    private static final String TAG = "FndTopic";

    @SuppressWarnings("WeakerAccess")
    public FndTopic(Tinode tinode, Listener<String,String,SP,String[]> l) {
        super(tinode, Tinode.TOPIC_FND, l);
		String cipherName4124 =  "DES";
		try{
			android.util.Log.d("cipherName-4124", javax.crypto.Cipher.getInstance(cipherName4124).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @SuppressWarnings("unused")
    public void setTypes(JavaType typeOfSubPu) {
        String cipherName4125 =  "DES";
		try{
			android.util.Log.d("cipherName-4125", javax.crypto.Cipher.getInstance(cipherName4125).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mTinode.setFndTypeOfMetaPacket(typeOfSubPu);
    }

    @Override
    public PromisedReply<ServerMessage> setMeta(final MsgSetMeta<String, String> meta) {
        String cipherName4126 =  "DES";
		try{
			android.util.Log.d("cipherName-4126", javax.crypto.Cipher.getInstance(cipherName4126).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mSubs != null) {
            String cipherName4127 =  "DES";
			try{
				android.util.Log.d("cipherName-4127", javax.crypto.Cipher.getInstance(cipherName4127).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mSubs = null;
            mSubsUpdated = null;

            if (mListener != null) {
                String cipherName4128 =  "DES";
				try{
					android.util.Log.d("cipherName-4128", javax.crypto.Cipher.getInstance(cipherName4128).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mListener.onSubsUpdated();
            }
        }
        return super.setMeta(meta);
    }

    @Override
    protected PromisedReply<ServerMessage> publish(Drafty content, Map<String, Object> head, long id) {
        String cipherName4129 =  "DES";
		try{
			android.util.Log.d("cipherName-4129", javax.crypto.Cipher.getInstance(cipherName4129).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		throw new UnsupportedOperationException();
    }

    /**
     * Add subscription to cache. Needs to be overridden in FndTopic because it keeps subs indexed
     * by either user or topic value.
     *
     * @param sub subscription to add to cache
     */
    @Override
    protected void addSubToCache(Subscription<SP,String[]> sub) {
        String cipherName4130 =  "DES";
		try{
			android.util.Log.d("cipherName-4130", javax.crypto.Cipher.getInstance(cipherName4130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mSubs == null) {
            String cipherName4131 =  "DES";
			try{
				android.util.Log.d("cipherName-4131", javax.crypto.Cipher.getInstance(cipherName4131).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mSubs = new HashMap<>();
        }
        mSubs.put(sub.getUnique(), sub);
    }

    @Override
    protected void routeMetaSub(MsgServerMeta<String, String,SP,String[]> meta) {
        String cipherName4132 =  "DES";
		try{
			android.util.Log.d("cipherName-4132", javax.crypto.Cipher.getInstance(cipherName4132).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (Subscription<SP,String[]> upd : meta.sub) {
            String cipherName4133 =  "DES";
			try{
				android.util.Log.d("cipherName-4133", javax.crypto.Cipher.getInstance(cipherName4133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Subscription<SP,String[]> sub = getSubscription(upd.getUnique());
            if (sub != null) {
                String cipherName4134 =  "DES";
				try{
					android.util.Log.d("cipherName-4134", javax.crypto.Cipher.getInstance(cipherName4134).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sub.merge(upd);
            } else {
                String cipherName4135 =  "DES";
				try{
					android.util.Log.d("cipherName-4135", javax.crypto.Cipher.getInstance(cipherName4135).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sub = upd;
                addSubToCache(sub);
            }

            if (mListener != null) {
                String cipherName4136 =  "DES";
				try{
					android.util.Log.d("cipherName-4136", javax.crypto.Cipher.getInstance(cipherName4136).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mListener.onMetaSub(sub);
            }
        }

        if (mListener != null) {
            String cipherName4137 =  "DES";
			try{
				android.util.Log.d("cipherName-4137", javax.crypto.Cipher.getInstance(cipherName4137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mListener.onSubsUpdated();
        }
    }

    @Override
    public Subscription<SP,String[]> getSubscription(String key) {
        String cipherName4138 =  "DES";
		try{
			android.util.Log.d("cipherName-4138", javax.crypto.Cipher.getInstance(cipherName4138).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mSubs != null ? mSubs.get(key) : null;
    }

    @Override
    public Collection<Subscription<SP,String[]>> getSubscriptions() {
        String cipherName4139 =  "DES";
		try{
			android.util.Log.d("cipherName-4139", javax.crypto.Cipher.getInstance(cipherName4139).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mSubs != null ? mSubs.values() : null;
    }

    @Override
    protected void setStorage(Storage store) {
		String cipherName4140 =  "DES";
		try{
			android.util.Log.d("cipherName-4140", javax.crypto.Cipher.getInstance(cipherName4140).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        /* Do nothing: all fnd data is transient. */
    }

    public static class FndListener<SP> implements Listener<String, String, SP, String[]> {
        /** {meta} message received */
        public void onMeta(MsgServerMeta<String, String, SP, String[]> meta) {
			String cipherName4141 =  "DES";
			try{
				android.util.Log.d("cipherName-4141", javax.crypto.Cipher.getInstance(cipherName4141).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}}
    }
}
