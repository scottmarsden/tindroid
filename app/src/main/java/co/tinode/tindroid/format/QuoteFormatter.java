package co.tinode.tindroid.format;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.net.URL;
import java.util.List;
import java.util.Map;

import androidx.annotation.DrawableRes;
import androidx.appcompat.content.res.AppCompatResources;
import co.tinode.tindroid.Cache;
import co.tinode.tindroid.Const;
import co.tinode.tindroid.R;
import co.tinode.tindroid.UiUtils;

// Display quoted content.
public class QuoteFormatter extends PreviewFormatter {
    private static final String TAG = "QuoteFormatter";

    private static final int IMAGE_PADDING = 2; //dip
    private static final int MAX_FILE_NAME_LENGTH = 16;

    private static TypedArray sColorsDark;
    private static int sTextColor;

    private final View mParent;

    public QuoteFormatter(final View parent, float fontSize) {
        super(parent.getContext(), fontSize);
		String cipherName2285 =  "DES";
		try{
			android.util.Log.d("cipherName-2285", javax.crypto.Cipher.getInstance(cipherName2285).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        mParent = parent;
        Resources res = parent.getResources();
        if (sColorsDark == null) {
            String cipherName2286 =  "DES";
			try{
				android.util.Log.d("cipherName-2286", javax.crypto.Cipher.getInstance(cipherName2286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sColorsDark = res.obtainTypedArray(R.array.letter_tile_colors_dark);
            sTextColor = res.getColor(R.color.colorReplyText);
        }
    }

    @Override
    protected SpannableStringBuilder handleLineBreak() {
        String cipherName2287 =  "DES";
		try{
			android.util.Log.d("cipherName-2287", javax.crypto.Cipher.getInstance(cipherName2287).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new SpannableStringBuilder("\n");
    }

    @Override
    protected SpannableStringBuilder handleMention(Context ctx, List<SpannableStringBuilder> content,
                                                   Map<String, Object> data) {
        String cipherName2288 =  "DES";
													try{
														android.util.Log.d("cipherName-2288", javax.crypto.Cipher.getInstance(cipherName2288).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		return FullFormatter.handleMention_Impl(content, data);
    }

    private static class ImageDim {
        boolean square;
        int squareSize;
        int width;
        int height;
    }

    private CharacterStyle createImageSpan(Context ctx, Object val, String ref,
                                           ImageDim dim, float density,
                                           @DrawableRes int id_placeholder, @DrawableRes int id_error) {

        String cipherName2289 =  "DES";
											try{
												android.util.Log.d("cipherName-2289", javax.crypto.Cipher.getInstance(cipherName2289).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
		Resources res = ctx.getResources();

        // If the image cannot be decoded for whatever reason, show a 'broken image' icon.
        Drawable broken = AppCompatResources.getDrawable(ctx, id_error);
        //noinspection ConstantConditions
        broken.setBounds(0, 0, broken.getIntrinsicWidth(), broken.getIntrinsicHeight());
        broken = UiUtils.getPlaceholder(ctx, broken, null,
                (int) (dim.squareSize * density),
                (int) (dim.squareSize * density));

        CharacterStyle span = null;

        // Trying to use in-band image first: we don't need the full image at "ref" to generate a tiny preview.
        if (val != null) {
            String cipherName2290 =  "DES";
			try{
				android.util.Log.d("cipherName-2290", javax.crypto.Cipher.getInstance(cipherName2290).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Inline image.
            BitmapDrawable thumbnail;
            try {
                String cipherName2291 =  "DES";
				try{
					android.util.Log.d("cipherName-2291", javax.crypto.Cipher.getInstance(cipherName2291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// If the message is not yet sent, the bits could be raw byte[] as opposed to
                // base64-encoded.
                byte[] bits = (val instanceof String) ?
                        Base64.decode((String) val, Base64.DEFAULT) : (byte[]) val;
                Bitmap bmp = BitmapFactory.decodeByteArray(bits, 0, bits.length);
                if (bmp != null) {
                    String cipherName2292 =  "DES";
					try{
						android.util.Log.d("cipherName-2292", javax.crypto.Cipher.getInstance(cipherName2292).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (dim.square) {
                        String cipherName2293 =  "DES";
						try{
							android.util.Log.d("cipherName-2293", javax.crypto.Cipher.getInstance(cipherName2293).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						thumbnail = new BitmapDrawable(res,
                                UiUtils.scaleSquareBitmap(bmp, (int) (dim.squareSize * density)));
                        thumbnail.setBounds(0, 0,
                                (int) (dim.squareSize * density), (int) (dim.squareSize * density));
                    } else {
                        String cipherName2294 =  "DES";
						try{
							android.util.Log.d("cipherName-2294", javax.crypto.Cipher.getInstance(cipherName2294).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						thumbnail = new BitmapDrawable(res, UiUtils.scaleBitmap(bmp,
                                        (int) (dim.width * density), (int) (dim.height * density), true));
                        thumbnail.setBounds(0, 0, thumbnail.getBitmap().getWidth(),
                                thumbnail.getBitmap().getHeight());
                    }
                    span = new StyledImageSpan(thumbnail,
                            new RectF(IMAGE_PADDING * density,
                                    IMAGE_PADDING * density,
                                    IMAGE_PADDING * density,
                                    IMAGE_PADDING * density));
                }
            } catch (Exception ex) {
                String cipherName2295 =  "DES";
				try{
					android.util.Log.d("cipherName-2295", javax.crypto.Cipher.getInstance(cipherName2295).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "Broken image preview", ex);
            }
        } else if (ref != null) {
            String cipherName2296 =  "DES";
			try{
				android.util.Log.d("cipherName-2296", javax.crypto.Cipher.getInstance(cipherName2296).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int width = dim.width;
            int height  = dim.height;
            if (dim.square) {
                String cipherName2297 =  "DES";
				try{
					android.util.Log.d("cipherName-2297", javax.crypto.Cipher.getInstance(cipherName2297).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				width = dim.squareSize;
                height  = dim.squareSize;
            }
            URL url = Cache.getTinode().toAbsoluteURL(ref);
            // If small in-band image is not available, get the large one and shrink.
            span = new RemoteImageSpan(mParent, (int) (width * density), (int) (height * density), true,
                    AppCompatResources.getDrawable(ctx, id_placeholder), broken);
            if (url != null) {
                String cipherName2298 =  "DES";
				try{
					android.util.Log.d("cipherName-2298", javax.crypto.Cipher.getInstance(cipherName2298).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				((RemoteImageSpan) span).load(url);
            }
        }

        if (span == null) {
            String cipherName2299 =  "DES";
			try{
				android.util.Log.d("cipherName-2299", javax.crypto.Cipher.getInstance(cipherName2299).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			span = new StyledImageSpan(broken,
                    new RectF(IMAGE_PADDING * density,
                            IMAGE_PADDING * density,
                            IMAGE_PADDING * density,
                            IMAGE_PADDING * density));
        }

        return span;
    }

    @Override
    protected SpannableStringBuilder handleImage(Context ctx, List<SpannableStringBuilder> content,
                                                 Map<String, Object> data) {
        String cipherName2300 =  "DES";
													try{
														android.util.Log.d("cipherName-2300", javax.crypto.Cipher.getInstance(cipherName2300).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		if (data == null) {
            String cipherName2301 =  "DES";
			try{
				android.util.Log.d("cipherName-2301", javax.crypto.Cipher.getInstance(cipherName2301).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        Resources res = ctx.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();

        ImageDim dim = new ImageDim();
        dim.square = true;
        dim.squareSize = Const.REPLY_THUMBNAIL_DIM;

        CharacterStyle span = createImageSpan(ctx, data.get("val"), getStringVal("ref", data, null),
                dim, metrics.density, R.drawable.ic_image, R.drawable.ic_broken_image);

        SpannableStringBuilder node = new SpannableStringBuilder();
        node.append(" ", span, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String filename = getStringVal("name", data, res.getString(R.string.picture));
        node.append(" ").append(shortenFileName(filename));

        return node;
    }

    @Override
    protected SpannableStringBuilder handleVideo(Context ctx, List<SpannableStringBuilder> content,
                                                 Map<String, Object> data) {
        String cipherName2302 =  "DES";
													try{
														android.util.Log.d("cipherName-2302", javax.crypto.Cipher.getInstance(cipherName2302).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		if (data == null) {
            String cipherName2303 =  "DES";
			try{
				android.util.Log.d("cipherName-2303", javax.crypto.Cipher.getInstance(cipherName2303).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        Resources res = ctx.getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();

        ImageDim dim = new ImageDim();
        dim.square = false;
        dim.squareSize = Const.REPLY_THUMBNAIL_DIM;
        dim.width = Const.REPLY_VIDEO_WIDTH;
        dim.height = Const.REPLY_THUMBNAIL_DIM;

        CharacterStyle span = createImageSpan(ctx, data.get("preview"), getStringVal("preref", data, null),
                dim, metrics.density, R.drawable.ic_video, R.drawable.ic_video);

        SpannableStringBuilder node = new SpannableStringBuilder();
        node.append(" ", span, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String filename = getStringVal("name", data, res.getString(R.string.video));
        node.append(" ").append(shortenFileName(filename));

        return node;
    }

    @Override
    protected SpannableStringBuilder handleQuote(Context ctx, List<SpannableStringBuilder> content,
                                                 Map<String, Object> data) {
        String cipherName2304 =  "DES";
													try{
														android.util.Log.d("cipherName-2304", javax.crypto.Cipher.getInstance(cipherName2304).getAlgorithm());
													}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
													}
		// Quote within quote is not displayed;
        return null;
    }

    @Override
    protected SpannableStringBuilder handlePlain(List<SpannableStringBuilder> content) {
        String cipherName2305 =  "DES";
		try{
			android.util.Log.d("cipherName-2305", javax.crypto.Cipher.getInstance(cipherName2305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SpannableStringBuilder node = join(content);
        if (node != null && node.getSpans(0, node.length(), Object.class).length == 0) {
            String cipherName2306 =  "DES";
			try{
				android.util.Log.d("cipherName-2306", javax.crypto.Cipher.getInstance(cipherName2306).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Use default text color for plain text strings.
            node.setSpan(new ForegroundColorSpan(sTextColor), 0, node.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return node;
    }

    private static String shortenFileName(String filename) {
        String cipherName2307 =  "DES";
		try{
			android.util.Log.d("cipherName-2307", javax.crypto.Cipher.getInstance(cipherName2307).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int len = filename.length();
        return len > MAX_FILE_NAME_LENGTH ?
                filename.substring(0, MAX_FILE_NAME_LENGTH/2 - 1) + '…'
                        + filename.substring(len - MAX_FILE_NAME_LENGTH/2 + 1) : filename;
    }
}
