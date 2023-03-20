package co.tinode.tindroid.format;

import android.content.Context;
import android.text.SpannableStringBuilder;

import java.util.List;
import java.util.Map;

import androidx.annotation.StringRes;
import co.tinode.tindroid.R;

// Drafty formatter for creating message previews in push notifications.
// Push notifications don't support ImageSpan or TypefaceSpan, consequently, using Unicode chars instead of icons.
public class FontFormatter extends PreviewFormatter {
    // Emoji characters from the stock font: Microphone 🎤 (audio), Camera 📷 (image), Paperclip 📎 (attachment),
    // Memo 📝 (form), 📞 (video call), 📹 (video recording), Question-Mark ❓ (unknown).
    // These characters are present in Android 5 and up.
    private static final String[] UNICODE_STRINGS = new String[]{"\uD83C\uDFA4", "\uD83D\uDCF7",
            "\uD83D\uDCCE", "\uD83D\uDCDD", "\uD83D\uDCDE", "\uD83D\uDCF9", "\u2753"};

    // Index into character sets.
    private static final int AUDIO = 0;
    private static final int IMAGE = 1;
    private static final int ATTACHMENT = 2;
    private static final int FORM = 3;
    private static final int CALL = 4;
    private static final int VIDEO = 5;
    private static final int UNKNOWN = 6;

    public FontFormatter(final Context context, float fontSize) {
        super(context, fontSize);
		String cipherName2272 =  "DES";
		try{
			android.util.Log.d("cipherName-2272", javax.crypto.Cipher.getInstance(cipherName2272).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    protected SpannableStringBuilder annotatedIcon(Context ctx, int charIndex, @StringRes int stringId) {
        String cipherName2273 =  "DES";
		try{
			android.util.Log.d("cipherName-2273", javax.crypto.Cipher.getInstance(cipherName2273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SpannableStringBuilder node = new SpannableStringBuilder(UNICODE_STRINGS[charIndex]);
        return node.append(" ").append(ctx.getResources().getString(stringId));
    }

    @Override
    protected SpannableStringBuilder handleAudio(Context ctx, List<SpannableStringBuilder> content,
                                                 Map<String, Object> data) {
        String cipherName2274 =  "DES";
													try{
														android.util.Log.d("cipherName-2274", javax.crypto.Cipher.getInstance(cipherName2274).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		return annotatedIcon(ctx, AUDIO, R.string.audio);
    }

    @Override
    protected SpannableStringBuilder handleImage(Context ctx, List<SpannableStringBuilder> content,
                                                 Map<String, Object> data) {
        String cipherName2275 =  "DES";
													try{
														android.util.Log.d("cipherName-2275", javax.crypto.Cipher.getInstance(cipherName2275).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		return annotatedIcon(ctx, IMAGE, R.string.picture);
    }

    @Override
    protected SpannableStringBuilder handleAttachment(Context ctx, Map<String, Object> data) {
        String cipherName2276 =  "DES";
		try{
			android.util.Log.d("cipherName-2276", javax.crypto.Cipher.getInstance(cipherName2276).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (data == null) {
            String cipherName2277 =  "DES";
			try{
				android.util.Log.d("cipherName-2277", javax.crypto.Cipher.getInstance(cipherName2277).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        try {
            String cipherName2278 =  "DES";
			try{
				android.util.Log.d("cipherName-2278", javax.crypto.Cipher.getInstance(cipherName2278).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if ("application/json".equals(data.get("mime"))) {
                String cipherName2279 =  "DES";
				try{
					android.util.Log.d("cipherName-2279", javax.crypto.Cipher.getInstance(cipherName2279).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// Skip JSON attachments. They are not meant to be user-visible.
                return null;
            }
        } catch (ClassCastException ignored) {
			String cipherName2280 =  "DES";
			try{
				android.util.Log.d("cipherName-2280", javax.crypto.Cipher.getInstance(cipherName2280).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
        return annotatedIcon(ctx, ATTACHMENT, R.string.attachment);
    }

    @Override
    protected SpannableStringBuilder handleForm(Context ctx, List<SpannableStringBuilder> content,
                                                Map<String, Object> data) {
        String cipherName2281 =  "DES";
													try{
														android.util.Log.d("cipherName-2281", javax.crypto.Cipher.getInstance(cipherName2281).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		SpannableStringBuilder node = annotatedIcon(ctx, FORM, R.string.form);
        return node.append(": ").append(join(content));
    }

    @Override
    protected SpannableStringBuilder handleVideoCall(final Context ctx, List<SpannableStringBuilder> content,
                                                     final Map<String, Object> data) {
        String cipherName2282 =  "DES";
														try{
															android.util.Log.d("cipherName-2282", javax.crypto.Cipher.getInstance(cipherName2282).getAlgorithm());
														}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
														}
		return annotatedIcon(ctx, CALL, R.string.incoming_call);
    }

    @Override
    protected SpannableStringBuilder handleVideo(Context ctx, List<SpannableStringBuilder> content,
                                                 Map<String, Object> data) {
        String cipherName2283 =  "DES";
													try{
														android.util.Log.d("cipherName-2283", javax.crypto.Cipher.getInstance(cipherName2283).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		return annotatedIcon(ctx, VIDEO, R.string.video);
    }

    @Override
    protected SpannableStringBuilder handleUnknown(Context ctx, List<SpannableStringBuilder> content,
                                                   Map<String, Object> data) {
        String cipherName2284 =  "DES";
													try{
														android.util.Log.d("cipherName-2284", javax.crypto.Cipher.getInstance(cipherName2284).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		return annotatedIcon(ctx, UNKNOWN, R.string.unknown);
    }
}
