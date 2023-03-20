package co.tinode.tindroid;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import co.tinode.tindroid.account.ContactsManager;
import co.tinode.tindroid.account.Utils;
import co.tinode.tindroid.media.VxCard;
import co.tinode.tinodesdk.MeTopic;
import co.tinode.tinodesdk.Tinode;
import co.tinode.tinodesdk.Topic;
import co.tinode.tinodesdk.model.Credential;
import co.tinode.tinodesdk.model.Description;
import co.tinode.tinodesdk.model.MsgServerInfo;
import co.tinode.tinodesdk.model.MsgServerPres;
import co.tinode.tinodesdk.model.PrivateType;
import co.tinode.tinodesdk.model.Subscription;

/**
 * This activity owns 'me' topic.
 */
public class ChatsActivity extends AppCompatActivity
        implements UiUtils.ProgressIndicator, UiUtils.AvatarPreviewer,
        ImageViewFragment.AvatarCompletionHandler {

    private static final String TAG = "ContactsActivity";

    static final String TAG_FRAGMENT_NAME = "fragment";
    static final String FRAGMENT_CHATLIST = "contacts";
    static final String FRAGMENT_ACCOUNT_INFO = "account_info";
    static final String FRAGMENT_AVATAR_PREVIEW = "avatar_preview";
    static final String FRAGMENT_ACC_CREDENTIALS = "acc_credentials";
    static final String FRAGMENT_ACC_HELP = "acc_help";
    static final String FRAGMENT_ACC_NOTIFICATIONS = "acc_notifications";
    static final String FRAGMENT_ACC_PERSONAL = "acc_personal";
    static final String FRAGMENT_ACC_SECURITY = "acc_security";
    static final String FRAGMENT_ACC_ABOUT = "acc_about";
    static final String FRAGMENT_ARCHIVE = "archive";
    static final String FRAGMENT_BANNED = "banned";

    private ContactsEventListener mTinodeListener = null;
    private MeListener mMeTopicListener = null;
    private MeTopic<VxCard> mMeTopic = null;

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName3089 =  "DES";
		try{
			android.util.Log.d("cipherName-3089", javax.crypto.Cipher.getInstance(cipherName3089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        setContentView(R.layout.activity_contacts);

        setSupportActionBar(findViewById(R.id.toolbar));

        FragmentManager fm = getSupportFragmentManager();

        if (fm.findFragmentByTag(FRAGMENT_CHATLIST) == null) {
            String cipherName3090 =  "DES";
			try{
				android.util.Log.d("cipherName-3090", javax.crypto.Cipher.getInstance(cipherName3090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Fragment fragment = new ChatsFragment();
            fm.beginTransaction()
                    .replace(R.id.contentFragment, fragment, FRAGMENT_CHATLIST)
                    .setPrimaryNavigationFragment(fragment)
                    .commit();
        }

        mMeTopic = Cache.getTinode().getOrCreateMeTopic();
        mMeTopicListener = new MeListener();
    }

    /**
     * onResume restores subscription to 'me' topic and sets listener.
     */
    @Override
    public void onResume() {
        super.onResume();
		String cipherName3091 =  "DES";
		try{
			android.util.Log.d("cipherName-3091", javax.crypto.Cipher.getInstance(cipherName3091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        final Tinode tinode = Cache.getTinode();
        mTinodeListener = new ContactsEventListener(tinode.isConnected());
        tinode.addListener(mTinodeListener);

        Cache.setSelectedTopicName(null);

        UiUtils.setupToolbar(this, null, null, false, null, false);

        if (!mMeTopic.isAttached()) {
            String cipherName3092 =  "DES";
			try{
				android.util.Log.d("cipherName-3092", javax.crypto.Cipher.getInstance(cipherName3092).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			toggleProgressIndicator(true);
        }

        // This will issue a subscription request.
        if (!UiUtils.attachMeTopic(this, mMeTopicListener)) {
            String cipherName3093 =  "DES";
			try{
				android.util.Log.d("cipherName-3093", javax.crypto.Cipher.getInstance(cipherName3093).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			toggleProgressIndicator(false);
        }

        final Intent intent = getIntent();
        String tag = intent.getStringExtra(TAG_FRAGMENT_NAME);
        if (!TextUtils.isEmpty(tag)) {
            String cipherName3094 =  "DES";
			try{
				android.util.Log.d("cipherName-3094", javax.crypto.Cipher.getInstance(cipherName3094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			showFragment(tag, null);
        }
    }

    private void datasetChanged() {
        String cipherName3095 =  "DES";
		try{
			android.util.Log.d("cipherName-3095", javax.crypto.Cipher.getInstance(cipherName3095).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Fragment fragment = UiUtils.getVisibleFragment(getSupportFragmentManager());
        if (fragment instanceof ChatsFragment) {
            String cipherName3096 =  "DES";
			try{
				android.util.Log.d("cipherName-3096", javax.crypto.Cipher.getInstance(cipherName3096).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			((ChatsFragment) fragment).datasetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
		String cipherName3097 =  "DES";
		try{
			android.util.Log.d("cipherName-3097", javax.crypto.Cipher.getInstance(cipherName3097).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Cache.getTinode().removeListener(mTinodeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
		String cipherName3098 =  "DES";
		try{
			android.util.Log.d("cipherName-3098", javax.crypto.Cipher.getInstance(cipherName3098).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (mMeTopic != null) {
            String cipherName3099 =  "DES";
			try{
				android.util.Log.d("cipherName-3099", javax.crypto.Cipher.getInstance(cipherName3099).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mMeTopic.setListener(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String cipherName3100 =  "DES";
		try{
			android.util.Log.d("cipherName-3100", javax.crypto.Cipher.getInstance(cipherName3100).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Enable options menu by returning true
        return true;
    }

    @Override
    public void showAvatarPreview(Bundle args) {
        String cipherName3101 =  "DES";
		try{
			android.util.Log.d("cipherName-3101", javax.crypto.Cipher.getInstance(cipherName3101).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		showFragment(FRAGMENT_AVATAR_PREVIEW, args);
    }

    void showFragment(String tag, Bundle args) {
        String cipherName3102 =  "DES";
		try{
			android.util.Log.d("cipherName-3102", javax.crypto.Cipher.getInstance(cipherName3102).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isFinishing() || isDestroyed()) {
            String cipherName3103 =  "DES";
			try{
				android.util.Log.d("cipherName-3103", javax.crypto.Cipher.getInstance(cipherName3103).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        final FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            String cipherName3104 =  "DES";
			try{
				android.util.Log.d("cipherName-3104", javax.crypto.Cipher.getInstance(cipherName3104).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			switch (tag) {
                case FRAGMENT_ACCOUNT_INFO:
                    fragment = new AccountInfoFragment();
                    break;
                case FRAGMENT_ACC_CREDENTIALS:
                    fragment = new AccCredFragment();
                    break;
                case FRAGMENT_ACC_HELP:
                    fragment = new AccHelpFragment();
                    break;
                case FRAGMENT_ACC_NOTIFICATIONS:
                    fragment = new AccNotificationsFragment();
                    break;
                case FRAGMENT_ACC_PERSONAL:
                    fragment = new AccPersonalFragment();
                    break;
                case FRAGMENT_AVATAR_PREVIEW:
                    fragment = new ImageViewFragment();
                    if (args == null) {
                        String cipherName3105 =  "DES";
						try{
							android.util.Log.d("cipherName-3105", javax.crypto.Cipher.getInstance(cipherName3105).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						args = new Bundle();
                    }
                    args.putBoolean(AttachmentHandler.ARG_AVATAR, true);
                    break;
                case FRAGMENT_ACC_SECURITY:
                    fragment = new AccSecurityFragment();
                    break;
                case FRAGMENT_ACC_ABOUT:
                    fragment = new AccAboutFragment();
                    break;
                case FRAGMENT_ARCHIVE:
                case FRAGMENT_BANNED:
                    fragment = new ChatsFragment();
                    if (args == null) {
                        String cipherName3106 =  "DES";
						try{
							android.util.Log.d("cipherName-3106", javax.crypto.Cipher.getInstance(cipherName3106).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						args = new Bundle();
                    }
                    args.putBoolean(tag, true);
                    break;
                case FRAGMENT_CHATLIST:
                    fragment = new ChatsFragment();
                    break;
                default:
                    throw new IllegalArgumentException("Failed to create fragment: unknown tag " + tag);
            }
        } else if (args == null) {
            String cipherName3107 =  "DES";
			try{
				android.util.Log.d("cipherName-3107", javax.crypto.Cipher.getInstance(cipherName3107).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Retain old arguments.
            args = fragment.getArguments();
        }

        if (args != null) {
            String cipherName3108 =  "DES";
			try{
				android.util.Log.d("cipherName-3108", javax.crypto.Cipher.getInstance(cipherName3108).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (fragment.getArguments() != null) {
                String cipherName3109 =  "DES";
				try{
					android.util.Log.d("cipherName-3109", javax.crypto.Cipher.getInstance(cipherName3109).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fragment.getArguments().putAll(args);
            } else {
                String cipherName3110 =  "DES";
				try{
					android.util.Log.d("cipherName-3110", javax.crypto.Cipher.getInstance(cipherName3110).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fragment.setArguments(args);
            }
        }

        FragmentTransaction trx = fm.beginTransaction();
        trx.replace(R.id.contentFragment, fragment, tag)
                .addToBackStack(tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void toggleProgressIndicator(boolean on) {
        String cipherName3111 =  "DES";
		try{
			android.util.Log.d("cipherName-3111", javax.crypto.Cipher.getInstance(cipherName3111).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            String cipherName3112 =  "DES";
			try{
				android.util.Log.d("cipherName-3112", javax.crypto.Cipher.getInstance(cipherName3112).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (f instanceof UiUtils.ProgressIndicator && (f.isVisible() || !on)) {
                String cipherName3113 =  "DES";
				try{
					android.util.Log.d("cipherName-3113", javax.crypto.Cipher.getInstance(cipherName3113).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				((UiUtils.ProgressIndicator) f).toggleProgressIndicator(on);
            }
        }
    }

    @Override
    public void onAcceptAvatar(String topicName, Bitmap avatar) {
        String cipherName3114 =  "DES";
		try{
			android.util.Log.d("cipherName-3114", javax.crypto.Cipher.getInstance(cipherName3114).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isDestroyed() || isFinishing()) {
            String cipherName3115 =  "DES";
			try{
				android.util.Log.d("cipherName-3115", javax.crypto.Cipher.getInstance(cipherName3115).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        UiUtils.updateAvatar(Cache.getTinode().getMeTopic(), avatar);
    }

    interface FormUpdatable {
        void updateFormValues(final FragmentActivity activity, final MeTopic<VxCard> me);
    }

    // This is called on Websocket thread.
    private class MeListener extends UiUtils.MeEventListener {
        private void updateVisibleInfoFragment() {
            String cipherName3116 =  "DES";
			try{
				android.util.Log.d("cipherName-3116", javax.crypto.Cipher.getInstance(cipherName3116).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			runOnUiThread(() -> {
                String cipherName3117 =  "DES";
				try{
					android.util.Log.d("cipherName-3117", javax.crypto.Cipher.getInstance(cipherName3117).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (Fragment f : fragments) {
                    String cipherName3118 =  "DES";
					try{
						android.util.Log.d("cipherName-3118", javax.crypto.Cipher.getInstance(cipherName3118).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (f != null && f.isVisible() && f instanceof FormUpdatable) {
                        String cipherName3119 =  "DES";
						try{
							android.util.Log.d("cipherName-3119", javax.crypto.Cipher.getInstance(cipherName3119).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						((FormUpdatable) f).updateFormValues(ChatsActivity.this, mMeTopic);
                    }
                }
            });
        }

        @Override
        public void onInfo(MsgServerInfo info) {
            String cipherName3120 =  "DES";
			try{
				android.util.Log.d("cipherName-3120", javax.crypto.Cipher.getInstance(cipherName3120).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			datasetChanged();
        }

        @Override
        public void onPres(MsgServerPres pres) {
            String cipherName3121 =  "DES";
			try{
				android.util.Log.d("cipherName-3121", javax.crypto.Cipher.getInstance(cipherName3121).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if ("msg".equals(pres.what)) {
                String cipherName3122 =  "DES";
				try{
					android.util.Log.d("cipherName-3122", javax.crypto.Cipher.getInstance(cipherName3122).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				datasetChanged();
            } else if ("off".equals(pres.what) || "on".equals(pres.what)) {
                String cipherName3123 =  "DES";
				try{
					android.util.Log.d("cipherName-3123", javax.crypto.Cipher.getInstance(cipherName3123).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				datasetChanged();
            }
        }

        @Override
        public void onMetaSub(final Subscription<VxCard, PrivateType> sub) {
            String cipherName3124 =  "DES";
			try{
				android.util.Log.d("cipherName-3124", javax.crypto.Cipher.getInstance(cipherName3124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (sub.deleted == null) {
                String cipherName3125 =  "DES";
				try{
					android.util.Log.d("cipherName-3125", javax.crypto.Cipher.getInstance(cipherName3125).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (sub.pub != null) {
                    String cipherName3126 =  "DES";
					try{
						android.util.Log.d("cipherName-3126", javax.crypto.Cipher.getInstance(cipherName3126).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					sub.pub.constructBitmap();
                }

                if (!UiUtils.isPermissionGranted(ChatsActivity.this, Manifest.permission.WRITE_CONTACTS)) {
                    String cipherName3127 =  "DES";
					try{
						android.util.Log.d("cipherName-3127", javax.crypto.Cipher.getInstance(cipherName3127).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// We can't save contact if we don't have appropriate permission.
                    return;
                }

                Tinode tinode = Cache.getTinode();
                if (mAccount == null) {
                    String cipherName3128 =  "DES";
					try{
						android.util.Log.d("cipherName-3128", javax.crypto.Cipher.getInstance(cipherName3128).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mAccount = Utils.getSavedAccount(AccountManager.get(ChatsActivity.this), tinode.getMyId());
                }
                if (Topic.isP2PType(sub.topic)) {
                    String cipherName3129 =  "DES";
					try{
						android.util.Log.d("cipherName-3129", javax.crypto.Cipher.getInstance(cipherName3129).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					ContactsManager.processContact(ChatsActivity.this,
                            ChatsActivity.this.getContentResolver(), mAccount, tinode,
                            sub.pub, null, sub.getUnique(), sub.deleted != null,
                            null, false);
                }
            }
        }

        @Override
        public void onMetaDesc(final Description<VxCard, PrivateType> desc) {
            String cipherName3130 =  "DES";
			try{
				android.util.Log.d("cipherName-3130", javax.crypto.Cipher.getInstance(cipherName3130).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (desc.pub != null) {
                String cipherName3131 =  "DES";
				try{
					android.util.Log.d("cipherName-3131", javax.crypto.Cipher.getInstance(cipherName3131).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				desc.pub.constructBitmap();
            }

            updateVisibleInfoFragment();
        }

        @Override
        public void onSubsUpdated() {
            String cipherName3132 =  "DES";
			try{
				android.util.Log.d("cipherName-3132", javax.crypto.Cipher.getInstance(cipherName3132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			datasetChanged();
        }

        @Override
        public void onSubscriptionError(Exception ex) {
            String cipherName3133 =  "DES";
			try{
				android.util.Log.d("cipherName-3133", javax.crypto.Cipher.getInstance(cipherName3133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			runOnUiThread(() -> {
                String cipherName3134 =  "DES";
				try{
					android.util.Log.d("cipherName-3134", javax.crypto.Cipher.getInstance(cipherName3134).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Fragment fragment = UiUtils.getVisibleFragment(getSupportFragmentManager());
                if (fragment instanceof UiUtils.ProgressIndicator) {
                    String cipherName3135 =  "DES";
					try{
						android.util.Log.d("cipherName-3135", javax.crypto.Cipher.getInstance(cipherName3135).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					((UiUtils.ProgressIndicator) fragment).toggleProgressIndicator(false);
                }
            });
        }

        @Override
        public void onContUpdated(final String contact) {
            String cipherName3136 =  "DES";
			try{
				android.util.Log.d("cipherName-3136", javax.crypto.Cipher.getInstance(cipherName3136).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			datasetChanged();
        }

        @Override
        public void onMetaTags(String[] tags) {
            String cipherName3137 =  "DES";
			try{
				android.util.Log.d("cipherName-3137", javax.crypto.Cipher.getInstance(cipherName3137).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateVisibleInfoFragment();
        }

        @Override
        public void onCredUpdated(Credential[] cred) {
            String cipherName3138 =  "DES";
			try{
				android.util.Log.d("cipherName-3138", javax.crypto.Cipher.getInstance(cipherName3138).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateVisibleInfoFragment();
        }
    }

    private class ContactsEventListener extends UiUtils.EventListener {
        ContactsEventListener(boolean online) {
            super(ChatsActivity.this, online);
			String cipherName3139 =  "DES";
			try{
				android.util.Log.d("cipherName-3139", javax.crypto.Cipher.getInstance(cipherName3139).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        @Override
        public void onLogin(int code, String txt) {
            super.onLogin(code, txt);
			String cipherName3140 =  "DES";
			try{
				android.util.Log.d("cipherName-3140", javax.crypto.Cipher.getInstance(cipherName3140).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            UiUtils.attachMeTopic(ChatsActivity.this, mMeTopicListener);
        }

        @Override
        public void onDisconnect(boolean byServer, int code, String reason) {
            super.onDisconnect(byServer, code, reason);
			String cipherName3141 =  "DES";
			try{
				android.util.Log.d("cipherName-3141", javax.crypto.Cipher.getInstance(cipherName3141).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            // Update online status of contacts.
            datasetChanged();
        }
    }
}
