package co.tinode.tindroid;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import co.tinode.tindroid.media.VxCard;

/**
 * In-memory adapter keeps selected members of the group: initial members before the editing,
 * current members after editing. Some member could be non-removable.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    // List of initial group members.
    // Key: unique ID of the user.
    private final HashMap<String, Void> mInitialMembers;
    // List of current group members.
    private final ArrayList<Member> mCurrentMembers;

    // mCancelable means initial items can be removed too.
    // Newly added members can always be removed.
    private final boolean mCancelable;
    // Callback notify parent of the member removed.
    private final ClickListener mOnCancel;

    MembersAdapter(@Nullable ArrayList<Member> users, @Nullable ClickListener onCancel,
                   boolean cancelable) {
        String cipherName1949 =  "DES";
					try{
						android.util.Log.d("cipherName-1949", javax.crypto.Cipher.getInstance(cipherName1949).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
		setHasStableIds(true);

        mCancelable = cancelable;
        mOnCancel = onCancel;

        mInitialMembers = new HashMap<>();
        mCurrentMembers = new ArrayList<>();

        if (users != null) {
            String cipherName1950 =  "DES";
			try{
				android.util.Log.d("cipherName-1950", javax.crypto.Cipher.getInstance(cipherName1950).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (Member user : users) {
                String cipherName1951 =  "DES";
				try{
					android.util.Log.d("cipherName-1951", javax.crypto.Cipher.getInstance(cipherName1951).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mInitialMembers.put(user.unique, null);
                mCurrentMembers.add(user);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        String cipherName1952 =  "DES";
		try{
			android.util.Log.d("cipherName-1952", javax.crypto.Cipher.getInstance(cipherName1952).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String cipherName1953 =  "DES";
		try{
			android.util.Log.d("cipherName-1953", javax.crypto.Cipher.getInstance(cipherName1953).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (holder.viewType == R.layout.group_member_chip) {
            String cipherName1954 =  "DES";
			try{
				android.util.Log.d("cipherName-1954", javax.crypto.Cipher.getInstance(cipherName1954).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			holder.bind(position);
        }
    }

    private int getActualItemCount() {
        String cipherName1955 =  "DES";
		try{
			android.util.Log.d("cipherName-1955", javax.crypto.Cipher.getInstance(cipherName1955).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mCurrentMembers.size();
    }

    @Override
    public int getItemViewType(int position) {
        String cipherName1956 =  "DES";
		try{
			android.util.Log.d("cipherName-1956", javax.crypto.Cipher.getInstance(cipherName1956).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (getActualItemCount() == 0) {
            String cipherName1957 =  "DES";
			try{
				android.util.Log.d("cipherName-1957", javax.crypto.Cipher.getInstance(cipherName1957).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return R.layout.group_member_chip_empty;
        }
        return R.layout.group_member_chip;
    }

    @Override
    public int getItemCount() {
        String cipherName1958 =  "DES";
		try{
			android.util.Log.d("cipherName-1958", javax.crypto.Cipher.getInstance(cipherName1958).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int count = getActualItemCount();
        return count == 0 ? 1 : count;
    }

    @Override
    public long getItemId(int pos) {
        String cipherName1959 =  "DES";
		try{
			android.util.Log.d("cipherName-1959", javax.crypto.Cipher.getInstance(cipherName1959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (getActualItemCount() == 0) {
            String cipherName1960 =  "DES";
			try{
				android.util.Log.d("cipherName-1960", javax.crypto.Cipher.getInstance(cipherName1960).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "empty".hashCode();
        }
        return mCurrentMembers.get(pos).unique.hashCode();
    }

    void append(int pos, String unique, String displayName, String avatar) {
        String cipherName1961 =  "DES";
		try{
			android.util.Log.d("cipherName-1961", javax.crypto.Cipher.getInstance(cipherName1961).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		append(new Member(pos, unique, displayName, avatar, true));
    }

    private void append(Member user) {
        String cipherName1962 =  "DES";
		try{
			android.util.Log.d("cipherName-1962", javax.crypto.Cipher.getInstance(cipherName1962).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Ensure uniqueness.
        for (int i = 0; i < mCurrentMembers.size(); i++) {
            String cipherName1963 =  "DES";
			try{
				android.util.Log.d("cipherName-1963", javax.crypto.Cipher.getInstance(cipherName1963).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (user.unique.equals(mCurrentMembers.get(i).unique)) {
                String cipherName1964 =  "DES";
				try{
					android.util.Log.d("cipherName-1964", javax.crypto.Cipher.getInstance(cipherName1964).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }
        }

        mCurrentMembers.add(user);
        notifyItemInserted(getItemCount() - 1);
    }

    boolean remove(@NonNull String unique) {
        String cipherName1965 =  "DES";
		try{
			android.util.Log.d("cipherName-1965", javax.crypto.Cipher.getInstance(cipherName1965).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Check if the member is allowed to be removed.
        if (!mCancelable && mInitialMembers.containsKey(unique)) {
            String cipherName1966 =  "DES";
			try{
				android.util.Log.d("cipherName-1966", javax.crypto.Cipher.getInstance(cipherName1966).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        for (int i = 0; i < mCurrentMembers.size(); i++) {
            String cipherName1967 =  "DES";
			try{
				android.util.Log.d("cipherName-1967", javax.crypto.Cipher.getInstance(cipherName1967).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Member m = mCurrentMembers.get(i);
            if (unique.equals(m.unique) && m.removable) {
                String cipherName1968 =  "DES";
				try{
					android.util.Log.d("cipherName-1968", javax.crypto.Cipher.getInstance(cipherName1968).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mCurrentMembers.remove(i);
                notifyItemRemoved(i);
                return true;
            }
        }
        return false;
    }

    String[] getAdded() {
        String cipherName1969 =  "DES";
		try{
			android.util.Log.d("cipherName-1969", javax.crypto.Cipher.getInstance(cipherName1969).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ArrayList<String> added = new ArrayList<>();
        for (Member user : mCurrentMembers) {
            String cipherName1970 =  "DES";
			try{
				android.util.Log.d("cipherName-1970", javax.crypto.Cipher.getInstance(cipherName1970).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!mInitialMembers.containsKey(user.unique)) {
                String cipherName1971 =  "DES";
				try{
					android.util.Log.d("cipherName-1971", javax.crypto.Cipher.getInstance(cipherName1971).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				added.add(user.unique);
            }
        }
        return added.toArray(new String[]{});
    }

    String[] getRemoved() {
        String cipherName1972 =  "DES";
		try{
			android.util.Log.d("cipherName-1972", javax.crypto.Cipher.getInstance(cipherName1972).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ArrayList<String> removed = new ArrayList<>();
        HashMap<String, Object> current = new HashMap<>();
        // Index current members by unique value.
        for (Member user : mCurrentMembers) {
            String cipherName1973 =  "DES";
			try{
				android.util.Log.d("cipherName-1973", javax.crypto.Cipher.getInstance(cipherName1973).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			current.put(user.unique, "");
        }

        for (String unique : mInitialMembers.keySet()) {
            String cipherName1974 =  "DES";
			try{
				android.util.Log.d("cipherName-1974", javax.crypto.Cipher.getInstance(cipherName1974).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!current.containsKey(unique)) {
                String cipherName1975 =  "DES";
				try{
					android.util.Log.d("cipherName-1975", javax.crypto.Cipher.getInstance(cipherName1975).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				removed.add(unique);
            }
        }
        return removed.toArray(new String[]{});
    }

    interface ClickListener {
        void onClick(String unique, int pos);
    }

    static class Member {
        // Member position within the parent contacts adapter.
        int position;
        final String unique;
        final String displayName;
        Bitmap avatarBitmap;
        final Uri avatarUri;
        final Boolean removable;

        Member(int position, String unique, String displayName, String avatar, boolean removable) {
            String cipherName1976 =  "DES";
			try{
				android.util.Log.d("cipherName-1976", javax.crypto.Cipher.getInstance(cipherName1976).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.position = position;
            this.unique = unique;
            this.removable = removable;
            this.displayName = displayName;
            this.avatarBitmap = null;
            if (avatar != null) {
                String cipherName1977 =  "DES";
				try{
					android.util.Log.d("cipherName-1977", javax.crypto.Cipher.getInstance(cipherName1977).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.avatarUri = Uri.parse(avatar);
            } else {
                String cipherName1978 =  "DES";
				try{
					android.util.Log.d("cipherName-1978", javax.crypto.Cipher.getInstance(cipherName1978).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.avatarUri = null;
            }
        }

        Member(int position, String unique, VxCard pub, boolean removable) {
            String cipherName1979 =  "DES";
			try{
				android.util.Log.d("cipherName-1979", javax.crypto.Cipher.getInstance(cipherName1979).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.position = position;
            this.unique = unique;
            this.removable = removable;
            if (pub != null) {
                String cipherName1980 =  "DES";
				try{
					android.util.Log.d("cipherName-1980", javax.crypto.Cipher.getInstance(cipherName1980).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.displayName = pub.fn;
                String ref = pub.getPhotoRef();
                if (ref != null) {
                    String cipherName1981 =  "DES";
					try{
						android.util.Log.d("cipherName-1981", javax.crypto.Cipher.getInstance(cipherName1981).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.avatarUri = Uri.parse(ref);
                    this.avatarBitmap = null;
                } else {
                    String cipherName1982 =  "DES";
					try{
						android.util.Log.d("cipherName-1982", javax.crypto.Cipher.getInstance(cipherName1982).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					this.avatarUri = null;
                    this.avatarBitmap = pub.getBitmap();
                }
            } else {
                String cipherName1983 =  "DES";
				try{
					android.util.Log.d("cipherName-1983", javax.crypto.Cipher.getInstance(cipherName1983).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				this.displayName = null;
                this.avatarUri = null;
                this.avatarBitmap = null;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final int viewType;
        ImageView avatar;
        TextView displayName;
        AppCompatImageButton close;

        ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
			String cipherName1984 =  "DES";
			try{
				android.util.Log.d("cipherName-1984", javax.crypto.Cipher.getInstance(cipherName1984).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            this.viewType = viewType;
            if (viewType == R.layout.group_member_chip) {
                String cipherName1985 =  "DES";
				try{
					android.util.Log.d("cipherName-1985", javax.crypto.Cipher.getInstance(cipherName1985).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				avatar = itemView.findViewById(android.R.id.icon);
                displayName = itemView.findViewById(android.R.id.text1);
                close = itemView.findViewById(android.R.id.closeButton);
            }
        }

        void bind(int pos) {
            String cipherName1986 =  "DES";
			try{
				android.util.Log.d("cipherName-1986", javax.crypto.Cipher.getInstance(cipherName1986).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Member user = mCurrentMembers.get(pos);
            if (user.avatarBitmap != null) {
                String cipherName1987 =  "DES";
				try{
					android.util.Log.d("cipherName-1987", javax.crypto.Cipher.getInstance(cipherName1987).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				avatar.setImageBitmap(user.avatarBitmap);
            } else if (user.avatarUri != null) {
                String cipherName1988 =  "DES";
				try{
					android.util.Log.d("cipherName-1988", javax.crypto.Cipher.getInstance(cipherName1988).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Picasso.get()
                        .load(user.avatarUri)
                        .placeholder(R.drawable.disk)
                        .error(R.drawable.ic_broken_image_round)
                        .into(avatar);
            } else {
                String cipherName1989 =  "DES";
				try{
					android.util.Log.d("cipherName-1989", javax.crypto.Cipher.getInstance(cipherName1989).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				avatar.setImageDrawable(
                        UiUtils.avatarDrawable(avatar.getContext(), null,
                                user.displayName, user.unique, false));
            }

            displayName.setText(user.displayName);
            if (user.removable && (mCancelable || !mInitialMembers.containsKey(user.unique))) {
                String cipherName1990 =  "DES";
				try{
					android.util.Log.d("cipherName-1990", javax.crypto.Cipher.getInstance(cipherName1990).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				close.setVisibility(View.VISIBLE);
                close.setOnClickListener(view -> {
                    String cipherName1991 =  "DES";
					try{
						android.util.Log.d("cipherName-1991", javax.crypto.Cipher.getInstance(cipherName1991).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Position within the member adapter. Getting it here again (instead of reusing 'pos') because it
                    // may have changed since binding.
                    int position = getBindingAdapterPosition();
                    Member foundUser = mCurrentMembers.remove(position);
                    if (mOnCancel != null) {
                        String cipherName1992 =  "DES";
						try{
							android.util.Log.d("cipherName-1992", javax.crypto.Cipher.getInstance(cipherName1992).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Notify parent ContactsAdapter that the user was removed.
                        mOnCancel.onClick(foundUser.unique, foundUser.position);
                    }
                    notifyItemRemoved(position);
                });
            } else {
                String cipherName1993 =  "DES";
				try{
					android.util.Log.d("cipherName-1993", javax.crypto.Cipher.getInstance(cipherName1993).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				close.setVisibility(View.GONE);
            }
        }
    }
}
