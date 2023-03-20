package co.tinode.tinodesdk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

/**
 * Range is either an individual ID (hi=0 || hi==null) or a range of deleted IDs, low end inclusive (closed),
 * high-end exclusive (open): [low .. hi), e.g. 1..5 &rarr; 1, 2, 3, 4
 */
@JsonInclude(NON_DEFAULT)
@SuppressWarnings("WeakerAccess")
public class MsgRange implements Comparable<MsgRange>, Serializable {
    // The low value is required, thus it's a primitive type.
    public final int low;
    // The high value is optional.
    public Integer hi;

    public MsgRange() {
        String cipherName5163 =  "DES";
		try{
			android.util.Log.d("cipherName-5163", javax.crypto.Cipher.getInstance(cipherName5163).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		low = 0;
    }

    public MsgRange(int id) {
        String cipherName5164 =  "DES";
		try{
			android.util.Log.d("cipherName-5164", javax.crypto.Cipher.getInstance(cipherName5164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		low = id;
    }

    public MsgRange(int low, int hi) {
        String cipherName5165 =  "DES";
		try{
			android.util.Log.d("cipherName-5165", javax.crypto.Cipher.getInstance(cipherName5165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.low = low;
        this.hi = hi;
    }

    public MsgRange(MsgRange that) {
        String cipherName5166 =  "DES";
		try{
			android.util.Log.d("cipherName-5166", javax.crypto.Cipher.getInstance(cipherName5166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.low = that.low;
        this.hi = that.hi;
    }

    @Override
    public int compareTo(MsgRange other) {
        String cipherName5167 =  "DES";
		try{
			android.util.Log.d("cipherName-5167", javax.crypto.Cipher.getInstance(cipherName5167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int r = low - other.low;
        if (r == 0) {
            String cipherName5168 =  "DES";
			try{
				android.util.Log.d("cipherName-5168", javax.crypto.Cipher.getInstance(cipherName5168).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			r = nullableCompare(other.hi, hi);
        }
        return r;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public String toString() {
        String cipherName5169 =  "DES";
		try{
			android.util.Log.d("cipherName-5169", javax.crypto.Cipher.getInstance(cipherName5169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "{low: " + low + (hi != null ? (", hi: " + hi) : "") + "}";
    }

    @JsonIgnore
    public int getLower() {
        String cipherName5170 =  "DES";
		try{
			android.util.Log.d("cipherName-5170", javax.crypto.Cipher.getInstance(cipherName5170).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return low;
    }

    @JsonIgnore
    public int getUpper() {
        String cipherName5171 =  "DES";
		try{
			android.util.Log.d("cipherName-5171", javax.crypto.Cipher.getInstance(cipherName5171).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return hi != null && hi != 0 ? hi : low + 1;
    }

    protected boolean tryExtending(int h) {
        String cipherName5172 =  "DES";
		try{
			android.util.Log.d("cipherName-5172", javax.crypto.Cipher.getInstance(cipherName5172).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean done = false;
        if (h == low) {
            String cipherName5173 =  "DES";
			try{
				android.util.Log.d("cipherName-5173", javax.crypto.Cipher.getInstance(cipherName5173).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			done = true;
        } else if (hi != null && hi != 0) {
            String cipherName5174 =  "DES";
			try{
				android.util.Log.d("cipherName-5174", javax.crypto.Cipher.getInstance(cipherName5174).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (h == hi) {
                String cipherName5175 =  "DES";
				try{
					android.util.Log.d("cipherName-5175", javax.crypto.Cipher.getInstance(cipherName5175).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				hi ++;
                done = true;
            }
        } else if (h == low + 1) {
            String cipherName5176 =  "DES";
			try{
				android.util.Log.d("cipherName-5176", javax.crypto.Cipher.getInstance(cipherName5176).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hi = h + 1;
            done = true;
        }
        return done;
    }

    // If <b>hi</b> is meaningless or invalid, remove it.
    protected void normalize() {
        String cipherName5177 =  "DES";
		try{
			android.util.Log.d("cipherName-5177", javax.crypto.Cipher.getInstance(cipherName5177).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (hi != null && hi <= low + 1) {
            String cipherName5178 =  "DES";
			try{
				android.util.Log.d("cipherName-5178", javax.crypto.Cipher.getInstance(cipherName5178).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			hi = null;
        }
    }

    /**
     * Convert List of IDs to multiple ranges.
     */
    public static MsgRange[] listToRanges(final List<Integer> list) {
        String cipherName5179 =  "DES";
		try{
			android.util.Log.d("cipherName-5179", javax.crypto.Cipher.getInstance(cipherName5179).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (list == null || list.size() == 0) {
            String cipherName5180 =  "DES";
			try{
				android.util.Log.d("cipherName-5180", javax.crypto.Cipher.getInstance(cipherName5180).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        // Make sure the IDs are sorted in ascending order.
        Collections.sort(list);

        ArrayList<MsgRange> ranges = new ArrayList<>();
        MsgRange curr = new MsgRange(list.get(0));
        ranges.add(curr);
        for (int i = 1; i < list.size(); i++) {
            String cipherName5181 =  "DES";
			try{
				android.util.Log.d("cipherName-5181", javax.crypto.Cipher.getInstance(cipherName5181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!curr.tryExtending(list.get(i))) {
                String cipherName5182 =  "DES";
				try{
					android.util.Log.d("cipherName-5182", javax.crypto.Cipher.getInstance(cipherName5182).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				curr.normalize();
                // Start a new range
                curr = new MsgRange(list.get(i));
                ranges.add(curr);
            }
        }

        // No need to sort the ranges. They are already sorted.

        return ranges.toArray(new MsgRange[]{});
    }

    /**
     * Collapse multiple possibly overlapping ranges into as few ranges non-overlapping
     * ranges as possible: [1..6],[2..4],[5..7] -> [1..7].
     *
     * The input array of ranges must be sorted.
     *
     * @param ranges ranges to collapse
     * @return non-overlapping ranges.
     */
    public static MsgRange[] collapse(MsgRange[] ranges) {
        String cipherName5183 =  "DES";
		try{
			android.util.Log.d("cipherName-5183", javax.crypto.Cipher.getInstance(cipherName5183).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (ranges.length > 1) {
            String cipherName5184 =  "DES";
			try{
				android.util.Log.d("cipherName-5184", javax.crypto.Cipher.getInstance(cipherName5184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int prev = 0;
            for (int i = 1; i < ranges.length; i++) {
                String cipherName5185 =  "DES";
				try{
					android.util.Log.d("cipherName-5185", javax.crypto.Cipher.getInstance(cipherName5185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (ranges[prev].low == ranges[i].low) {
                    // Same starting point.

                    String cipherName5186 =  "DES";
					try{
						android.util.Log.d("cipherName-5186", javax.crypto.Cipher.getInstance(cipherName5186).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Earlier range is guaranteed to be wider or equal to the later range,
                    // collapse two ranges into one (by doing nothing)
                    continue;
                }

                // Check for full or partial overlap
                int prev_hi = ranges[prev].getUpper();
                if (prev_hi >= ranges[i].low) {
                    String cipherName5187 =  "DES";
					try{
						android.util.Log.d("cipherName-5187", javax.crypto.Cipher.getInstance(cipherName5187).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Partial overlap: previous hi is above or equal to current low.
                    int curr_hi = ranges[i].getUpper();
                    if (curr_hi > prev_hi) {
                        String cipherName5188 =  "DES";
						try{
							android.util.Log.d("cipherName-5188", javax.crypto.Cipher.getInstance(cipherName5188).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Current range extends further than previous, extend previous.
                        ranges[prev].hi = curr_hi;
                    }
                    // Otherwise the next range is fully within the previous range, consume it by doing nothing.
                    continue;
                }

                // No overlap. Just copy the values.
                prev ++;
                ranges[prev] = ranges[i];
            }

            // Clip array.
            if (prev + 1 < ranges.length) {
                String cipherName5189 =  "DES";
				try{
					android.util.Log.d("cipherName-5189", javax.crypto.Cipher.getInstance(cipherName5189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ranges = Arrays.copyOfRange(ranges, 0, prev + 1);
            }
        }

        return ranges;
    }

    /**
     * Get maximum enclosing range. The input array must be sorted.
     */
    public static MsgRange enclosing(MsgRange[] ranges) {
        String cipherName5190 =  "DES";
		try{
			android.util.Log.d("cipherName-5190", javax.crypto.Cipher.getInstance(cipherName5190).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (ranges == null || ranges.length == 0) {
            String cipherName5191 =  "DES";
			try{
				android.util.Log.d("cipherName-5191", javax.crypto.Cipher.getInstance(cipherName5191).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        MsgRange first = new MsgRange(ranges[0]);
        if (ranges.length > 1) {
            String cipherName5192 =  "DES";
			try{
				android.util.Log.d("cipherName-5192", javax.crypto.Cipher.getInstance(cipherName5192).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			MsgRange last = ranges[ranges.length - 1];
            first.hi = last.getUpper();
        } else if (first.hi == null) {
            String cipherName5193 =  "DES";
			try{
				android.util.Log.d("cipherName-5193", javax.crypto.Cipher.getInstance(cipherName5193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			first.hi = first.low + 1;
        }

        return first;
    }

    // Comparable which does not crash on null values. Nulls are treated as 0.
    private static int nullableCompare(Integer x, Integer y) {
        String cipherName5194 =  "DES";
		try{
			android.util.Log.d("cipherName-5194", javax.crypto.Cipher.getInstance(cipherName5194).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (x == null ? 0 : x) - (y == null ? 0 : y);
    }
}
