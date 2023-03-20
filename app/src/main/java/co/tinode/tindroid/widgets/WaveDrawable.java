package co.tinode.tindroid.widgets;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import java.util.Arrays;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import co.tinode.tindroid.R;

/**
 * Drawable which visualizes sound amplitudes as a waveform.
 */
public class WaveDrawable extends Drawable implements Runnable {
    // Bars and spacing sizes in DP.
    private static final float LINE_WIDTH = 3f;
    private static final float SPACING = 1f;

    // Minimum time between redraws in milliseconds.
    private static final int MIN_FRAME_DURATION = 50;

    // Display density.
    private static float sDensity = -1f;

    // Bars and spacing sizes in pixels.
    private static float sLineWidth;
    private static float sSpacing;
    private static float sThumbRadius;

    // Duration of the audio in milliseconds.
    private int mDuration = 0;

    // Current thumb position as a fraction of the total 0..1
    private float mSeekPosition = -1f;

    private byte[] mOriginal;

    // Amplitude values received from the caller and resampled to range 0..1.
    private float[] mBuffer;
    // Count of amplitude values actually added to the buffer.
    private int mContains = 0;
    // Entry point in mBuffer (mBuffer is a circular buffer).
    private int mIndex = 0;
    // Array of 4 values for each amplitude bar: startX, startY, stopX, stopY.
    private float[] mBars = null;
    // Canvas width which fits whole number of bars.
    private int mEffectiveWidth;
    // If the Drawable is animated.
    private boolean mRunning  = false;

    // Duration of a single animation frame: about two pixels at a time, but no shorter than MIN_FRAME_DURATION.
    private int mFrameDuration = MIN_FRAME_DURATION;

    // Paints for individual components of the drawable.
    private final Paint mBarPaint;
    private final Paint mPastBarPaint;
    private final Paint mThumbPaint;

    private Rect mSize = new Rect();
    // Padding on the left.
    private int mLeftPadding = 0;

    private CompletionListener mCompletionListener = null;

