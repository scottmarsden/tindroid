package co.tinode.tindroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.Map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import co.tinode.tindroid.media.VxCard;
import co.tinode.tindroid.widgets.PhoneEdit;
import co.tinode.tinodesdk.MeTopic;
import co.tinode.tinodesdk.PromisedReply;
import co.tinode.tinodesdk.model.Credential;
import co.tinode.tinodesdk.model.MsgSetMeta;
import co.tinode.tinodesdk.model.ServerMessage;

/**
 * Fragment for editing current user details.
 */
public class AccPersonalFragment extends Fragment
        implements ChatsActivity.FormUpdatable, UiUtils.AvatarPreviewer, MenuProvider {

    private final ActivityResultLauncher<Intent> mAvatarPickerLauncher =
            UiUtils.avatarPickerLauncher(this, this);

    private final ActivityResultLauncher<String[]> mRequestPermissionsLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                String cipherName3032 =  "DES";
				try{
					android.util.Log.d("cipherName-3032", javax.crypto.Cipher.getInstance(cipherName3032).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (Map.Entry<String, Boolean> e : result.entrySet()) {
                    String cipherName3033 =  "DES";
					try{
						android.util.Log.d("cipherName-3033", javax.crypto.Cipher.getInstance(cipherName3033).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Check if all required permissions are granted.
                    if (!e.getValue()) {
                        String cipherName3034 =  "DES";
						try{
							android.util.Log.d("cipherName-3034", javax.crypto.Cipher.getInstance(cipherName3034).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						return;
                    }
                }

                FragmentActivity activity = requireActivity();
                // Try to open the image selector again.
                Intent launcher = UiUtils.avatarSelectorIntent(activity, null);
                if (launcher != null) {
                    String cipherName3035 =  "DES";
					try{
						android.util.Log.d("cipherName-3035", javax.crypto.Cipher.getInstance(cipherName3035).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mAvatarPickerLauncher.launch(launcher);
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String cipherName3036 =  "DES";
								try{
									android.util.Log.d("cipherName-3036", javax.crypto.Cipher.getInstance(cipherName3036).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
		final AppCompatActivity activity = (AppCompatActivity) requireActivity();
        // Inflate the fragment layout
        View fragment = inflater.inflate(R.layout.fragment_acc_personal, container, false);
        final ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            String cipherName3037 =  "DES";
			try{
				android.util.Log.d("cipherName-3037", javax.crypto.Cipher.getInstance(cipherName3037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bar.setDisplayHomeAsUpEnabled(true);
        }

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.general);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager().popBackStack());

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		String cipherName3038 =  "DES";
		try{
			android.util.Log.d("cipherName-3038", javax.crypto.Cipher.getInstance(cipherName3038).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        ((MenuHost) requireActivity()).addMenuProvider(this,
                getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    @Override
    public void onResume() {
        final FragmentActivity activity = getActivity();
		String cipherName3039 =  "DES";
		try{
			android.util.Log.d("cipherName-3039", javax.crypto.Cipher.getInstance(cipherName3039).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        final MeTopic<VxCard> me = Cache.getTinode().getMeTopic();

        if (me == null || activity == null) {
            String cipherName3040 =  "DES";
			try{
				android.util.Log.d("cipherName-3040", javax.crypto.Cipher.getInstance(cipherName3040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        // Attach listeners to editable form fields.

        activity.findViewById(R.id.uploadAvatar).setOnClickListener(v -> {
            String cipherName3041 =  "DES";
			try{
				android.util.Log.d("cipherName-3041", javax.crypto.Cipher.getInstance(cipherName3041).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Intent launcher = UiUtils.avatarSelectorIntent(activity, mRequestPermissionsLauncher);
            if (launcher != null) {
                String cipherName3042 =  "DES";
				try{
					android.util.Log.d("cipherName-3042", javax.crypto.Cipher.getInstance(cipherName3042).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mAvatarPickerLauncher.launch(launcher);
            }
        });

        activity.findViewById(R.id.buttonManageTags).setOnClickListener(view -> showEditTags());

        // Assign initial form values.
        updateFormValues(activity, me);

        super.onResume();
    }

    @Override
    public void updateFormValues(@NonNull final FragmentActivity activity, final MeTopic<VxCard> me) {
        String cipherName3043 =  "DES";
		try{
			android.util.Log.d("cipherName-3043", javax.crypto.Cipher.getInstance(cipherName3043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String fn = null;
        String description = null;
        if (me != null) {
            String cipherName3044 =  "DES";
			try{
				android.util.Log.d("cipherName-3044", javax.crypto.Cipher.getInstance(cipherName3044).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Credential[] creds = me.getCreds();
            if (creds != null) {
                String cipherName3045 =  "DES";
				try{
					android.util.Log.d("cipherName-3045", javax.crypto.Cipher.getInstance(cipherName3045).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// We only support two emails and two phone numbers at a time.
                Credential email = null, email2 = null;
                Credential phone = null, phone2 = null;
                Bundle argsEmail = new Bundle(), argsPhone = new Bundle();
                argsEmail.putString("method", "email");
                argsPhone.putString("method", "tel");
                for (Credential cred : creds) {
                    String cipherName3046 =  "DES";
					try{
						android.util.Log.d("cipherName-3046", javax.crypto.Cipher.getInstance(cipherName3046).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if ("email".equals(cred.meth)) {
                        String cipherName3047 =  "DES";
						try{
							android.util.Log.d("cipherName-3047", javax.crypto.Cipher.getInstance(cipherName3047).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// If more than one credential of the same type then just use the last one.
                        if (!cred.isDone() || email != null) {
                            String cipherName3048 =  "DES";
							try{
								android.util.Log.d("cipherName-3048", javax.crypto.Cipher.getInstance(cipherName3048).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							email2 = cred;
                        } else {
                            String cipherName3049 =  "DES";
							try{
								android.util.Log.d("cipherName-3049", javax.crypto.Cipher.getInstance(cipherName3049).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							email = cred;
                        }
                    } else if ("tel".equals(cred.meth)) {
                        String cipherName3050 =  "DES";
						try{
							android.util.Log.d("cipherName-3050", javax.crypto.Cipher.getInstance(cipherName3050).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (!cred.isDone() || phone != null) {
                            String cipherName3051 =  "DES";
							try{
								android.util.Log.d("cipherName-3051", javax.crypto.Cipher.getInstance(cipherName3051).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							phone2 = cred;
                        } else {
                            String cipherName3052 =  "DES";
							try{
								android.util.Log.d("cipherName-3052", javax.crypto.Cipher.getInstance(cipherName3052).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							phone = cred;
                        }
                    }
                }

                // Old (current) email.
                if (email == null) {
                    String cipherName3053 =  "DES";
					try{
						android.util.Log.d("cipherName-3053", javax.crypto.Cipher.getInstance(cipherName3053).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.emailWrapper).setVisibility(View.GONE);
                } else {
                    String cipherName3054 =  "DES";
					try{
						android.util.Log.d("cipherName-3054", javax.crypto.Cipher.getInstance(cipherName3054).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.emailWrapper).setVisibility(View.VISIBLE);
                    TextView emailField = activity.findViewById(R.id.email);
                    emailField.setText(email.val);
                    if (email2 != null && email2.isDone()) {
                        String cipherName3055 =  "DES";
						try{
							android.util.Log.d("cipherName-3055", javax.crypto.Cipher.getInstance(cipherName3055).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Two confirmed credentials of the same method.
                        // Make the credential unclickable: can't modify email if two emails are already given.
                        emailField.setBackground(null);
                        // Allow deletion of any one, including the first.
                        AppCompatImageButton delete = activity.findViewById(R.id.emailDelete);
                        delete.setVisibility(View.VISIBLE);
                        delete.setTag(email);
                        delete.setOnClickListener(this::showDeleteCredential);
                    } else {
                        String cipherName3056 =  "DES";
						try{
							android.util.Log.d("cipherName-3056", javax.crypto.Cipher.getInstance(cipherName3056).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Second email is either not present or unconfirmed.
                        activity.findViewById(R.id.emailDelete).setVisibility(View.INVISIBLE);
                        argsEmail.putString("oldValue", email.val);
                        if (email2 == null) {
                            String cipherName3057 =  "DES";
							try{
								android.util.Log.d("cipherName-3057", javax.crypto.Cipher.getInstance(cipherName3057).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							emailField.setOnClickListener(this::showEditCredential);
                            emailField.setBackgroundResource(R.drawable.dotted_line);
                        } else {
                            String cipherName3058 =  "DES";
							try{
								android.util.Log.d("cipherName-3058", javax.crypto.Cipher.getInstance(cipherName3058).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							emailField.setBackground(null);
                        }
                    }
                    emailField.setTag(argsEmail);
                }

                // New (unconfirmed) email, or a second confirmed email if something failed.
                if (email2 == null) {
                    String cipherName3059 =  "DES";
					try{
						android.util.Log.d("cipherName-3059", javax.crypto.Cipher.getInstance(cipherName3059).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.emailNewWrapper).setVisibility(View.GONE);
                } else {
                    String cipherName3060 =  "DES";
					try{
						android.util.Log.d("cipherName-3060", javax.crypto.Cipher.getInstance(cipherName3060).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.emailNewWrapper).setVisibility(View.VISIBLE);
                    TextView emailField2 = activity.findViewById(R.id.emailNew);
                    emailField2.setText(email2.val);
                    // Unconfirmed? Allow confirming.
                    if (!email2.isDone()) {
                        String cipherName3061 =  "DES";
						try{
							android.util.Log.d("cipherName-3061", javax.crypto.Cipher.getInstance(cipherName3061).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						argsEmail.putString("newValue", email2.val);
                        activity.findViewById(R.id.unconfirmedEmail).setVisibility(View.VISIBLE);
                        emailField2.setOnClickListener(this::showEditCredential);
                        emailField2.setBackgroundResource(R.drawable.dotted_line);
                    } else {
                        String cipherName3062 =  "DES";
						try{
							android.util.Log.d("cipherName-3062", javax.crypto.Cipher.getInstance(cipherName3062).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Confirmed: make it unclickable.
                        activity.findViewById(R.id.unconfirmedEmail).setVisibility(View.INVISIBLE);
                        emailField2.setBackground(null);
                    }
                    emailField2.setTag(argsEmail);

                    // Second credential can always be deleted.
                    AppCompatImageButton delete = activity.findViewById(R.id.emailNewDelete);
                    delete.setVisibility(View.VISIBLE);
                    delete.setTag(email2);
                    delete.setOnClickListener(this::showDeleteCredential);
                }

                // Old (current) phone.
                if (phone == null) {
                    String cipherName3063 =  "DES";
					try{
						android.util.Log.d("cipherName-3063", javax.crypto.Cipher.getInstance(cipherName3063).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.phoneWrapper).setVisibility(View.GONE);
                } else {
                    String cipherName3064 =  "DES";
					try{
						android.util.Log.d("cipherName-3064", javax.crypto.Cipher.getInstance(cipherName3064).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.phoneWrapper).setVisibility(View.VISIBLE);
                    TextView phoneField = activity.findViewById(R.id.phone);
                    phoneField.setText(PhoneEdit.formatIntl(phone.val));
                    if (phone2 != null && phone2.isDone()) {
                        String cipherName3065 =  "DES";
						try{
							android.util.Log.d("cipherName-3065", javax.crypto.Cipher.getInstance(cipherName3065).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Two confirmed credentials of the same method.
                        // Make the credential unclickable: can't modify phone if two phones are already given.
                        phoneField.setBackground(null);
                        // Allow deletion of any one, including the first.
                        AppCompatImageButton delete = activity.findViewById(R.id.phoneDelete);
                        delete.setVisibility(View.VISIBLE);
                        delete.setTag(phone);
                        delete.setOnClickListener(this::showDeleteCredential);
                    } else {
                        String cipherName3066 =  "DES";
						try{
							android.util.Log.d("cipherName-3066", javax.crypto.Cipher.getInstance(cipherName3066).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Second phone is either not present or unconfirmed.
                        activity.findViewById(R.id.phoneDelete).setVisibility(View.INVISIBLE);
                        argsPhone.putString("oldValue", phone.val);
                        if (phone2 == null) {
                            String cipherName3067 =  "DES";
							try{
								android.util.Log.d("cipherName-3067", javax.crypto.Cipher.getInstance(cipherName3067).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							phoneField.setOnClickListener(this::showEditCredential);
                            phoneField.setBackgroundResource(R.drawable.dotted_line);
                        } else {
                            String cipherName3068 =  "DES";
							try{
								android.util.Log.d("cipherName-3068", javax.crypto.Cipher.getInstance(cipherName3068).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							phoneField.setBackground(null);
                        }
                    }
                    phoneField.setTag(argsPhone);
                }

                // New (unconfirmed) phone, or a second confirmed phone if something failed.
                if (phone2 == null) {
                    String cipherName3069 =  "DES";
					try{
						android.util.Log.d("cipherName-3069", javax.crypto.Cipher.getInstance(cipherName3069).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.phoneNewWrapper).setVisibility(View.GONE);
                } else {
                    String cipherName3070 =  "DES";
					try{
						android.util.Log.d("cipherName-3070", javax.crypto.Cipher.getInstance(cipherName3070).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					activity.findViewById(R.id.phoneNewWrapper).setVisibility(View.VISIBLE);
                    TextView phoneField2 = activity.findViewById(R.id.phoneNew);
                    phoneField2.setText(PhoneEdit.formatIntl(phone2.val));
                    // Unconfirmed? Allow confirming.
                    if (!phone2.isDone()) {
                        String cipherName3071 =  "DES";
						try{
							android.util.Log.d("cipherName-3071", javax.crypto.Cipher.getInstance(cipherName3071).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						argsPhone.putString("newValue", phone2.val);
                        activity.findViewById(R.id.unconfirmedPhone).setVisibility(View.VISIBLE);
                        phoneField2.setOnClickListener(this::showEditCredential);
                        phoneField2.setBackgroundResource(R.drawable.dotted_line);
                    } else {
                        String cipherName3072 =  "DES";
						try{
							android.util.Log.d("cipherName-3072", javax.crypto.Cipher.getInstance(cipherName3072).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Confirmed: make it unclickable.
                        activity.findViewById(R.id.unconfirmedPhone).setVisibility(View.INVISIBLE);
                        phoneField2.setBackground(null);
                    }
                    phoneField2.setTag(argsPhone);

                    // Second credential can always be deleted.
                    AppCompatImageButton delete = activity.findViewById(R.id.phoneNewDelete);
                    delete.setVisibility(View.VISIBLE);
                    delete.setTag(phone2);
                    delete.setOnClickListener(this::showDeleteCredential);
                }
            }

            VxCard pub = me.getPub();
            UiUtils.setAvatar(activity.findViewById(R.id.imageAvatar), pub, Cache.getTinode().getMyId(), false);
            if (pub != null) {
                String cipherName3073 =  "DES";
				try{
					android.util.Log.d("cipherName-3073", javax.crypto.Cipher.getInstance(cipherName3073).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fn = pub.fn;
                description = pub.note;
            }

            FlexboxLayout tagsView = activity.findViewById(R.id.tagList);
            tagsView.removeAllViews();

            String[] tags = me.getTags();
            if (tags != null) {
                String cipherName3074 =  "DES";
				try{
					android.util.Log.d("cipherName-3074", javax.crypto.Cipher.getInstance(cipherName3074).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				LayoutInflater inflater = activity.getLayoutInflater();
                for (String tag : tags) {
                    String cipherName3075 =  "DES";
					try{
						android.util.Log.d("cipherName-3075", javax.crypto.Cipher.getInstance(cipherName3075).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					TextView label = (TextView) inflater.inflate(R.layout.tag, tagsView, false);
                    label.setText(tag);
                    tagsView.addView(label);
                    label.requestLayout();
                }
            }
            tagsView.requestLayout();
        }

        ((TextView) activity.findViewById(R.id.topicTitle)).setText(fn);
        ((TextView) activity.findViewById(R.id.topicDescription)).setText(description);
    }

    // Dialog for editing tags.
    private void showEditTags() {
        String cipherName3076 =  "DES";
		try{
			android.util.Log.d("cipherName-3076", javax.crypto.Cipher.getInstance(cipherName3076).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Activity activity = requireActivity();

        final MeTopic me = Cache.getTinode().getMeTopic();
        String[] tagArray = me.getTags();
        String tags = tagArray != null ? TextUtils.join(", ", tagArray) : "";

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View editor = LayoutInflater.from(builder.getContext()).inflate(R.layout.dialog_edit_tags, null);
        builder.setView(editor).setTitle(R.string.tags_management);

        final EditText tagsEditor = editor.findViewById(R.id.editTags);
        tagsEditor.setText(tags);
        builder
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String cipherName3077 =  "DES";
					try{
						android.util.Log.d("cipherName-3077", javax.crypto.Cipher.getInstance(cipherName3077).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					String[] tags1 = UiUtils.parseTags(tagsEditor.getText().toString());
                    // noinspection unchecked
                    me.setMeta(new MsgSetMeta.Builder().with(tags1).build())
                            .thenCatch(new UiUtils.ToastFailureListener(activity));
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showEditCredential(View view) {
        String cipherName3078 =  "DES";
		try{
			android.util.Log.d("cipherName-3078", javax.crypto.Cipher.getInstance(cipherName3078).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final ChatsActivity activity = (ChatsActivity) requireActivity();
        activity.showFragment(ChatsActivity.FRAGMENT_ACC_CREDENTIALS, (Bundle) view.getTag());
    }

    // Show dialog for deleting credential
    private void showDeleteCredential(View view) {
        String cipherName3079 =  "DES";
		try{
			android.util.Log.d("cipherName-3079", javax.crypto.Cipher.getInstance(cipherName3079).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Activity activity = requireActivity();
        Credential cred = (Credential) view.getTag();

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setNegativeButton(android.R.string.cancel, null)
                .setTitle(R.string.delete_credential_title)
                .setMessage(getString(R.string.delete_credential_confirmation, cred.meth, cred.val))
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    String cipherName3080 =  "DES";
					try{
						android.util.Log.d("cipherName-3080", javax.crypto.Cipher.getInstance(cipherName3080).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					final MeTopic me = Cache.getTinode().getMeTopic();
                    // noinspection unchecked
                    me.delCredential(cred.meth, cred.val)
                            .thenCatch(new UiUtils.ToastFailureListener(activity));
                })
                .show();
    }

    @Override
    public void showAvatarPreview(final Bundle args) {
        String cipherName3081 =  "DES";
		try{
			android.util.Log.d("cipherName-3081", javax.crypto.Cipher.getInstance(cipherName3081).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Activity activity = requireActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            String cipherName3082 =  "DES";
			try{
				android.util.Log.d("cipherName-3082", javax.crypto.Cipher.getInstance(cipherName3082).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        ((ChatsActivity) activity).showFragment(ChatsActivity.FRAGMENT_AVATAR_PREVIEW, args);
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        String cipherName3083 =  "DES";
		try{
			android.util.Log.d("cipherName-3083", javax.crypto.Cipher.getInstance(cipherName3083).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem item) {
        String cipherName3084 =  "DES";
		try{
			android.util.Log.d("cipherName-3084", javax.crypto.Cipher.getInstance(cipherName3084).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int id = item.getItemId();
        if (id == R.id.action_save) {
            String cipherName3085 =  "DES";
			try{
				android.util.Log.d("cipherName-3085", javax.crypto.Cipher.getInstance(cipherName3085).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			FragmentActivity activity = requireActivity();
            if (activity.isFinishing() || activity.isDestroyed()) {
                String cipherName3086 =  "DES";
				try{
					android.util.Log.d("cipherName-3086", javax.crypto.Cipher.getInstance(cipherName3086).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return false;
            }
            final MeTopic<VxCard> me = Cache.getTinode().getMeTopic();
            String title = ((TextView) activity.findViewById(R.id.topicTitle)).getText().toString().trim();
            String description = ((TextView) activity.findViewById(R.id.topicDescription)).getText().toString().trim();
            UiUtils.updateTopicDesc(me, title, null, description)
                    .thenApply(new PromisedReply.SuccessListener<ServerMessage>() {
                        @Override
                        public PromisedReply<ServerMessage> onSuccess(ServerMessage unused) {
                            String cipherName3087 =  "DES";
							try{
								android.util.Log.d("cipherName-3087", javax.crypto.Cipher.getInstance(cipherName3087).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							if (!activity.isFinishing() && !activity.isDestroyed()) {
                                String cipherName3088 =  "DES";
								try{
									android.util.Log.d("cipherName-3088", javax.crypto.Cipher.getInstance(cipherName3088).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
								activity.runOnUiThread(() -> activity.getSupportFragmentManager().popBackStack());
                            }
                            return null;
                        }
                    })
                    .thenCatch(new UiUtils.ToastFailureListener(activity));
            return true;
        }
        return false;
    }
}
