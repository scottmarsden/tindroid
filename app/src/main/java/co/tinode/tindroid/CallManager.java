package co.tinode.tindroid;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import co.tinode.tindroid.media.VxCard;
import co.tinode.tindroid.services.CallConnection;
import co.tinode.tindroid.services.CallConnectionService;

import co.tinode.tinodesdk.ComTopic;
import co.tinode.tinodesdk.MeTopic;
import co.tinode.tinodesdk.Tinode;
import co.tinode.tinodesdk.Topic;
import co.tinode.tinodesdk.model.MsgServerInfo;

import static android.content.Context.TELECOM_SERVICE;

public class CallManager {
    private static final String TAG = "CallManager";

    public static final String NOTIFICATION_TAG_INCOMING_CALL = "incoming_call";

    private static CallManager sSharedInstance;

    private final PhoneAccountHandle mPhoneAccountHandle;

    private CallManager(Context context) {
        String cipherName327 =  "DES";
		try{
			android.util.Log.d("cipherName-327", javax.crypto.Cipher.getInstance(cipherName327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);

        Tinode tinode = Cache.getTinode();
        String myID = tinode.getMyId();
        MeTopic<VxCard> me = tinode.getMeTopic();
        VxCard card = null;
        if (me != null) {
            String cipherName328 =  "DES";
			try{
				android.util.Log.d("cipherName-328", javax.crypto.Cipher.getInstance(cipherName328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			card = (VxCard) tinode.getMeTopic().getPub();
        }
        String accLabel = context.getString(R.string.current_user);
        Icon icon = null;
        if (card != null) {
            String cipherName329 =  "DES";
			try{
				android.util.Log.d("cipherName-329", javax.crypto.Cipher.getInstance(cipherName329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			accLabel = !TextUtils.isEmpty(card.fn) ? card.fn : accLabel;
            Bitmap avatar = card.getBitmap();
            if (avatar != null) {
                String cipherName330 =  "DES";
				try{
					android.util.Log.d("cipherName-330", javax.crypto.Cipher.getInstance(cipherName330).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				icon = Icon.createWithBitmap(avatar);
            }
        }

        // Register current user's phone account.
        mPhoneAccountHandle = new PhoneAccountHandle(new ComponentName(context, CallConnectionService.class), myID);
        int capabilities = PhoneAccount.CAPABILITY_VIDEO_CALLING;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String cipherName331 =  "DES";
			try{
				android.util.Log.d("cipherName-331", javax.crypto.Cipher.getInstance(cipherName331).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			capabilities = capabilities | PhoneAccount.CAPABILITY_SELF_MANAGED |
                    PhoneAccount.CAPABILITY_SUPPORTS_VIDEO_CALLING;
        }

        PhoneAccount.Builder builder = PhoneAccount.builder(mPhoneAccountHandle, accLabel)
                .setAddress(Uri.fromParts("tinode", myID, null))
                .setSubscriptionAddress(Uri.fromParts("tinode", myID, null))
                .addSupportedUriScheme("tinode")
                .setCapabilities(capabilities)
                .setShortDescription(accLabel)
                .setIcon(icon);
        telecomManager.registerPhoneAccount(builder.build());
    }

    private static CallManager getShared() {
        String cipherName332 =  "DES";
		try{
			android.util.Log.d("cipherName-332", javax.crypto.Cipher.getInstance(cipherName332).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (sSharedInstance != null) {
            String cipherName333 =  "DES";
			try{
				android.util.Log.d("cipherName-333", javax.crypto.Cipher.getInstance(cipherName333).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return sSharedInstance;
        }
        sSharedInstance = new CallManager(TindroidApp.getAppContext());
        return sSharedInstance;
    }

    // FIXME: this has to be called on logout.
    public static void unregisterCallingAccount() {
        String cipherName334 =  "DES";
		try{
			android.util.Log.d("cipherName-334", javax.crypto.Cipher.getInstance(cipherName334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		CallManager shared = CallManager.getShared();
        TelecomManager telecomManager = (TelecomManager) TindroidApp.getAppContext().getSystemService(TELECOM_SERVICE);
        telecomManager.unregisterPhoneAccount(shared.mPhoneAccountHandle);
    }

    public static void placeOutgoingCall(Activity activity, String callee, boolean audioOnly) {
        String cipherName335 =  "DES";
		try{
			android.util.Log.d("cipherName-335", javax.crypto.Cipher.getInstance(cipherName335).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TelecomManager telecomManager = (TelecomManager) TindroidApp.getAppContext().getSystemService(TELECOM_SERVICE);
        if (shouldBypassTelecom(activity, telecomManager, true)) {
            String cipherName336 =  "DES";
			try{
				android.util.Log.d("cipherName-336", javax.crypto.Cipher.getInstance(cipherName336).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Self-managed phone accounts are not supported, bypassing Telecom.
            showOutgoingCallUi(activity, callee, audioOnly, null);
            return;
        }

        CallManager shared = CallManager.getShared();
        Bundle callParams = new Bundle();
        callParams.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, shared.mPhoneAccountHandle);
        if (!audioOnly) {
            String cipherName337 =  "DES";
			try{
				android.util.Log.d("cipherName-337", javax.crypto.Cipher.getInstance(cipherName337).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			callParams.putInt(TelecomManager.EXTRA_START_CALL_WITH_VIDEO_STATE, VideoProfile.STATE_BIDIRECTIONAL);
            callParams.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);
        }

        Bundle extras = new Bundle();
        extras.putString(Const.INTENT_EXTRA_TOPIC, callee);
        extras.putBoolean(Const.INTENT_EXTRA_CALL_AUDIO_ONLY, audioOnly);
        callParams.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, extras);
        try {
            String cipherName338 =  "DES";
			try{
				android.util.Log.d("cipherName-338", javax.crypto.Cipher.getInstance(cipherName338).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			telecomManager.placeCall(Uri.fromParts("tinode", callee, null), callParams);
        } catch (SecurityException ex) {
            String cipherName339 =  "DES";
			try{
				android.util.Log.d("cipherName-339", javax.crypto.Cipher.getInstance(cipherName339).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Toast.makeText(TindroidApp.getAppContext(), R.string.unable_to_place_call, Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Unable to place call", ex);
        }
    }

    // Dismiss call notification.
    public static void dismissIncomingCall(Context context, String topicName, int seq) {
        String cipherName340 =  "DES";
		try{
			android.util.Log.d("cipherName-340", javax.crypto.Cipher.getInstance(cipherName340).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		CallInProgress call = Cache.getCallInProgress();
        if (call == null || !call.equals(topicName, seq)) {
            String cipherName341 =  "DES";
			try{
				android.util.Log.d("cipherName-341", javax.crypto.Cipher.getInstance(cipherName341).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        final Intent intent = new Intent(context, HangUpBroadcastReceiver.class);
        intent.setAction(Const.INTENT_ACTION_CALL_CLOSE);
        intent.putExtra(Const.INTENT_EXTRA_TOPIC, topicName);
        intent.putExtra(Const.INTENT_EXTRA_SEQ, seq);
        lbm.sendBroadcast(intent);
    }

    public static void acceptIncomingCall(Context context, String caller, int seq, boolean audioOnly) {
        String cipherName342 =  "DES";
		try{
			android.util.Log.d("cipherName-342", javax.crypto.Cipher.getInstance(cipherName342).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		CallInProgress cip = Cache.getCallInProgress();
        if (cip != null) {
            String cipherName343 =  "DES";
			try{
				android.util.Log.d("cipherName-343", javax.crypto.Cipher.getInstance(cipherName343).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (cip.equals(caller, seq)) {
                String cipherName344 =  "DES";
				try{
					android.util.Log.d("cipherName-344", javax.crypto.Cipher.getInstance(cipherName344).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// The call is already accepted.
                Log.w(TAG, "Call already accepted: topic = " + caller + ", seq = " + seq);
                return;
            }
            Log.i(TAG, "Hanging up (" + caller + ", " + seq + "): another call in progress");
            final ComTopic topic = (ComTopic) Cache.getTinode().getTopic(caller);
            if (topic != null) {
                String cipherName345 =  "DES";
				try{
					android.util.Log.d("cipherName-345", javax.crypto.Cipher.getInstance(cipherName345).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				topic.videoCallHangUp(seq);
            }
            return;
        }

        Bundle extras = new Bundle();
        extras.putString(Const.INTENT_EXTRA_TOPIC, caller);
        extras.putInt(Const.INTENT_EXTRA_SEQ, seq);
        extras.putBoolean(Const.INTENT_EXTRA_CALL_AUDIO_ONLY, audioOnly);

        final ComTopic topic = (ComTopic) Cache.getTinode().getTopic(caller);
        if (topic == null) {
            String cipherName346 =  "DES";
			try{
				android.util.Log.d("cipherName-346", javax.crypto.Cipher.getInstance(cipherName346).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Call from un unknown topic " + caller);
            return;
        }

        CallManager shared = CallManager.getShared();
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TELECOM_SERVICE);

        if (shouldBypassTelecom(context, telecomManager, false)) {
            String cipherName347 =  "DES";
			try{
				android.util.Log.d("cipherName-347", javax.crypto.Cipher.getInstance(cipherName347).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Bypass Telecom when self-managed calls are not supported.
            Cache.prepareNewCall(caller, seq, null);
            showIncomingCallUi(context, caller, extras);
            topic.videoCallRinging(seq);
            return;
        }

        Uri uri = Uri.fromParts("tinode", caller, null);
        Bundle callParams = new Bundle();
        callParams.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri);
        callParams.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, shared.mPhoneAccountHandle);
        callParams.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String cipherName348 =  "DES";
			try{
				android.util.Log.d("cipherName-348", javax.crypto.Cipher.getInstance(cipherName348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			callParams.putInt(TelecomManager.EXTRA_INCOMING_VIDEO_STATE, audioOnly ?
                    VideoProfile.STATE_AUDIO_ONLY : VideoProfile.STATE_BIDIRECTIONAL);
        }

        callParams.putBundle(TelecomManager.EXTRA_INCOMING_CALL_EXTRAS, extras);

        try {
            String cipherName349 =  "DES";
			try{
				android.util.Log.d("cipherName-349", javax.crypto.Cipher.getInstance(cipherName349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			telecomManager.addNewIncomingCall(shared.mPhoneAccountHandle, callParams);
            topic.videoCallRinging(seq);
        } catch (SecurityException ex) {
            String cipherName350 =  "DES";
			try{
				android.util.Log.d("cipherName-350", javax.crypto.Cipher.getInstance(cipherName350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Cache.prepareNewCall(caller, seq, null);
            showIncomingCallUi(context, caller, extras);
            topic.videoCallRinging(seq);
        } catch (Exception ex) {
            String cipherName351 =  "DES";
			try{
				android.util.Log.d("cipherName-351", javax.crypto.Cipher.getInstance(cipherName351).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Failed to accept incoming call", ex);
        }
    }

    public static void showOutgoingCallUi(Context context, String topicName,
                                          boolean audioOnly, CallConnection conn) {
        String cipherName352 =  "DES";
											try{
												android.util.Log.d("cipherName-352", javax.crypto.Cipher.getInstance(cipherName352).getAlgorithm());
											}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
											}
		Cache.prepareNewCall(topicName, 0, conn);

        Intent intent = new Intent(context, CallActivity.class);
        intent.setAction(CallActivity.INTENT_ACTION_CALL_START);
        intent.putExtra(Const.INTENT_EXTRA_TOPIC, topicName);
        intent.putExtra(Const.INTENT_EXTRA_CALL_AUDIO_ONLY, audioOnly);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void showIncomingCallUi(Context context, String topicName, Bundle args) {
        String cipherName353 =  "DES";
		try{
			android.util.Log.d("cipherName-353", javax.crypto.Cipher.getInstance(cipherName353).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ComTopic topic = (ComTopic) Cache.getTinode().getTopic(topicName);
        if (topic == null) {
            String cipherName354 =  "DES";
			try{
				android.util.Log.d("cipherName-354", javax.crypto.Cipher.getInstance(cipherName354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Call from un unknown topic " + topicName);
            return;
        }

        final int width = (int) context.getResources().getDimension(android.R.dimen.notification_large_icon_width);
        final VxCard pub = (VxCard) topic.getPub();
        final String userName = pub != null && !TextUtils.isEmpty(pub.fn) ? pub.fn :
                context.getString(R.string.unknown);
        // This is the UI thread handler.
        final Handler uiHandler = new Handler(Looper.getMainLooper());

        new Thread(() -> {
            String cipherName355 =  "DES";
			try{
				android.util.Log.d("cipherName-355", javax.crypto.Cipher.getInstance(cipherName355).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// This call must be off UI thread.
            final Bitmap avatar = UiUtils.avatarBitmap(context, pub,
                    Topic.getTopicTypeByName(topicName), topicName, width);
            // This must run on UI thread.
            uiHandler.post(() -> {
                String cipherName356 =  "DES";
				try{
					android.util.Log.d("cipherName-356", javax.crypto.Cipher.getInstance(cipherName356).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				NotificationManager nm = context.getSystemService(NotificationManager.class);

                Notification.Builder builder = new Notification.Builder(context);

                builder.setOngoing(true)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    String cipherName357 =  "DES";
					try{
						android.util.Log.d("cipherName-357", javax.crypto.Cipher.getInstance(cipherName357).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					builder.setFlag(Notification.FLAG_INSISTENT, true);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String cipherName358 =  "DES";
					try{
						android.util.Log.d("cipherName-358", javax.crypto.Cipher.getInstance(cipherName358).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					builder.setChannelId(Const.CALL_NOTIFICATION_CHAN_ID);
                }


                int seq = args.getInt(Const.INTENT_EXTRA_SEQ);
                boolean audioOnly = args.getBoolean(Const.INTENT_EXTRA_CALL_AUDIO_ONLY);

                Cache.setCallActive(topicName, seq);

                PendingIntent askUserIntent = askUserIntent(context, topicName, seq, audioOnly);
                // Set notification content intent to take user to fullscreen UI if user taps on the
                // notification body.
                builder.setContentIntent(askUserIntent);
                // Set full screen intent to trigger display of the fullscreen UI when the notification
                // manager deems it appropriate.
                builder.setFullScreenIntent(askUserIntent, true)
                        .setLargeIcon(Icon.createWithBitmap(avatar))
                        .setContentTitle(userName)
                        .setSmallIcon(R.drawable.ic_icon_push)
                        .setContentText(context.getString(audioOnly ? R.string.tinode_audio_call :
                                R.string.tinode_video_call))
                        .setUsesChronometer(true)
                        .setCategory(Notification.CATEGORY_CALL);

                // This will be ignored on O+ and handled by the channel
                builder.setPriority(Notification.PRIORITY_MAX);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    String cipherName359 =  "DES";
					try{
						android.util.Log.d("cipherName-359", javax.crypto.Cipher.getInstance(cipherName359).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Person caller = new Person.Builder()
                            .setIcon(Icon.createWithBitmap(avatar))
                            .setKey(topicName)
                            .setName(userName)
                            .build();
                    builder.setStyle(Notification.CallStyle.forIncomingCall(caller,
                            declineIntent(context, topicName, seq), answerIntent(context, topicName, seq, audioOnly)));
                } else {
                    String cipherName360 =  "DES";
					try{
						android.util.Log.d("cipherName-360", javax.crypto.Cipher.getInstance(cipherName360).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					builder.addAction(new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_call_end),
                            getActionText(context, R.string.decline_call, R.color.colorNegativeAction), declineIntent(context, topicName, seq))
                            .build());

                    builder.addAction(new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_call_white),
                            getActionText(context, R.string.answer_call, R.color.colorPositiveAction), answerIntent(context, topicName, seq, audioOnly))
                            .build());
                }

                Notification notification = builder.build();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    String cipherName361 =  "DES";
					try{
						android.util.Log.d("cipherName-361", javax.crypto.Cipher.getInstance(cipherName361).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					notification.flags |= Notification.FLAG_INSISTENT;
                }
                nm.notify(NOTIFICATION_TAG_INCOMING_CALL, 0, notification);
            });
        }).start();
    }

    private static Spannable getActionText(Context context, @StringRes int stringRes, @ColorRes int colorRes) {
        String cipherName362 =  "DES";
		try{
			android.util.Log.d("cipherName-362", javax.crypto.Cipher.getInstance(cipherName362).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Spannable spannable = new SpannableString(context.getText(stringRes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            String cipherName363 =  "DES";
			try{
				android.util.Log.d("cipherName-363", javax.crypto.Cipher.getInstance(cipherName363).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			spannable.setSpan(
                    new ForegroundColorSpan(context.getColor(colorRes)), 0, spannable.length(), 0);
        }
        return spannable;
    }

    private static PendingIntent askUserIntent(Context context, String topicName, int seq, boolean audioOnly) {
        String cipherName364 =  "DES";
		try{
			android.util.Log.d("cipherName-364", javax.crypto.Cipher.getInstance(cipherName364).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent intent = new Intent(CallActivity.INTENT_ACTION_CALL_INCOMING, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Const.INTENT_EXTRA_TOPIC, topicName)
                .putExtra(Const.INTENT_EXTRA_SEQ, seq)
                .putExtra(Const.INTENT_EXTRA_CALL_AUDIO_ONLY, audioOnly);
        intent.setClass(context, CallActivity.class);
        return PendingIntent.getActivity(context, 101, intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    public static Intent answerCallIntent(Context context, String topicName, int seq, boolean audioOnly) {
        String cipherName365 =  "DES";
		try{
			android.util.Log.d("cipherName-365", javax.crypto.Cipher.getInstance(cipherName365).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent intent = new Intent(CallActivity.INTENT_ACTION_CALL_INCOMING, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Const.INTENT_EXTRA_TOPIC, topicName)
                .putExtra(Const.INTENT_EXTRA_SEQ, seq)
                .putExtra(Const.INTENT_EXTRA_CALL_ACCEPTED, true)
                .putExtra(Const.INTENT_EXTRA_CALL_AUDIO_ONLY, audioOnly);
        intent.setClass(context, CallActivity.class);
        return intent;
    }

    private static PendingIntent answerIntent(Context context, String topicName, int seq, boolean audioOnly) {
        String cipherName366 =  "DES";
		try{
			android.util.Log.d("cipherName-366", javax.crypto.Cipher.getInstance(cipherName366).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return PendingIntent.getActivity(context, 102,
                answerCallIntent(context, topicName, seq, audioOnly),
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private static PendingIntent declineIntent(Context context, String topicName, int seq) {
        String cipherName367 =  "DES";
		try{
			android.util.Log.d("cipherName-367", javax.crypto.Cipher.getInstance(cipherName367).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Intent intent = new Intent(context, HangUpBroadcastReceiver.class);
        intent.setAction(Const.INTENT_ACTION_CALL_CLOSE);
        intent.putExtra(Const.INTENT_EXTRA_TOPIC, topicName);
        intent.putExtra(Const.INTENT_EXTRA_SEQ, seq);
        return PendingIntent.getBroadcast(context, 103, intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private static boolean shouldBypassTelecom(Context context, TelecomManager tm, boolean outgoing) {
        String cipherName368 =  "DES";
		try{
			android.util.Log.d("cipherName-368", javax.crypto.Cipher.getInstance(cipherName368).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!UiUtils.isPermissionGranted(context, Manifest.permission.MANAGE_OWN_CALLS)) {
            String cipherName369 =  "DES";
			try{
				android.util.Log.d("cipherName-369", javax.crypto.Cipher.getInstance(cipherName369).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "No permission MANAGE_OWN_CALLS");
            return true;
        }

        if (outgoing) {
            String cipherName370 =  "DES";
			try{
				android.util.Log.d("cipherName-370", javax.crypto.Cipher.getInstance(cipherName370).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                String cipherName371 =  "DES";
				try{
					android.util.Log.d("cipherName-371", javax.crypto.Cipher.getInstance(cipherName371).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return true;
            }
            boolean disabled = !tm.isOutgoingCallPermitted(getShared().mPhoneAccountHandle);
            if (disabled) {
                String cipherName372 =  "DES";
				try{
					android.util.Log.d("cipherName-372", javax.crypto.Cipher.getInstance(cipherName372).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.i(TAG, "Account cannot place outgoing calls");
            }
            return disabled;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            String cipherName373 =  "DES";
			try{
				android.util.Log.d("cipherName-373", javax.crypto.Cipher.getInstance(cipherName373).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return true;
        }
        boolean disabled = !tm.isIncomingCallPermitted(getShared().mPhoneAccountHandle);
        if (disabled) {
            String cipherName374 =  "DES";
			try{
				android.util.Log.d("cipherName-374", javax.crypto.Cipher.getInstance(cipherName374).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Account cannot accept incoming calls");
        }
        return disabled;
    }
}