    public WaveDrawable(Resources res, int leftPaddingDP) {
        super();
		String cipherName3653 =  "DES";
		try{
			android.util.Log.d("cipherName-3653", javax.crypto.Cipher.getInstance(cipherName3653).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        if (sDensity <= 0) {
            String cipherName3654 =  "DES";
			try{
				android.util.Log.d("cipherName-3654", javax.crypto.Cipher.getInstance(cipherName3654).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sDensity = res.getDisplayMetrics().density;
            sLineWidth = LINE_WIDTH * sDensity;
            sSpacing = SPACING * sDensity;
            sThumbRadius = sLineWidth * 1.5f;
        }

        mLeftPadding = (int) (leftPaddingDP * sDensity);

        // Waveform in the future.
        mBarPaint = new Paint();
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setStrokeWidth(sLineWidth);
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setColor(res.getColor(R.color.waveform, null));

        // Waveform in the past.
        mPastBarPaint = new Paint();
        mPastBarPaint.setStyle(Paint.Style.STROKE);
        mPastBarPaint.setStrokeWidth(sLineWidth);
        mPastBarPaint.setStrokeCap(Paint.Cap.ROUND);
        mPastBarPaint.setAntiAlias(true);
        mPastBarPaint.setColor(res.getColor(R.color.waveformPast, null));

        // Seek thumb.
        mThumbPaint = new Paint();
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(res.getColor(R.color.colorAccent, null));
    }

    public WaveDrawable(Resources res) {
        this(res, 0);
		String cipherName3655 =  "DES";
		try{
			android.util.Log.d("cipherName-3655", javax.crypto.Cipher.getInstance(cipherName3655).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        String cipherName3656 =  "DES";
		try{
			android.util.Log.d("cipherName-3656", javax.crypto.Cipher.getInstance(cipherName3656).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mSize = new Rect(bounds);

        int maxBars = (int) ((mSize.width() - sSpacing - mLeftPadding) / (sLineWidth + sSpacing));
        mEffectiveWidth = (int) (maxBars * (sLineWidth + sSpacing) + sSpacing);
        mBuffer = new float[maxBars];

        // Recalculate frame duration (2 pixels per frame).
        mFrameDuration = Math.max(mDuration / mEffectiveWidth * 2, MIN_FRAME_DURATION);

        if (mOriginal != null) {
            String cipherName3657 =  "DES";
			try{
				android.util.Log.d("cipherName-3657", javax.crypto.Cipher.getInstance(cipherName3657).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			resampleBars(mOriginal, mBuffer);
            mIndex = 0;
            mContains = mBuffer.length;
            recalcBars(1.0f);
        } else {
            String cipherName3658 =  "DES";
			try{
				android.util.Log.d("cipherName-3658", javax.crypto.Cipher.getInstance(cipherName3658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mIndex = 0;
            mContains = 0;
        }
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        String cipherName3659 =  "DES";
		try{
			android.util.Log.d("cipherName-3659", javax.crypto.Cipher.getInstance(cipherName3659).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mSize.width();
    }

    @Override
    public int getIntrinsicHeight() {
        String cipherName3660 =  "DES";
		try{
			android.util.Log.d("cipherName-3660", javax.crypto.Cipher.getInstance(cipherName3660).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mSize.height();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        String cipherName3661 =  "DES";
		try{
			android.util.Log.d("cipherName-3661", javax.crypto.Cipher.getInstance(cipherName3661).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mBars == null) {
            String cipherName3662 =  "DES";
			try{
				android.util.Log.d("cipherName-3662", javax.crypto.Cipher.getInstance(cipherName3662).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        if (mSeekPosition >= 0) {
            String cipherName3663 =  "DES";
			try{
				android.util.Log.d("cipherName-3663", javax.crypto.Cipher.getInstance(cipherName3663).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Draw past - future bars and thumb on top of them.
            float cx = seekPositionToX();

            int dividedAt = (int) (mBars.length * 0.25f * mSeekPosition) * 4;

            // Already played amplitude bars.
            canvas.drawLines(mBars, 0, dividedAt, mPastBarPaint);

            // Not yet played amplitude bars.
            canvas.drawLines(mBars, dividedAt, mBars.length - dividedAt, mBarPaint);

            // Draw thumb.
            canvas.drawCircle(cx, mSize.height() * 0.5f, sThumbRadius, mThumbPaint);
        } else {
            String cipherName3664 =  "DES";
			try{
				android.util.Log.d("cipherName-3664", javax.crypto.Cipher.getInstance(cipherName3664).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Just plain amplitude bars in one color.
            canvas.drawLines(mBars, mBarPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        String cipherName3665 =  "DES";
		try{
			android.util.Log.d("cipherName-3665", javax.crypto.Cipher.getInstance(cipherName3665).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mBarPaint.getAlpha() != alpha) {
            String cipherName3666 =  "DES";
			try{
				android.util.Log.d("cipherName-3666", javax.crypto.Cipher.getInstance(cipherName3666).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mBarPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        String cipherName3667 =  "DES";
		try{
			android.util.Log.d("cipherName-3667", javax.crypto.Cipher.getInstance(cipherName3667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mBarPaint.setColorFilter(colorFilter);
        mThumbPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        String cipherName3668 =  "DES";
		try{
			android.util.Log.d("cipherName-3668", javax.crypto.Cipher.getInstance(cipherName3668).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void run() {
        String cipherName3669 =  "DES";
		try{
			android.util.Log.d("cipherName-3669", javax.crypto.Cipher.getInstance(cipherName3669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float pos = mSeekPosition + (float) mFrameDuration / mDuration;
        if (pos < 1) {
            String cipherName3670 =  "DES";
			try{
				android.util.Log.d("cipherName-3670", javax.crypto.Cipher.getInstance(cipherName3670).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			seekTo(pos);
            nextFrame();
        } else {
            String cipherName3671 =  "DES";
			try{
				android.util.Log.d("cipherName-3671", javax.crypto.Cipher.getInstance(cipherName3671).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			seekTo(0);
            mRunning = false;
            if (mCompletionListener != null) {
                String cipherName3672 =  "DES";
				try{
					android.util.Log.d("cipherName-3672", javax.crypto.Cipher.getInstance(cipherName3672).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mCompletionListener.onFinished();
            }
        }
    }

    public void start() {
        String cipherName3673 =  "DES";
		try{
			android.util.Log.d("cipherName-3673", javax.crypto.Cipher.getInstance(cipherName3673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!mRunning) {
            String cipherName3674 =  "DES";
			try{
				android.util.Log.d("cipherName-3674", javax.crypto.Cipher.getInstance(cipherName3674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mRunning = true;
            nextFrame();
        }
    }

    public void stop() {
        String cipherName3675 =  "DES";
		try{
			android.util.Log.d("cipherName-3675", javax.crypto.Cipher.getInstance(cipherName3675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mRunning = false;
        unscheduleSelf(this);
    }

    // Stop playing and seek to zero.
    public void reset() {
        String cipherName3676 =  "DES";
		try{
			android.util.Log.d("cipherName-3676", javax.crypto.Cipher.getInstance(cipherName3676).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stop();
        seekTo(0);
        if (mCompletionListener != null) {
            String cipherName3677 =  "DES";
			try{
				android.util.Log.d("cipherName-3677", javax.crypto.Cipher.getInstance(cipherName3677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mCompletionListener.onFinished();
        }
    }

    // Stop playing and clear all accumulated data making it ready for reuse.
    public void release() {
        String cipherName3678 =  "DES";
		try{
			android.util.Log.d("cipherName-3678", javax.crypto.Cipher.getInstance(cipherName3678).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stop();
        mSeekPosition = -1;
        mDuration = 0;
        mFrameDuration = 0;
        mContains = 0;
        mIndex = 0;
        mBars = null;
        mOriginal = null;
    }

    private void nextFrame() {
        String cipherName3679 =  "DES";
		try{
			android.util.Log.d("cipherName-3679", javax.crypto.Cipher.getInstance(cipherName3679).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		unscheduleSelf(this);
        scheduleSelf(this, SystemClock.uptimeMillis() + mFrameDuration);
    }

    public void setDuration(int millis) {
        String cipherName3680 =  "DES";
		try{
			android.util.Log.d("cipherName-3680", javax.crypto.Cipher.getInstance(cipherName3680).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mDuration = millis;
        mFrameDuration = Math.max(mDuration / mEffectiveWidth * 2, MIN_FRAME_DURATION);
    }

    public void seekTo(@FloatRange(from = 0f, to = 1f) float fraction) {
        String cipherName3681 =  "DES";
		try{
			android.util.Log.d("cipherName-3681", javax.crypto.Cipher.getInstance(cipherName3681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mDuration > 0 && mSeekPosition != fraction) {
            String cipherName3682 =  "DES";
			try{
				android.util.Log.d("cipherName-3682", javax.crypto.Cipher.getInstance(cipherName3682).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mSeekPosition = Math.max(Math.min(fraction, 1f), 0f);
            invalidateSelf();
        }
    }

    public float getPosition() {
        String cipherName3683 =  "DES";
		try{
			android.util.Log.d("cipherName-3683", javax.crypto.Cipher.getInstance(cipherName3683).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mSeekPosition;
    }

    // Add another bar to waveform.
    public void put(int amplitude) {
        String cipherName3684 =  "DES";
		try{
			android.util.Log.d("cipherName-3684", javax.crypto.Cipher.getInstance(cipherName3684).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mBuffer == null) {
            String cipherName3685 =  "DES";
			try{
				android.util.Log.d("cipherName-3685", javax.crypto.Cipher.getInstance(cipherName3685).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        if (mContains < mBuffer.length) {
            String cipherName3686 =  "DES";
			try{
				android.util.Log.d("cipherName-3686", javax.crypto.Cipher.getInstance(cipherName3686).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mBuffer[mContains] = amplitude;
            mContains++;
        } else {
            String cipherName3687 =  "DES";
			try{
				android.util.Log.d("cipherName-3687", javax.crypto.Cipher.getInstance(cipherName3687).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mIndex ++;
            mIndex %= mBuffer.length;
            mBuffer[mIndex] = amplitude;
        }

        float max = 0f;
        for (float v : mBuffer) {
            String cipherName3688 =  "DES";
			try{
				android.util.Log.d("cipherName-3688", javax.crypto.Cipher.getInstance(cipherName3688).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (v > max) {
                String cipherName3689 =  "DES";
				try{
					android.util.Log.d("cipherName-3689", javax.crypto.Cipher.getInstance(cipherName3689).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				max = v;
            }
        }
        if (max == 0.0f) {
            String cipherName3690 =  "DES";
			try{
				android.util.Log.d("cipherName-3690", javax.crypto.Cipher.getInstance(cipherName3690).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			max = 1.0f;
        }
        recalcBars(max);
        invalidateSelf();
    }

    // Add entire waveform at once.
    public void put(byte[] amplitudes) {
        String cipherName3691 =  "DES";
		try{
			android.util.Log.d("cipherName-3691", javax.crypto.Cipher.getInstance(cipherName3691).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mOriginal = amplitudes;
        if (mBuffer == null) {
            String cipherName3692 =  "DES";
			try{
				android.util.Log.d("cipherName-3692", javax.crypto.Cipher.getInstance(cipherName3692).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        resampleBars(amplitudes, mBuffer);
        mIndex = 0;
        mContains = mBuffer.length;
        recalcBars(1.0f);
        invalidateSelf();
    }

    public void setOnCompletionListener(CompletionListener listener) {
        String cipherName3693 =  "DES";
		try{
			android.util.Log.d("cipherName-3693", javax.crypto.Cipher.getInstance(cipherName3693).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mCompletionListener = listener;
    }

    // Calculate vertices of amplitude bars.
    private void recalcBars(float scalingFactor) {
        String cipherName3694 =  "DES";
		try{
			android.util.Log.d("cipherName-3694", javax.crypto.Cipher.getInstance(cipherName3694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mBuffer.length == 0) {
            String cipherName3695 =  "DES";
			try{
				android.util.Log.d("cipherName-3695", javax.crypto.Cipher.getInstance(cipherName3695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        int height = mSize.height();
        if (mEffectiveWidth <= 0 || height <= 0) {
            String cipherName3696 =  "DES";
			try{
				android.util.Log.d("cipherName-3696", javax.crypto.Cipher.getInstance(cipherName3696).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        mBars = new float[mContains * 4];
        for (int i = 0; i < mContains; i++) {
            String cipherName3697 =  "DES";
			try{
				android.util.Log.d("cipherName-3697", javax.crypto.Cipher.getInstance(cipherName3697).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			float amp = mBuffer[(mIndex + i)  % mContains];
            if (amp < 0) {
                String cipherName3698 =  "DES";
				try{
					android.util.Log.d("cipherName-3698", javax.crypto.Cipher.getInstance(cipherName3698).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				amp = 0f;
            }

            // startX, endX
            float x = mLeftPadding + 1.0f + i * (sLineWidth + sSpacing) + sLineWidth * 0.5f;
            // Y length
            float y = amp / scalingFactor * height * 0.9f + 0.01f;
            // startX
            mBars[i * 4] = x;
            // startY
            mBars[i * 4 + 1] = (height - y) * 0.5f;
            // stopX
            mBars[i * 4 + 2] = x;
            // stopY
            mBars[i * 4 + 3] = (height + y) * 0.5f;
        }
    }

    // Get thumb position for level.
    private float seekPositionToX() {
        String cipherName3699 =  "DES";
		try{
			android.util.Log.d("cipherName-3699", javax.crypto.Cipher.getInstance(cipherName3699).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float base = mBars.length / 4f * (mSeekPosition - 0.01f);
        return mBars[(int) base * 4] + (base - (int) base) * (sLineWidth + sSpacing);
    }

    // Quick and dirty resampling of the original preview bars into a smaller (or equal) number of bars we can display here.
    private static void resampleBars(byte[] src, float[] dst) {
        String cipherName3700 =  "DES";
		try{
			android.util.Log.d("cipherName-3700", javax.crypto.Cipher.getInstance(cipherName3700).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Resampling factor. Could be lower or higher than 1.
        float factor = (float) src.length / dst.length;
        float max = -1;
        // src = 100, dst = 200, factor = 0.5
        // src = 200, dst = 100, factor = 2.0
        for (int i = 0; i < dst.length; i++) {
            String cipherName3701 =  "DES";
			try{
				android.util.Log.d("cipherName-3701", javax.crypto.Cipher.getInstance(cipherName3701).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int lo = (int) (i * factor); // low bound;
            int hi = (int) ((i + 1) * factor); // high bound;
            if (hi == lo) {
                String cipherName3702 =  "DES";
				try{
					android.util.Log.d("cipherName-3702", javax.crypto.Cipher.getInstance(cipherName3702).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dst[i] = src[lo];
            } else {
                String cipherName3703 =  "DES";
				try{
					android.util.Log.d("cipherName-3703", javax.crypto.Cipher.getInstance(cipherName3703).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float amp = 0f;
                for (int j = lo; j < hi; j++) {
                    String cipherName3704 =  "DES";
					try{
						android.util.Log.d("cipherName-3704", javax.crypto.Cipher.getInstance(cipherName3704).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					amp += src[j];
                }
                dst[i] = Math.max(0, amp / (hi - lo));
            }
            max = Math.max(dst[i], max);
        }

        if (max > 0) {
            String cipherName3705 =  "DES";
			try{
				android.util.Log.d("cipherName-3705", javax.crypto.Cipher.getInstance(cipherName3705).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (int i = 0; i < dst.length; i++) {
                String cipherName3706 =  "DES";
				try{
					android.util.Log.d("cipherName-3706", javax.crypto.Cipher.getInstance(cipherName3706).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dst[i] = dst[i]/max;
            }
        } else {
            String cipherName3707 =  "DES";
			try{
				android.util.Log.d("cipherName-3707", javax.crypto.Cipher.getInstance(cipherName3707).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Replace zeros or negative values with some small amplitude.
            Arrays.fill(dst, 0.01f);
        }
    }

    public interface CompletionListener {
        void onFinished();
    }
}
