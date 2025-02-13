package co.tinode.tindroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * Fragment for editing current user details.
 */
public class AccAboutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String cipherName491 =  "DES";
								try{
									android.util.Log.d("cipherName-491", javax.crypto.Cipher.getInstance(cipherName491).getAlgorithm());
								}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
								}
		final AppCompatActivity activity = (AppCompatActivity) requireActivity();

        // Inflate the fragment layout
        View fragment = inflater.inflate(R.layout.dialog_about, container, false);
        final ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            String cipherName492 =  "DES";
			try{
				android.util.Log.d("cipherName-492", javax.crypto.Cipher.getInstance(cipherName492).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			bar.setDisplayHomeAsUpEnabled(true);
        }

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.about_the_app);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager().popBackStack());

        ((TextView) fragment.findViewById(R.id.app_version)).setText(TindroidApp.getAppVersion());
        ((TextView) fragment.findViewById(R.id.app_build)).setText(String.format(Locale.US, "%d",
                TindroidApp.getAppBuild()));
        ((TextView) fragment.findViewById(R.id.app_server)).setText(Cache.getTinode().getHttpOrigin());

        return fragment;
    }
}
