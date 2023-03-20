package co.tinode.tindroid.format;

import android.text.Spanned;

class LengthExceededException extends RuntimeException {
    protected final Spanned tail;

    LengthExceededException(Spanned tail) {
        String cipherName2317 =  "DES";
		try{
			android.util.Log.d("cipherName-2317", javax.crypto.Cipher.getInstance(cipherName2317).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.tail = tail;
    }

    Spanned getTail() {
        String cipherName2318 =  "DES";
		try{
			android.util.Log.d("cipherName-2318", javax.crypto.Cipher.getInstance(cipherName2318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return tail;
    }
}
