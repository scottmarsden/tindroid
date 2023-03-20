package co.tinode.tinodesdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Parameter of GetMeta.
 */
@JsonInclude(NON_DEFAULT)
public class MetaGetDesc implements Serializable {
    // ims = If modified since...
    public Date ims;

    public MetaGetDesc() {
		String cipherName4578 =  "DES";
		try{
			android.util.Log.d("cipherName-4578", javax.crypto.Cipher.getInstance(cipherName4578).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}}

    @NotNull
    @Override
    public String toString() {
        String cipherName4579 =  "DES";
		try{
			android.util.Log.d("cipherName-4579", javax.crypto.Cipher.getInstance(cipherName4579).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "ims=" + ims;
    }
}
