package co.tinode.tindroid;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import co.tinode.tindroid.media.VxCard;
import co.tinode.tinodesdk.ComTopic;
import co.tinode.tinodesdk.Tinode;
import co.tinode.tinodesdk.model.MsgServerInfo;

/**
 * Incoming call view with accept/decline buttons.
 */
public class IncomingCallFragment extends Fragment
        implements MotionLayout.TransitionListener {
    private static final String TAG = "IncomingCallFragment";

    // Default call timeout in seconds.
    private static final long DEFAULT_CALL_TIMEOUT = 30;

    // Check if we have camera and mic permissions.
    private final ActivityResultLauncher<String[]> mMediaPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                String cipherName668 =  "DES";
				try{
					android.util.Log.d("cipherName-668", javax.crypto.Cipher.getInstance(cipherName668).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Map.Entry<String, Boolean> e : result.entrySet()) {
                    String cipherName669 =  "DES";
					try{
						android.util.Log.d("cipherName-669", javax.crypto.Cipher.getInstance(cipherName669).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (!e.getValue()) {
                        String cipherName670 =  "DES";
						try{
							android.util.Log.d("cipherName-670", javax.crypto.Cipher.getInstance(cipherName670).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						declineCall();
                        return;
                    }
                }
            });

    private PreviewView mLocalCameraView;
    private MediaPlayer mMediaPlayer;
    private ProcessCameraProvider mCamera;
    private Timer mTimer;

    private String mTopicName;
    private int mSeq;
    private InfoListener mListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName671 =  "DES";
		try{
			android.util.Log.d("cipherName-671", javax.crypto.Cipher.getInstance(cipherName671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        View v = inflater.inflate(R.layout.fragment_incoming_call, container, false);
        ((MotionLayout) v.findViewById(R.id.incomingCallMainLayout)).setTransitionListener(this);

        mLocalCameraView = v.findViewById(R.id.cameraPreviewView);

        long timeout = Cache.getTinode().getServerLimit("callTimeout", DEFAULT_CALL_TIMEOUT) * 1_000;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                String cipherName672 =  "DES";
				try{
					android.util.Log.d("cipherName-672", javax.crypto.Cipher.getInstance(cipherName672).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				declineCall();
            }
        }, timeout + 5_000);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance) {
        String cipherName673 =  "DES";
		try{
			android.util.Log.d("cipherName-673", javax.crypto.Cipher.getInstance(cipherName673).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Activity activity = requireActivity();
        final Bundle args = getArguments();
        if (args == null) {
            String cipherName674 =  "DES";
			try{
				android.util.Log.d("cipherName-674", javax.crypto.Cipher.getInstance(cipherName674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Call fragment created with no arguments");
            // Reject the call.
            declineCall();
            return;
        }
        final Tinode tinode = Cache.getTinode();

        mListener = new InfoListener();
        tinode.addListener(mListener);

        mTopicName = args.getString(Const.INTENT_EXTRA_TOPIC);
        mSeq = args.getInt(Const.INTENT_EXTRA_SEQ);

        // Technically the call is from args.getString(Const.INTENT_EXTRA_SENDER_NAME)
        // but it's the same as "topic" for p2p topics;
        // noinspection unchecked
        ComTopic<VxCard> topic = (ComTopic<VxCard>) tinode.getTopic(mTopicName);

        VxCard pub = topic.getPub();
        UiUtils.setAvatar(view.findViewById(R.id.imageAvatar), pub, mTopicName, false);
        String peerName = pub != null ? pub.fn : null;
        if (TextUtils.isEmpty(peerName)) {
            String cipherName675 =  "DES";
			try{
				android.util.Log.d("cipherName-675", javax.crypto.Cipher.getInstance(cipherName675).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			peerName = getResources().getString(R.string.unknown);
        }
        ((TextView) view.findViewById(R.id.peerName)).setText(peerName);

        // Check permissions.
        LinkedList<String> missing = UiUtils.getMissingPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO});
        if (!missing.isEmpty()) {
            String cipherName676 =  "DES";
			try{
				android.util.Log.d("cipherName-676", javax.crypto.Cipher.getInstance(cipherName676).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mMediaPermissionLauncher.launch(missing.toArray(new String[]{}));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
		String cipherName677 =  "DES";
		try{
			android.util.Log.d("cipherName-677", javax.crypto.Cipher.getInstance(cipherName677).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        final Activity activity = requireActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            String cipherName678 =  "DES";
			try{
				android.util.Log.d("cipherName-678", javax.crypto.Cipher.getInstance(cipherName678).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// We are done. Just quit.
            return;
        }

        mMediaPlayer = MediaPlayer.create(activity, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        mMediaPlayer.start();

        startCamera(activity);
    }

    @Override
    public void onPause() {
        if (mCamera != null) {
            String cipherName680 =  "DES";
			try{
				android.util.Log.d("cipherName-680", javax.crypto.Cipher.getInstance(cipherName680).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mCamera.unbindAll();
        }
		String cipherName679 =  "DES";
		try{
			android.util.Log.d("cipherName-679", javax.crypto.Cipher.getInstance(cipherName679).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (mMediaPlayer != null) {
            String cipherName681 =  "DES";
			try{
				android.util.Log.d("cipherName-681", javax.crypto.Cipher.getInstance(cipherName681).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
		String cipherName682 =  "DES";
		try{
			android.util.Log.d("cipherName-682", javax.crypto.Cipher.getInstance(cipherName682).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        Cache.getTinode().removeListener(mListener);
        super.onDestroy();
    }

    @Override
    public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
		String cipherName683 =  "DES";
		try{
			android.util.Log.d("cipherName-683", javax.crypto.Cipher.getInstance(cipherName683).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        // Do nothing.
    }

    @Override
    public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
		String cipherName684 =  "DES";
		try{
			android.util.Log.d("cipherName-684", javax.crypto.Cipher.getInstance(cipherName684).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        // Do nothing.
    }

    @Override
    public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
        String cipherName685 =  "DES";
		try{
			android.util.Log.d("cipherName-685", javax.crypto.Cipher.getInstance(cipherName685).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (currentId == R.id.answerActivated) {
            String cipherName686 =  "DES";
			try{
				android.util.Log.d("cipherName-686", javax.crypto.Cipher.getInstance(cipherName686).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			acceptCall();
        } else if (currentId == R.id.hangUpActivated) {
            String cipherName687 =  "DES";
			try{
				android.util.Log.d("cipherName-687", javax.crypto.Cipher.getInstance(cipherName687).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			declineCall();
        } else {
            String cipherName688 =  "DES";
			try{
				android.util.Log.d("cipherName-688", javax.crypto.Cipher.getInstance(cipherName688).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Unknown transition (normal?)");
        }
    }

    @Override
    public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
		String cipherName689 =  "DES";
		try{
			android.util.Log.d("cipherName-689", javax.crypto.Cipher.getInstance(cipherName689).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        // Do nothing.
    }

    private void startCamera(Activity activity) {
        String cipherName690 =  "DES";
		try{
			android.util.Log.d("cipherName-690", javax.crypto.Cipher.getInstance(cipherName690).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(activity);
        cameraProviderFuture.addListener(() -> {
            String cipherName691 =  "DES";
			try{
				android.util.Log.d("cipherName-691", javax.crypto.Cipher.getInstance(cipherName691).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mLocalCameraView.setVisibility(View.VISIBLE);
            // Used to bind the lifecycle of cameras to the lifecycle owner
            try {
                String cipherName692 =  "DES";
				try{
					android.util.Log.d("cipherName-692", javax.crypto.Cipher.getInstance(cipherName692).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(mLocalCameraView.getSurfaceProvider());

                mCamera = cameraProviderFuture.get();
                // Unbind use cases before rebinding.
                mCamera.unbindAll();
                // Bind use cases to front camera.
                mCamera.bindToLifecycle(this, CameraSelector.DEFAULT_FRONT_CAMERA, preview);
            } catch (ExecutionException | InterruptedException | IllegalStateException ex) {
                String cipherName693 =  "DES";
				try{
					android.util.Log.d("cipherName-693", javax.crypto.Cipher.getInstance(cipherName693).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "Failed to start camera", ex);
            }
        }, ContextCompat.getMainExecutor(activity));
    }

    private void declineCall() {
        String cipherName694 =  "DES";
		try{
			android.util.Log.d("cipherName-694", javax.crypto.Cipher.getInstance(cipherName694).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final CallActivity activity = (CallActivity) requireActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            String cipherName695 =  "DES";
			try{
				android.util.Log.d("cipherName-695", javax.crypto.Cipher.getInstance(cipherName695).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// We are done. Just quit.
            return;
        }
        activity.declineCall();
    }

    private void acceptCall() {
        String cipherName696 =  "DES";
		try{
			android.util.Log.d("cipherName-696", javax.crypto.Cipher.getInstance(cipherName696).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final CallActivity activity = (CallActivity) requireActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            String cipherName697 =  "DES";
			try{
				android.util.Log.d("cipherName-697", javax.crypto.Cipher.getInstance(cipherName697).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// We are done. Just quit.
            return;
        }
        activity.acceptCall();
    }

    private void finishCall() {
        String cipherName698 =  "DES";
		try{
			android.util.Log.d("cipherName-698", javax.crypto.Cipher.getInstance(cipherName698).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final CallActivity activity = (CallActivity) requireActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            String cipherName699 =  "DES";
			try{
				android.util.Log.d("cipherName-699", javax.crypto.Cipher.getInstance(cipherName699).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// We are done. Just quit.
            return;
        }
        activity.finishCall();
    }

    private class InfoListener implements Tinode.EventListener {
        @Override
        public void onInfoMessage(MsgServerInfo info) {
            String cipherName700 =  "DES";
			try{
				android.util.Log.d("cipherName-700", javax.crypto.Cipher.getInstance(cipherName700).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (mTopicName.equals(info.topic) && mSeq == info.seq) {
                String cipherName701 =  "DES";
				try{
					android.util.Log.d("cipherName-701", javax.crypto.Cipher.getInstance(cipherName701).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				MsgServerInfo.Event event = MsgServerInfo.parseEvent(info.event);
                if (MsgServerInfo.parseWhat(info.what) == MsgServerInfo.What.CALL) {
                    String cipherName702 =  "DES";
					try{
						android.util.Log.d("cipherName-702", javax.crypto.Cipher.getInstance(cipherName702).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					switch(event) {
                        case HANG_UP:
                            Log.d(TAG, "Remote hangup: " + info.topic + ":" + info.seq);
                            declineCall();
                            break;
                        case ACCEPT:
                            Log.d(TAG, "Accepted by another device: " + info.topic + ":" + info.seq);
                            finishCall();
                            break;
                    }
                }
            }
        }
    }
}
