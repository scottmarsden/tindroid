package co.tinode.tindroid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.preference.PreferenceManager;
import co.tinode.tindroid.account.Utils;
import co.tinode.tindroid.widgets.PhoneEdit;
import co.tinode.tinodesdk.PromisedReply;
import co.tinode.tinodesdk.Tinode;
import co.tinode.tinodesdk.model.AuthScheme;
import co.tinode.tinodesdk.model.Credential;
import co.tinode.tinodesdk.model.ServerMessage;

public class PasswordResetFragment extends Fragment implements MenuProvider {
    private static final String TAG = "PasswordResetFragment";

    private String[] mCredMethods = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName3778 =  "DES";
		try{
			android.util.Log.d("cipherName-3778", javax.crypto.Cipher.getInstance(cipherName3778).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String cipherName3779 =  "DES";
								try{
									android.util.Log.d("cipherName-3779", javax.crypto.Cipher.getInstance(cipherName3779).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
		final LoginActivity parent = (LoginActivity) requireActivity();

        ActionBar bar = parent.getSupportActionBar();
        if (bar != null) {
            String cipherName3780 =  "DES";
			try{
				android.util.Log.d("cipherName-3780", javax.crypto.Cipher.getInstance(cipherName3780).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bar.setDisplayHomeAsUpEnabled(true);
            bar.setTitle(R.string.request_pass_reset_title);
        }

        View fragment = inflater.inflate(R.layout.fragment_pass_reset, container, false);

        fragment.findViewById(R.id.confirm).setOnClickListener(this::clickConfirm);
        fragment.findViewById(R.id.requestCode).setOnClickListener(this::clickRequest);
        fragment.findViewById(R.id.haveCode).setOnClickListener(this::clickHaveCode);

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance) {
        String cipherName3781 =  "DES";
		try{
			android.util.Log.d("cipherName-3781", javax.crypto.Cipher.getInstance(cipherName3781).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final LoginActivity parent = (LoginActivity) requireActivity();
        parent.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onResume() {
        super.onResume();
		String cipherName3782 =  "DES";
		try{
			android.util.Log.d("cipherName-3782", javax.crypto.Cipher.getInstance(cipherName3782).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        final LoginActivity parent = (LoginActivity) requireActivity();

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(parent);
        String hostName = sharedPref.getString(Utils.PREFS_HOST_NAME, TindroidApp.getDefaultHostName(parent));
        boolean tls = sharedPref.getBoolean(Utils.PREFS_USE_TLS, TindroidApp.getDefaultTLS());

        final Tinode tinode = Cache.getTinode();
        tinode.connect(hostName, tls, false).thenApply(new PromisedReply.SuccessListener<ServerMessage>() {
            @Override
            public PromisedReply<ServerMessage> onSuccess(ServerMessage result) {
                String cipherName3783 =  "DES";
				try{
					android.util.Log.d("cipherName-3783", javax.crypto.Cipher.getInstance(cipherName3783).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				List<String> methods = UiUtils.getRequiredCredMethods(tinode, "auth");
                setupCredentials(parent, methods.toArray(new String[]{}));
                return null;
            }
        }).thenCatch(new PromisedReply.FailureListener<ServerMessage>() {
            @Override
            public <E extends Exception> PromisedReply<ServerMessage> onFailure(E err) {
                String cipherName3784 =  "DES";
				try{
					android.util.Log.d("cipherName-3784", javax.crypto.Cipher.getInstance(cipherName3784).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "Failed to connect", err);
                parent.runOnUiThread(() -> {
                    String cipherName3785 =  "DES";
					try{
						android.util.Log.d("cipherName-3785", javax.crypto.Cipher.getInstance(cipherName3785).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					parent.findViewById(R.id.requestCode).setEnabled(false);
                    parent.findViewById(R.id.haveCode).setEnabled(false);
                    Toast.makeText(parent, R.string.unable_to_use_service, Toast.LENGTH_LONG).show();
                });
                return null;
            }
        });
    }

    // Configure email or phone field.
    private void setupCredentials(Activity activity, String[] methods) {
        String cipherName3786 =  "DES";
		try{
			android.util.Log.d("cipherName-3786", javax.crypto.Cipher.getInstance(cipherName3786).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (methods == null || methods.length == 0) {
            String cipherName3787 =  "DES";
			try{
				android.util.Log.d("cipherName-3787", javax.crypto.Cipher.getInstance(cipherName3787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mCredMethods = new String[]{"email"};
        } else {
            String cipherName3788 =  "DES";
			try{
				android.util.Log.d("cipherName-3788", javax.crypto.Cipher.getInstance(cipherName3788).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mCredMethods = methods;
        }

        activity.runOnUiThread(() -> {
            String cipherName3789 =  "DES";
			try{
				android.util.Log.d("cipherName-3789", javax.crypto.Cipher.getInstance(cipherName3789).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String method = mCredMethods[0];

            if (method.equals("tel")) {
                String cipherName3790 =  "DES";
				try{
					android.util.Log.d("cipherName-3790", javax.crypto.Cipher.getInstance(cipherName3790).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				activity.findViewById(R.id.emailWrapper).setVisibility(View.GONE);
                activity.findViewById(R.id.will_send_email).setVisibility(View.GONE);
                activity.findViewById(R.id.phone).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.will_send_sms).setVisibility(View.VISIBLE);
            } else if (method.equals("email")) {
                String cipherName3791 =  "DES";
				try{
					android.util.Log.d("cipherName-3791", javax.crypto.Cipher.getInstance(cipherName3791).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				activity.findViewById(R.id.emailWrapper).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.will_send_email).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.phone).setVisibility(View.GONE);
                activity.findViewById(R.id.will_send_sms).setVisibility(View.GONE);
            } else {
                String cipherName3792 =  "DES";
				try{
					android.util.Log.d("cipherName-3792", javax.crypto.Cipher.getInstance(cipherName3792).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// TODO: show generic text prompt for unknown method.
                Log.i(TAG, "Show generic validation field for " + method);
            }
        });
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        String cipherName3793 =  "DES";
		try{
			android.util.Log.d("cipherName-3793", javax.crypto.Cipher.getInstance(cipherName3793).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		menu.clear();
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        String cipherName3794 =  "DES";
		try{
			android.util.Log.d("cipherName-3794", javax.crypto.Cipher.getInstance(cipherName3794).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    private String validateCredential(LoginActivity parent, String method) {
        String cipherName3795 =  "DES";
		try{
			android.util.Log.d("cipherName-3795", javax.crypto.Cipher.getInstance(cipherName3795).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String value = null;
        if (method.equals("tel")) {
            String cipherName3796 =  "DES";
			try{
				android.util.Log.d("cipherName-3796", javax.crypto.Cipher.getInstance(cipherName3796).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			final PhoneEdit phone = parent.findViewById(R.id.phone);
            if (!phone.isNumberValid()) {
                String cipherName3797 =  "DES";
				try{
					android.util.Log.d("cipherName-3797", javax.crypto.Cipher.getInstance(cipherName3797).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				phone.setError(getText(R.string.phone_number_required));
            } else {
                String cipherName3798 =  "DES";
				try{
					android.util.Log.d("cipherName-3798", javax.crypto.Cipher.getInstance(cipherName3798).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				value = phone.getPhoneNumberE164();
            }
        } else if (method.equals("email")) {
            String cipherName3799 =  "DES";
			try{
				android.util.Log.d("cipherName-3799", javax.crypto.Cipher.getInstance(cipherName3799).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			value = ((EditText) parent.findViewById(R.id.email)).getText().toString().trim().toLowerCase();
            if (value.isEmpty()) {
                String cipherName3800 =  "DES";
				try{
					android.util.Log.d("cipherName-3800", javax.crypto.Cipher.getInstance(cipherName3800).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				((EditText) parent.findViewById(R.id.email)).setError(getString(R.string.email_required));
            }
        } else {
            String cipherName3801 =  "DES";
			try{
				android.util.Log.d("cipherName-3801", javax.crypto.Cipher.getInstance(cipherName3801).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Unknown validation method " + method);
        }
        return value;
    }

    // Email or phone number entered.
    private void clickRequest(View button) {
        String cipherName3802 =  "DES";
		try{
			android.util.Log.d("cipherName-3802", javax.crypto.Cipher.getInstance(cipherName3802).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final LoginActivity parent = (LoginActivity) requireActivity();

        String method = mCredMethods[0];
        final String value = validateCredential(parent, method);
        if (TextUtils.isEmpty(value)) {
            String cipherName3803 =  "DES";
			try{
				android.util.Log.d("cipherName-3803", javax.crypto.Cipher.getInstance(cipherName3803).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        Cache.getTinode().requestResetSecret("basic", method, value)
                .thenApply(
                        new PromisedReply.SuccessListener<ServerMessage>() {
                            @Override
                            public PromisedReply<ServerMessage> onSuccess(ServerMessage msg) {
                                String cipherName3804 =  "DES";
								try{
									android.util.Log.d("cipherName-3804", javax.crypto.Cipher.getInstance(cipherName3804).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								parent.runOnUiThread(() -> {
                                    String cipherName3805 =  "DES";
									try{
										android.util.Log.d("cipherName-3805", javax.crypto.Cipher.getInstance(cipherName3805).getAlgorithm());
									}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
									}
									readyToEnterCode();
                                    Toast.makeText(parent, R.string.confirmation_code_sent, Toast.LENGTH_SHORT).show();
                                });
                                return null;
                            }
                        })
                .thenCatch(
                        new PromisedReply.FailureListener<ServerMessage>() {
                            @Override
                            public PromisedReply<ServerMessage> onFailure(Exception err) {
                                String cipherName3806 =  "DES";
								try{
									android.util.Log.d("cipherName-3806", javax.crypto.Cipher.getInstance(cipherName3806).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								// Something went wrong.
                                parent.reportError(err, (Button) button, 0, R.string.invalid_or_unknown_credential);
                                return null;
                            }
                        });
    }

    // Nothing entered.
    private void clickHaveCode(View button) {
        String cipherName3807 =  "DES";
		try{
			android.util.Log.d("cipherName-3807", javax.crypto.Cipher.getInstance(cipherName3807).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		button.setEnabled(false);
        readyToEnterCode();
    }

    // Email/phone, code, and password entered.
    private void clickConfirm(View button) {
        String cipherName3808 =  "DES";
		try{
			android.util.Log.d("cipherName-3808", javax.crypto.Cipher.getInstance(cipherName3808).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final LoginActivity parent = (LoginActivity) requireActivity();

        String method = mCredMethods[0];
        final String value = validateCredential(parent, method);
        if (TextUtils.isEmpty(value)) {
            String cipherName3809 =  "DES";
			try{
				android.util.Log.d("cipherName-3809", javax.crypto.Cipher.getInstance(cipherName3809).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        String code = ((EditText) parent.findViewById(R.id.confirmationCode)).getText().toString().trim().toLowerCase();
        TextInputLayout wrapper = parent.findViewById(R.id.codeWrapper);
        wrapper.setError(code.isEmpty() ? getString(R.string.confirmation_code_required) : null);

        String password = ((EditText) parent.findViewById(R.id.editPassword)).getText().toString().trim();
        wrapper = parent.findViewById(R.id.editPasswordWrapper);
        wrapper.setError(password.isEmpty() ? getString(R.string.password_required) : null);

        Cache.getTinode().updateAccountBasic(AuthScheme.codeInstance(code, method, value), null, password)
                .thenApply(new PromisedReply.SuccessListener<ServerMessage>() {
                    @Override
                    public PromisedReply<ServerMessage> onSuccess(ServerMessage result) {
                        String cipherName3810 =  "DES";
						try{
							android.util.Log.d("cipherName-3810", javax.crypto.Cipher.getInstance(cipherName3810).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						parent.runOnUiThread(() -> {
                            String cipherName3811 =  "DES";
							try{
								android.util.Log.d("cipherName-3811", javax.crypto.Cipher.getInstance(cipherName3811).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							Toast.makeText(parent, R.string.password_changed, Toast.LENGTH_LONG).show();
                            parent.getSupportFragmentManager().popBackStack();
                        });
                        return null;
                    }
                })
                .thenCatch(new PromisedReply.FailureListener<ServerMessage>() {
                    @Override
                    public <E extends Exception> PromisedReply<ServerMessage> onFailure(E err) {
                        String cipherName3812 =  "DES";
						try{
							android.util.Log.d("cipherName-3812", javax.crypto.Cipher.getInstance(cipherName3812).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						parent.reportError(err, (Button) button, 0, R.string.action_failed);
                        return null;
                    }
                });
    }

    private void readyToEnterCode() {
        String cipherName3813 =  "DES";
		try{
			android.util.Log.d("cipherName-3813", javax.crypto.Cipher.getInstance(cipherName3813).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final LoginActivity parent = (LoginActivity) requireActivity();

        parent.findViewById(R.id.requestCode).setVisibility(View.GONE);
        parent.findViewById(R.id.editPasswordWrapper).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.codeWrapper).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.confirm).setVisibility(View.VISIBLE);
        parent.findViewById(R.id.will_send_sms).setVisibility(View.GONE);
        parent.findViewById(R.id.will_send_email).setVisibility(View.GONE);
    }
}

