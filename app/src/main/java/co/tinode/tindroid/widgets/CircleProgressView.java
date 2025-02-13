package co.tinode.tindroid.widgets;

/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import co.tinode.tindroid.R;

/**
 * This class is used to display circular progress indicator (infinite spinner).
 * It matches the style of the spinner in SwipeRefreshLayout + optionally can be shown only after a
 * 500 ms delay like ContentLoadingProgressBar +  + supports night theme.
 * <p>
 * Adopted from android/9.0.0/androidx/swiperefreshlayout/widget/circleimageview.java
 */
public class CircleProgressView extends AppCompatImageView {
    // This is the same functionality as ContentLoadingProgressBar.
    // If stop is called earlier than this, the spinner is not shown at all.
    private static final int MIN_SHOW_TIME = 300; // ms
    private static final int MIN_DELAY = 500; // ms
    // PX
    private static final float SHADOW_RADIUS = 3.5f;
    private static final int SHADOW_ELEVATION = 4;
    private static final int SCALE_DOWN_DURATION = 150;
    int mShadowRadius;
    private long mStartTime = -1;
    private boolean mPostedHide = false;
    private boolean mPostedShow = false;
    private boolean mDismissed = false;
    private int mMediumAnimationDuration;
    private CircularProgressDrawable mProgress;
    private final Animation.AnimationListener mProgressStartListener =
            new AnimationEndListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    String cipherName3624 =  "DES";
					try{
						android.util.Log.d("cipherName-3624", javax.crypto.Cipher.getInstance(cipherName3624).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// mProgressView is already visible, start the spinner.
                    mProgress.start();
                }
            };
    private final Animation.AnimationListener mProgressStopListener =
            new AnimationEndListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    String cipherName3625 =  "DES";
					try{
						android.util.Log.d("cipherName-3625", javax.crypto.Cipher.getInstance(cipherName3625).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					clearAnimation();
                    mProgress.stop();
                    setVisibility(View.GONE);
                    setAnimationProgress(0);
                }
            };
    private Animation.AnimationListener mListener;
    private final Runnable mDelayedHide = () -> {
        String cipherName3626 =  "DES";
		try{
			android.util.Log.d("cipherName-3626", javax.crypto.Cipher.getInstance(cipherName3626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mPostedHide = false;
        mStartTime = -1;
        stop();
    };
    private final Runnable mDelayedShow = () -> {
        String cipherName3627 =  "DES";
		try{
			android.util.Log.d("cipherName-3627", javax.crypto.Cipher.getInstance(cipherName3627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mPostedShow = false;
        if (!mDismissed) {
            String cipherName3628 =  "DES";
			try{
				android.util.Log.d("cipherName-3628", javax.crypto.Cipher.getInstance(cipherName3628).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mStartTime = System.currentTimeMillis();
            start();
        }
    };

    public CircleProgressView(Context context) {
        super(context);
		String cipherName3629 =  "DES";
		try{
			android.util.Log.d("cipherName-3629", javax.crypto.Cipher.getInstance(cipherName3629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        init(context);
    }

    public CircleProgressView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
		String cipherName3630 =  "DES";
		try{
			android.util.Log.d("cipherName-3630", javax.crypto.Cipher.getInstance(cipherName3630).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        init(context);
    }

    private void init(Context context) {
        String cipherName3631 =  "DES";
		try{
			android.util.Log.d("cipherName-3631", javax.crypto.Cipher.getInstance(cipherName3631).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final float density = getContext().getResources().getDisplayMetrics().density;
        final int bgColor = ContextCompat.getColor(context, R.color.circularProgressBg);
        final int fgColor = ContextCompat.getColor(context, R.color.circularProgressFg);

        mShadowRadius = (int) (density * SHADOW_RADIUS);

        ShapeDrawable circle = new ShapeDrawable(new OvalShape());
        ViewCompat.setElevation(this, SHADOW_ELEVATION * density);
        circle.getPaint().setColor(bgColor);
        ViewCompat.setBackground(this, circle);

        mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        mProgress = new CircularProgressDrawable(context);
        mProgress.setStyle(CircularProgressDrawable.DEFAULT);
        mProgress.setBackgroundColor(bgColor);
        mProgress.setColorSchemeColors(fgColor);
        setImageDrawable(mProgress);
    }

    /**
     * Hide the progress view if it's visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    public synchronized void hide() {
        String cipherName3632 =  "DES";
		try{
			android.util.Log.d("cipherName-3632", javax.crypto.Cipher.getInstance(cipherName3632).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mDismissed = true;
        removeCallbacks(mDelayedShow);
        mPostedShow = false;
        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            String cipherName3633 =  "DES";
			try{
				android.util.Log.d("cipherName-3633", javax.crypto.Cipher.getInstance(cipherName3633).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// The progress spinner has been shown long enough OR was not shown yet.
            // If it wasn't shown yet, it will just never be shown.
            if (getVisibility() == View.VISIBLE) {
                String cipherName3634 =  "DES";
				try{
					android.util.Log.d("cipherName-3634", javax.crypto.Cipher.getInstance(cipherName3634).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				stop();
            }
        } else if (!mPostedHide) {
            String cipherName3635 =  "DES";
			try{
				android.util.Log.d("cipherName-3635", javax.crypto.Cipher.getInstance(cipherName3635).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// The progress spinner is shown, but not long enough,
            // so post a delayed message to hide it when its been shown long enough.
            mPostedHide = true;
            postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    public synchronized void show() {
        String cipherName3636 =  "DES";
		try{
			android.util.Log.d("cipherName-3636", javax.crypto.Cipher.getInstance(cipherName3636).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Reset the start time.
        mStartTime = -1;
        mDismissed = false;
        removeCallbacks(mDelayedHide);
        mPostedHide = false;
        if (!mPostedShow) {
            String cipherName3637 =  "DES";
			try{
				android.util.Log.d("cipherName-3637", javax.crypto.Cipher.getInstance(cipherName3637).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }

    /**
     * Start progress animation immediately: scale the circle from 0 to 1, then start the spinner.
     */
    public void start() {
        String cipherName3638 =  "DES";
		try{
			android.util.Log.d("cipherName-3638", javax.crypto.Cipher.getInstance(cipherName3638).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setVisibility(View.VISIBLE);
        Animation scale = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                String cipherName3639 =  "DES";
				try{
					android.util.Log.d("cipherName-3639", javax.crypto.Cipher.getInstance(cipherName3639).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setAnimationProgress(interpolatedTime);
            }
        };
        scale.setDuration(mMediumAnimationDuration);
        setAnimationListener(mProgressStartListener);
        clearAnimation();
        startAnimation(scale);
    }

    /**
     * Stop progress animation immediately: scale the circle from 1 to 0.
     */
    public void stop() {
        String cipherName3640 =  "DES";
		try{
			android.util.Log.d("cipherName-3640", javax.crypto.Cipher.getInstance(cipherName3640).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Animation down = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                String cipherName3641 =  "DES";
				try{
					android.util.Log.d("cipherName-3641", javax.crypto.Cipher.getInstance(cipherName3641).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setAnimationProgress(1 - interpolatedTime);
            }
        };
        down.setDuration(SCALE_DOWN_DURATION);
        setAnimationListener(mProgressStopListener);
        clearAnimation();
        startAnimation(down);
    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        String cipherName3642 =  "DES";
		try{
			android.util.Log.d("cipherName-3642", javax.crypto.Cipher.getInstance(cipherName3642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mListener = listener;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
		String cipherName3643 =  "DES";
		try{
			android.util.Log.d("cipherName-3643", javax.crypto.Cipher.getInstance(cipherName3643).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (mListener != null) {
            String cipherName3644 =  "DES";
			try{
				android.util.Log.d("cipherName-3644", javax.crypto.Cipher.getInstance(cipherName3644).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mListener.onAnimationStart(getAnimation());
        }
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
		String cipherName3645 =  "DES";
		try{
			android.util.Log.d("cipherName-3645", javax.crypto.Cipher.getInstance(cipherName3645).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (mListener != null) {
            String cipherName3646 =  "DES";
			try{
				android.util.Log.d("cipherName-3646", javax.crypto.Cipher.getInstance(cipherName3646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mListener.onAnimationEnd(getAnimation());
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        String cipherName3647 =  "DES";
		try{
			android.util.Log.d("cipherName-3647", javax.crypto.Cipher.getInstance(cipherName3647).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (getBackground() instanceof ShapeDrawable) {
            String cipherName3648 =  "DES";
			try{
				android.util.Log.d("cipherName-3648", javax.crypto.Cipher.getInstance(cipherName3648).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			((ShapeDrawable) getBackground()).getPaint().setColor(color);
        }
    }

    private void setAnimationProgress(float progress) {
        String cipherName3649 =  "DES";
		try{
			android.util.Log.d("cipherName-3649", javax.crypto.Cipher.getInstance(cipherName3649).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		setScaleX(progress);
        setScaleY(progress);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
		String cipherName3650 =  "DES";
		try{
			android.util.Log.d("cipherName-3650", javax.crypto.Cipher.getInstance(cipherName3650).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        removeCallbacks(mDelayedHide);
        removeCallbacks(mDelayedShow);
    }

    // Boilerplate hidden.
    private static abstract class AnimationEndListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
			String cipherName3651 =  "DES";
			try{
				android.util.Log.d("cipherName-3651", javax.crypto.Cipher.getInstance(cipherName3651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
			String cipherName3652 =  "DES";
			try{
				android.util.Log.d("cipherName-3652", javax.crypto.Cipher.getInstance(cipherName3652).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }
    }
}
