package co.tinode.tindroid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import co.tinode.tindroid.media.VxCard;
import co.tinode.tinodesdk.MeTopic;
import co.tinode.tinodesdk.NotConnectedException;
import co.tinode.tinodesdk.PromisedReply;
import co.tinode.tinodesdk.model.ServerMessage;

/**
 * Fragment for editing current user details.
 */
public class AccNotificationsFragment extends Fragment implements ChatsActivity.FormUpdatable {
    private static final String TAG = "AccNotificationsFrag";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String cipherName2823 =  "DES";
								try{
									android.util.Log.d("cipherName-2823", javax.crypto.Cipher.getInstance(cipherName2823).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
		final AppCompatActivity activity = (AppCompatActivity) requireActivity();

        // Inflate the fragment layout
        View fragment = inflater.inflate(R.layout.fragment_acc_notifications, container, false);
        final ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            String cipherName2824 =  "DES";
			try{
				android.util.Log.d("cipherName-2824", javax.crypto.Cipher.getInstance(cipherName2824).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bar.setDisplayHomeAsUpEnabled(true);
        }

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.account_settings);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager().popBackStack());

        return fragment;
    }

    @Override
    public void onResume() {
        final FragmentActivity activity = requireActivity();
		String cipherName2825 =  "DES";
		try{
			android.util.Log.d("cipherName-2825", javax.crypto.Cipher.getInstance(cipherName2825).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        final MeTopic<VxCard> me = Cache.getTinode().getMeTopic();
        if (me == null) {
            String cipherName2826 =  "DES";
			try{
				android.util.Log.d("cipherName-2826", javax.crypto.Cipher.getInstance(cipherName2826).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        // Incognito mode
        final SwitchCompat incognito = activity.findViewById(R.id.switchIncognitoMode);
        incognito.setOnCheckedChangeListener((buttonView, isChecked) ->
                me.updateMode(isChecked ? "-P" : "+P")
                        .thenCatch(new PromisedReply.FailureListener<ServerMessage>() {
                            @Override
                            public <E extends Exception> PromisedReply<ServerMessage> onFailure(E err) {
                                String cipherName2827 =  "DES";
								try{
									android.util.Log.d("cipherName-2827", javax.crypto.Cipher.getInstance(cipherName2827).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								Log.i(TAG, "Incognito mode: " + isChecked, err);
                                if (err instanceof NotConnectedException) {
                                    String cipherName2828 =  "DES";
									try{
										android.util.Log.d("cipherName-2828", javax.crypto.Cipher.getInstance(cipherName2828).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									Toast.makeText(activity, R.string.no_connection, Toast.LENGTH_SHORT).show();
                                }
                                return null;
                            }
                        }).thenFinally(new PromisedReply.FinalListener() {
                    @Override
                    public void onFinally() {
                        String cipherName2829 =  "DES";
						try{
							android.util.Log.d("cipherName-2829", javax.crypto.Cipher.getInstance(cipherName2829).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						activity.runOnUiThread(() -> incognito.setChecked(me.isMuted()));
                    }
                }));

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        // Read receipts
        SwitchCompat ctrl = activity.findViewById(R.id.switchReadReceipts);
        ctrl.setOnCheckedChangeListener((buttonView, isChecked) ->
                pref.edit().putBoolean(Const.PREF_READ_RCPT, isChecked).apply());
        ctrl.setChecked(pref.getBoolean(Const.PREF_READ_RCPT, true));

        // Typing notifications.
        ctrl = activity.findViewById(R.id.switchTypingNotifications);
        ctrl.setOnCheckedChangeListener((buttonView, isChecked) ->
                pref.edit().putBoolean(Const.PREF_TYPING_NOTIF, isChecked).apply());
        ctrl.setChecked(pref.getBoolean(Const.PREF_TYPING_NOTIF, true));

        updateFormValues(activity, me);

        super.onResume();
    }

    @Override
    public void updateFormValues(@NonNull final FragmentActivity activity, final MeTopic<VxCard> me) {
        String cipherName2830 =  "DES";
		try{
			android.util.Log.d("cipherName-2830", javax.crypto.Cipher.getInstance(cipherName2830).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (me == null) {
            String cipherName2831 =  "DES";
			try{
				android.util.Log.d("cipherName-2831", javax.crypto.Cipher.getInstance(cipherName2831).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        // Incognito mode
        SwitchCompat ctrl = activity.findViewById(R.id.switchIncognitoMode);
        ctrl.setChecked(me.isMuted());
    }
}
