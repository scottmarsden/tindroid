package co.tinode.tindroid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import androidx.loader.content.CursorLoader;
import co.tinode.tinodesdk.Topic;
import co.tinode.tinodesdk.model.MsgRange;

/**
 * The table contains messages synchronized with the server and not yet synchronized.
 * It also contains message deletion markers, synchronized and not yet synchronized.
 * <p>
 * Storage structure for messages:
 * public String id -> _id
 * public String topic -> as topic_id
 * public String from; -> as user_id
 * public Date ts;
 * public int seq;
 * public Map head -> serialized into JSON;
 * public T content -> serialized into JSON;
 */
public class MessageDb implements BaseColumns {
    private static final String TAG = "MessageDb";

    static final int MESSAGE_PREVIEW_LENGTH = 80;
    /**
     * The name of the main table.
     */
    static final String TABLE_NAME = "messages";
    /**
     * Topic ID, references topics._ID
     */
    static final String COLUMN_NAME_TOPIC_ID = "topic_id";
    /**
     * Id of the originator of the message, references users._ID
     */
    private static final String COLUMN_NAME_USER_ID = "user_id";
    /**
     * Status of the message: unsent, delivered, deleted
     */
    private static final String COLUMN_NAME_STATUS = "status";
    /**
     * Uid as string. Deserialized here to avoid a join.
     */
    private static final String COLUMN_NAME_SENDER = "sender";
    /**
     * Message timestamp
     */
    private static final String COLUMN_NAME_TS = "ts";
    /**
     * Server-issued sequence ID, integer, indexed. If the message represents
     * a deleted range, then <tt>seq</tt> is the lowest bound of the range;
     * the bound is closed (inclusive).
     */
    private static final String COLUMN_NAME_SEQ = "seq";
    /**
     * If message represents a deleted range, this is the upper bound of the range, NULL otherwise.
     * The bound is open (exclusive).
     */
    private static final String COLUMN_NAME_HIGH = "high";
    /**
     * If message represents a deleted range, ID of the deletion record.
     */
    private static final String COLUMN_NAME_DEL_ID = "del_id";
    /**
     * If the message replaces another message, the ID of the message being replaced (from head).
     */
    private static final String COLUMN_NAME_REPLACES_SEQ ="repl_seq";
    /**
     * Timestamp of the original message this message has replaced
     * (could be the same as tc, if it does not replace anything).
     */
    private static final String COLUMN_NAME_EFFECTIVE_TS ="eff_ts";
    /**
     * If not NULL, then this message is the latest in edit history and this is the seq ID of the message it replaced.
     */
    private static final String COLUMN_NAME_EFFECTIVE_SEQ ="eff_seq";
    /**
     * Serialized header.
     */
    private static final String COLUMN_NAME_HEAD = "head";
    /**
     * Serialized message content
     */
    private static final String COLUMN_NAME_CONTENT = "content";
    /**
     * SQL statement to create Messages table
     */
    static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TOPIC_ID
                    + " REFERENCES " + TopicDb.TABLE_NAME + "(" + TopicDb._ID + ")," +
                    COLUMN_NAME_USER_ID
                    + " REFERENCES " + UserDb.TABLE_NAME + "(" + UserDb._ID + ")," +
                    COLUMN_NAME_STATUS + " INT," +
                    COLUMN_NAME_SENDER + " TEXT," +
                    COLUMN_NAME_TS + " INT," +
                    COLUMN_NAME_SEQ + " INT," +
                    COLUMN_NAME_HIGH + " INT," +
                    COLUMN_NAME_DEL_ID + " INT," +
                    COLUMN_NAME_REPLACES_SEQ + " INT," +
                    COLUMN_NAME_EFFECTIVE_TS + " INT," +
                    COLUMN_NAME_EFFECTIVE_SEQ + " INT," +
                    COLUMN_NAME_HEAD + " TEXT," +
                    COLUMN_NAME_CONTENT + " TEXT)";

    static final int COLUMN_IDX_ID = 0;
    static final int COLUMN_IDX_TOPIC_ID = 1;
    static final int COLUMN_IDX_USER_ID = 2;
    static final int COLUMN_IDX_STATUS = 3;
    static final int COLUMN_IDX_SENDER = 4;
    // static final int COLUMN_IDX_TS = 5;
    static final int COLUMN_IDX_SEQ = 6;
    static final int COLUMN_IDX_HIGH = 7;
    static final int COLUMN_IDX_DEL_ID = 8;
    // static final int COLUMN_IDX_REPLACES_SEQ = 9;
    static final int COLUMN_IDX_REPLACES_TS = 10;
    static final int COLUMN_IDX_EFFECTIVE_SEQ = 11;
    static final int COLUMN_IDX_HEAD = 12;
    static final int COLUMN_IDX_CONTENT = 13;
    // Used in JOIN.
    static final int COLUMN_IDX_TOPIC_NAME = 14;

    /**
     * SQL statement to drop Messages table.
     */
    static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    /**
     * The name of index: messages by topic and sequence.
     */
    private static final String INDEX_NAME = "message_topic_id_seq";
    private static final String INDEX_NAME_2 = "message_topic_id_eff_seq";
    /**
     * Drop the indexes too
     */
    static final String DROP_INDEX = "DROP INDEX IF EXISTS " + INDEX_NAME;
    static final String DROP_INDEX_2 = "DROP INDEX IF EXISTS " + INDEX_NAME_2;
    /**
     * Add unique index on topic-seq, in descending order
     */
    static final String CREATE_INDEX =
            "CREATE UNIQUE INDEX " + INDEX_NAME +
                    " ON " + TABLE_NAME + " (" +
                    COLUMN_NAME_TOPIC_ID + "," +
                    COLUMN_NAME_SEQ + " DESC)";

    static final String CREATE_INDEX_2 =
            "CREATE UNIQUE INDEX " + INDEX_NAME_2 +
                    " ON " + TABLE_NAME + " (" +
                    COLUMN_NAME_TOPIC_ID + "," +
                    COLUMN_NAME_EFFECTIVE_SEQ + " DESC) WHERE " + COLUMN_NAME_EFFECTIVE_SEQ + " IS NOT NULL";
    /**
     * Save message to DB.
     *
     * @return ID of the newly added message
     */
    static long insert(SQLiteDatabase db, Topic topic, StoredMessage msg) {
        String cipherName2597 =  "DES";
		try{
			android.util.Log.d("cipherName-2597", javax.crypto.Cipher.getInstance(cipherName2597).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (msg.id > 0) {
            String cipherName2598 =  "DES";
			try{
				android.util.Log.d("cipherName-2598", javax.crypto.Cipher.getInstance(cipherName2598).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Message is already inserted.
            return msg.id;
        }

        if (msg.topicId <= 0) {
            String cipherName2599 =  "DES";
			try{
				android.util.Log.d("cipherName-2599", javax.crypto.Cipher.getInstance(cipherName2599).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			msg.topicId = TopicDb.getId(db, msg.topic);
        }

        if (msg.topicId <= 0) {
            String cipherName2600 =  "DES";
			try{
				android.util.Log.d("cipherName-2600", javax.crypto.Cipher.getInstance(cipherName2600).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Failed to insert message (topic not found) " + msg.seq);
            return -1;
        }

        db.beginTransaction();
        try {
            String cipherName2601 =  "DES";
			try{
				android.util.Log.d("cipherName-2601", javax.crypto.Cipher.getInstance(cipherName2601).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int effSeq = msg.getReplacementSeqId();
            long effTs = -1;
            if (effSeq > 0) {
                String cipherName2602 =  "DES";
				try{
					android.util.Log.d("cipherName-2602", javax.crypto.Cipher.getInstance(cipherName2602).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// This is a replacement message. Two cases:
                // 1. The original message is already received and stored in DB. It should be replaced with this one.
                // 2. The original message is not in the DB and thus this message should not be shown to the user.
                Cursor c = getMessageBySeq(db, msg.topicId, effSeq);
                StoredMessage latestMsg = null;
                if (c.moveToFirst()) {
                    String cipherName2603 =  "DES";
					try{
						android.util.Log.d("cipherName-2603", javax.crypto.Cipher.getInstance(cipherName2603).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					latestMsg = StoredMessage.readMessage(c, 0);
                    effTs = latestMsg.ts.getTime();
                }
                c.close();

                // Replacement message.
                if (latestMsg != null && (msg.seq == 0 || msg.seq > latestMsg.seq)) {
                    String cipherName2604 =  "DES";
					try{
						android.util.Log.d("cipherName-2604", javax.crypto.Cipher.getInstance(cipherName2604).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Case 1: newer version while the original is found.
                    // Clear the effective_seq (invalidate) of all older effective message records.
                    deactivateMessageVersion(db, msg.topicId, effSeq);
                } else {
                    String cipherName2605 =  "DES";
					try{
						android.util.Log.d("cipherName-2605", javax.crypto.Cipher.getInstance(cipherName2605).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Case 2: original not found. Do not set effective seq.
                    effSeq = -1;
                }
            } else {
                String cipherName2606 =  "DES";
				try{
					android.util.Log.d("cipherName-2606", javax.crypto.Cipher.getInstance(cipherName2606).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// This is not a replacement message. Three cases:
                // 1. This is a never edited message.
                // 2. Edited message but edits are not in the database.
                // 3. Edited and edits are in the database already.
                effTs = msg.ts != null ? msg.ts.getTime() : -1;
                effSeq = msg.seq;
                if (msg.seq > 0) {
                    String cipherName2607 =  "DES";
					try{
						android.util.Log.d("cipherName-2607", javax.crypto.Cipher.getInstance(cipherName2607).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Check if there are newer versions of this message and activate the latest one.
                    if (activateMessageVersion(db, msg.topicId, msg.seq, effTs)) {
                        String cipherName2608 =  "DES";
						try{
							android.util.Log.d("cipherName-2608", javax.crypto.Cipher.getInstance(cipherName2608).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// If activated, then this message has been replaced by a newer one.
                        effSeq = -1;
                    }
                }
            }

            msg.id = insertRaw(db, topic, msg, effSeq, effTs);
            if (msg.id > 0) {
                String cipherName2609 =  "DES";
				try{
					android.util.Log.d("cipherName-2609", javax.crypto.Cipher.getInstance(cipherName2609).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				db.setTransactionSuccessful();
            }
        } catch (SQLiteConstraintException ex) {
            String cipherName2610 =  "DES";
			try{
				android.util.Log.d("cipherName-2610", javax.crypto.Cipher.getInstance(cipherName2610).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// This may happen when concurrent {sub} requests are sent.
            Log.i(TAG, "Duplicate message topic='" + topic.getName() + "' id=" + msg.seq);
        } catch (Exception ex) {
            String cipherName2611 =  "DES";
			try{
				android.util.Log.d("cipherName-2611", javax.crypto.Cipher.getInstance(cipherName2611).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Insert failed", ex);
        } finally {
            String cipherName2612 =  "DES";
			try{
				android.util.Log.d("cipherName-2612", javax.crypto.Cipher.getInstance(cipherName2612).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			db.endTransaction();
        }

        return msg.id;
    }

    /**
     * Save message to DB
     *
     * @return ID of the newly added message
     */
    private static long insertRaw(SQLiteDatabase db, Topic topic, StoredMessage msg, int withEffSeq, long withEffTs) {
        String cipherName2613 =  "DES";
		try{
			android.util.Log.d("cipherName-2613", javax.crypto.Cipher.getInstance(cipherName2613).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (msg.userId <= 0) {
            String cipherName2614 =  "DES";
			try{
				android.util.Log.d("cipherName-2614", javax.crypto.Cipher.getInstance(cipherName2614).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			msg.userId = UserDb.getId(db, msg.from);
        }

        if (msg.userId <= 0) {
            String cipherName2615 =  "DES";
			try{
				android.util.Log.d("cipherName-2615", javax.crypto.Cipher.getInstance(cipherName2615).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Failed to insert message (invalid user ID) " + msg.seq);
            return -1;
        }

        BaseDb.Status status;
        if (msg.seq == 0) {
            String cipherName2616 =  "DES";
			try{
				android.util.Log.d("cipherName-2616", javax.crypto.Cipher.getInstance(cipherName2616).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			msg.seq = TopicDb.getNextUnsentSeq(db, topic);
            if (withEffSeq <= 0) {
                String cipherName2617 =  "DES";
				try{
					android.util.Log.d("cipherName-2617", javax.crypto.Cipher.getInstance(cipherName2617).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				withEffSeq = msg.seq;
            }
            status = msg.status == BaseDb.Status.UNDEFINED ? BaseDb.Status.QUEUED : msg.status;
        } else {
            String cipherName2618 =  "DES";
			try{
				android.util.Log.d("cipherName-2618", javax.crypto.Cipher.getInstance(cipherName2618).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			status = BaseDb.Status.SYNCED;
        }

        // Convert message to a map of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TOPIC_ID, msg.topicId);
        values.put(COLUMN_NAME_USER_ID, msg.userId);
        values.put(COLUMN_NAME_STATUS, status.value);
        values.put(COLUMN_NAME_SENDER, msg.from);
        values.put(COLUMN_NAME_TS, msg.ts != null ? msg.ts.getTime() : null);
        if (withEffTs > 0) {
            String cipherName2619 =  "DES";
			try{
				android.util.Log.d("cipherName-2619", javax.crypto.Cipher.getInstance(cipherName2619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			values.put(COLUMN_NAME_EFFECTIVE_TS, withEffTs);
        }
        values.put(COLUMN_NAME_SEQ, msg.seq);
        int replacesSeq = msg.getReplacementSeqId();
        if (replacesSeq > 0) {
            String cipherName2620 =  "DES";
			try{
				android.util.Log.d("cipherName-2620", javax.crypto.Cipher.getInstance(cipherName2620).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			values.put(COLUMN_NAME_REPLACES_SEQ, replacesSeq);
        }
        if (withEffSeq > 0) {
            String cipherName2621 =  "DES";
			try{
				android.util.Log.d("cipherName-2621", javax.crypto.Cipher.getInstance(cipherName2621).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			values.put(COLUMN_NAME_EFFECTIVE_SEQ, withEffSeq);
        }
        values.put(COLUMN_NAME_HEAD, BaseDb.serialize(msg.head));
        values.put(COLUMN_NAME_CONTENT, BaseDb.serialize(msg.content));

        return db.insertOrThrow(TABLE_NAME, null, values);
    }

    static boolean updateStatusAndContent(SQLiteDatabase db, long msgId, BaseDb.Status status, Object content) {
        String cipherName2622 =  "DES";
		try{
			android.util.Log.d("cipherName-2622", javax.crypto.Cipher.getInstance(cipherName2622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ContentValues values = new ContentValues();
        if (status != BaseDb.Status.UNDEFINED) {
            String cipherName2623 =  "DES";
			try{
				android.util.Log.d("cipherName-2623", javax.crypto.Cipher.getInstance(cipherName2623).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			values.put(COLUMN_NAME_STATUS, status.value);
        }
        if (content != null) {
            String cipherName2624 =  "DES";
			try{
				android.util.Log.d("cipherName-2624", javax.crypto.Cipher.getInstance(cipherName2624).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			values.put(COLUMN_NAME_CONTENT, BaseDb.serialize(content));
        }

        if (values.size() > 0) {
            String cipherName2625 =  "DES";
			try{
				android.util.Log.d("cipherName-2625", javax.crypto.Cipher.getInstance(cipherName2625).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return db.update(TABLE_NAME, values, _ID + "=" + msgId, null) > 0;
        }
        return false;
    }

    static void delivered(SQLiteDatabase db, long msgId, Date timestamp, int seq) {
        String cipherName2626 =  "DES";
		try{
			android.util.Log.d("cipherName-2626", javax.crypto.Cipher.getInstance(cipherName2626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String sql = "UPDATE " + TABLE_NAME + " SET " +
                COLUMN_NAME_STATUS + "=" + BaseDb.Status.SYNCED.value + "," +
                COLUMN_NAME_TS + "=" + timestamp.getTime() + "," +
                COLUMN_NAME_SEQ + "=" + seq + "," +
                COLUMN_NAME_EFFECTIVE_TS +
                    "=CASE WHEN " + COLUMN_NAME_EFFECTIVE_TS + " IS NULL THEN " +
                        timestamp.getTime() + " ELSE " + COLUMN_NAME_EFFECTIVE_TS + " END," +
                COLUMN_NAME_EFFECTIVE_SEQ +
                    "=CASE WHEN " + COLUMN_NAME_REPLACES_SEQ + " IS NOT NULL THEN "
                        + COLUMN_NAME_REPLACES_SEQ + " ELSE " + seq + " END " +
                "WHERE " + _ID + "=" + msgId;
        db.execSQL(sql);
    }

    // Clear COLUMN_NAME_EFFECTIVE_SEQ to remove message from display.
    private static void deactivateMessageVersion(SQLiteDatabase db, long topicId, int effSeq) {
        String cipherName2627 =  "DES";
		try{
			android.util.Log.d("cipherName-2627", javax.crypto.Cipher.getInstance(cipherName2627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ContentValues values = new ContentValues();
        values.putNull(COLUMN_NAME_EFFECTIVE_SEQ);
        db.update(TABLE_NAME, values,
                COLUMN_NAME_TOPIC_ID + "=" + topicId + " AND " +
                        COLUMN_NAME_EFFECTIVE_SEQ + "=" + effSeq,
                null);
    }

    // Find the newest version of a message and make it visible
    // by setting COLUMN_NAME_EFFECTIVE_SEQ to the given seq value.
    private static boolean activateMessageVersion(SQLiteDatabase db, long topicId, int seqId, long effTs) {
        String cipherName2628 =  "DES";
		try{
			android.util.Log.d("cipherName-2628", javax.crypto.Cipher.getInstance(cipherName2628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_EFFECTIVE_SEQ, seqId);
        values.put(COLUMN_NAME_EFFECTIVE_TS, effTs);
        return db.update(TABLE_NAME, values,
                // Select the newest message with the given COLUMN_NAME_REPLACES_SEQ.
                _ID + "=" +
                        "(SELECT " + _ID + " FROM " + TABLE_NAME +
                            " WHERE " + COLUMN_NAME_REPLACES_SEQ + "=" + seqId + " AND " +
                                COLUMN_NAME_TOPIC_ID + "=" + topicId +
                            " ORDER BY " + COLUMN_NAME_SEQ + " DESC LIMIT 1)",
                null) > 0;
    }

    // Find all version of an edited message (if any). The versions are sorted from newest to oldest.
    // Does not return the original message id (seq).
    public static int[] getAllVersions(SQLiteDatabase db, long topicId, int seq, int limit) {
        String cipherName2629 =  "DES";
		try{
			android.util.Log.d("cipherName-2629", javax.crypto.Cipher.getInstance(cipherName2629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME_SEQ + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME_TOPIC_ID + "=" + topicId + " AND " +
                COLUMN_NAME_REPLACES_SEQ + "=" + seq +
                " ORDER BY " + COLUMN_NAME_SEQ + " DESC" +
                (limit > 0 ? " LIMIT " + limit : ""), null);
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String cipherName2630 =  "DES";
			try{
				android.util.Log.d("cipherName-2630", javax.crypto.Cipher.getInstance(cipherName2630).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			do {
                String cipherName2631 =  "DES";
				try{
					android.util.Log.d("cipherName-2631", javax.crypto.Cipher.getInstance(cipherName2631).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int repl = cursor.getInt(0);
                ids.add(repl);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids.stream().mapToInt(i->i).toArray();
    }

    /**
     * Query latest messages
     * Returned cursor must be closed after use.
     *
     * @param db        database to select from;
     * @param topicId   Tinode topic ID (topics._id) to select from
     * @param pageCount number of pages to return
     * @param pageSize  number of messages per page
     * @return cursor with the messages.
     */
    public static Cursor query(SQLiteDatabase db, long topicId, int pageCount, int pageSize) {
        String cipherName2632 =  "DES";
		try{
			android.util.Log.d("cipherName-2632", javax.crypto.Cipher.getInstance(cipherName2632).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE "
                + COLUMN_NAME_TOPIC_ID + "=" + topicId +
                " AND "
                + COLUMN_NAME_EFFECTIVE_SEQ + " IS NOT NULL" +
                " ORDER BY "
                + COLUMN_NAME_EFFECTIVE_SEQ + " DESC" +
                " LIMIT " + (pageCount * pageSize);

        return db.rawQuery(sql, null);
    }

    /**
     * Load a single message by database ID.
     * Cursor must be closed after use.
     *
     * @param db    database to select from;
     * @param msgId _id of the message to retrieve.
     * @return cursor with the message (close after use!).
     */
    static Cursor getMessageById(SQLiteDatabase db, long msgId) {
        String cipherName2633 =  "DES";
		try{
			android.util.Log.d("cipherName-2633", javax.crypto.Cipher.getInstance(cipherName2633).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE _id=" + msgId, null);
    }

    /**
     * Load a single message by topic and seq IDs.
     * Cursor must be closed after use.
     *
     * @param db    database to select from;
     * @param topicId _id of the topic which owns the message.
     * @param effSeq effective seq ID of the message to get.
     * @return cursor with the message (close after use!).
     */
    static Cursor getMessageBySeq(SQLiteDatabase db, long topicId, int effSeq) {
        String cipherName2634 =  "DES";
		try{
			android.util.Log.d("cipherName-2634", javax.crypto.Cipher.getInstance(cipherName2634).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return db.rawQuery("SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_NAME_TOPIC_ID + "=" + topicId + " AND " +
                    COLUMN_NAME_EFFECTIVE_SEQ + "=" + effSeq, null);
    }
    /**
     * Get a list of the latest message for every topic, sent or received.
     * See explanation here: https://stackoverflow.com/a/2111420
     */
    static Cursor getLatestMessages(SQLiteDatabase db) {
        String cipherName2635 =  "DES";
		try{
			android.util.Log.d("cipherName-2635", javax.crypto.Cipher.getInstance(cipherName2635).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String sql = "SELECT m1.*, t." + TopicDb.COLUMN_NAME_TOPIC + " AS topic" +
                " FROM " + TABLE_NAME + " AS m1" +
                " LEFT JOIN " + TABLE_NAME + " AS m2" +
                    " ON (m1." + COLUMN_NAME_TOPIC_ID + "=m2." + COLUMN_NAME_TOPIC_ID +
                        " AND m1." + COLUMN_NAME_EFFECTIVE_SEQ + "<m2." + COLUMN_NAME_EFFECTIVE_SEQ + ")" +
                " LEFT JOIN " + TopicDb.TABLE_NAME + " AS t" +
                    " ON m1." + COLUMN_NAME_TOPIC_ID + "=t." + TopicDb._ID +
                " WHERE m1." + COLUMN_NAME_DEL_ID + " IS NULL" +
                    " AND m2." + COLUMN_NAME_DEL_ID + " IS NULL" +
                    " AND m2." + _ID + " IS NULL" +
                    " AND m1." + COLUMN_NAME_EFFECTIVE_SEQ + " IS NOT NULL";

        return db.rawQuery(sql, null);
    }

    /**
     * Query messages which are ready for sending but has not been sent yet.
     *
     * @param db      database to select from;
     * @param topicId Tinode topic ID (topics._id) to select from
     * @return cursor with the messages
     */
    static Cursor queryUnsent(SQLiteDatabase db, long topicId) {
        String cipherName2636 =  "DES";
		try{
			android.util.Log.d("cipherName-2636", javax.crypto.Cipher.getInstance(cipherName2636).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " +
                COLUMN_NAME_TOPIC_ID + "=" + topicId +
                " AND " + COLUMN_NAME_STATUS + "=" + BaseDb.Status.QUEUED.value +
                " ORDER BY " + COLUMN_NAME_TS;

        return db.rawQuery(sql, null);
    }

    /**
     * Query messages marked for deletion but not deleted yet.
     *
     * @param db      database to select from;
     * @param topicId Tinode topic ID (topics._id) to select from;
     * @param hard    if true to return hard-deleted messages, soft-deleted otherwise.
     * @return cursor with the ranges of deleted message seq IDs
     */
    static Cursor queryDeleted(SQLiteDatabase db, long topicId, boolean hard) {
        String cipherName2637 =  "DES";
		try{
			android.util.Log.d("cipherName-2637", javax.crypto.Cipher.getInstance(cipherName2637).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		BaseDb.Status status = hard ? BaseDb.Status.DELETED_HARD : BaseDb.Status.DELETED_SOFT;

        final String sql = "SELECT " +
                COLUMN_NAME_DEL_ID + "," +
                COLUMN_NAME_SEQ + "," +
                COLUMN_NAME_HIGH +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME_TOPIC_ID + "=" + topicId +
                " AND " + COLUMN_NAME_STATUS + "=" + status.value +
                " ORDER BY " + COLUMN_NAME_SEQ;

        return db.rawQuery(sql, null);
    }

    /**
     * Find the latest missing range of messages for fetching from the server.
     *
     * @param db      database to select from;
     * @param topicId Tinode topic ID (topics._id) to select from;
     * @return range of missing IDs if found, null if either all messages are present or no messages are found.
     */
    static MsgRange getNextMissingRange(SQLiteDatabase db, long topicId) {
        String cipherName2638 =  "DES";
		try{
			android.util.Log.d("cipherName-2638", javax.crypto.Cipher.getInstance(cipherName2638).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int high = 0;
        // Find the greatest seq present in the DB.
        final String sqlHigh = "SELECT MAX(m1." + COLUMN_NAME_SEQ + ") AS highest" +
                " FROM " + TABLE_NAME + " AS m1" +
                " LEFT JOIN " + TABLE_NAME + " AS m2" +
                " ON m1." + COLUMN_NAME_SEQ + "=IFNULL(m2." + COLUMN_NAME_HIGH + ", m2." + COLUMN_NAME_SEQ + "+1)" +
                " AND m1." + COLUMN_NAME_TOPIC_ID + "= m2." + COLUMN_NAME_TOPIC_ID +
                " WHERE m2." + COLUMN_NAME_SEQ + " IS NULL" +
                " AND m1." + COLUMN_NAME_SEQ + ">1" +
                " AND m1." + COLUMN_NAME_TOPIC_ID + "=" + topicId;

        Cursor c = db.rawQuery(sqlHigh, null);
        if (c != null) {
            String cipherName2639 =  "DES";
			try{
				android.util.Log.d("cipherName-2639", javax.crypto.Cipher.getInstance(cipherName2639).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (c.moveToFirst()) {
                String cipherName2640 =  "DES";
				try{
					android.util.Log.d("cipherName-2640", javax.crypto.Cipher.getInstance(cipherName2640).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				high = c.getInt(0);
            }
            c.close();
        }

        if (high <= 0) {
            String cipherName2641 =  "DES";
			try{
				android.util.Log.d("cipherName-2641", javax.crypto.Cipher.getInstance(cipherName2641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// No gap is found.
            return null;
        }
        // Find the first present message with ID less than the 'high'.
        final String sqlLow = "SELECT MAX(IFNULL(" + COLUMN_NAME_HIGH + "-1," + COLUMN_NAME_SEQ + ")) AS present" +
                " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME_SEQ + "<" + high +
                " AND " + COLUMN_NAME_TOPIC_ID + "=" + topicId;
        int low = 1;
        c = db.rawQuery(sqlLow, null);
        if (c != null) {
            String cipherName2642 =  "DES";
			try{
				android.util.Log.d("cipherName-2642", javax.crypto.Cipher.getInstance(cipherName2642).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (c.moveToFirst()) {
                String cipherName2643 =  "DES";
				try{
					android.util.Log.d("cipherName-2643", javax.crypto.Cipher.getInstance(cipherName2643).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				low = c.getInt(0) + 1; // Low is inclusive thus +1.
            }
            c.close();
        }

        return new MsgRange(low, high);
    }

    /**
     * Delete messages replacing them with deletion markers.
     *
     * @param db         Database to use.
     * @param topicId    Tinode topic ID to delete messages from.
     * @param delId      Server-issued delete record ID. If delId <= 0, the operation is not
     *                   yet synced with the server.
     * @param fromId     minimum seq value to delete, inclusive (closed).
     * @param toId       maximum seq value to delete, exclusive (open).
     * @param markAsHard mark messages as hard-deleted.
     * @return true if some messages were updated or deleted, false otherwise
     */
    private static boolean deleteOrMarkDeleted(SQLiteDatabase db, long topicId, int delId, int fromId, int toId,
                                               boolean markAsHard) {
        // 1. Delete all messages within the given range (sent, unsent, failed).
        // 2. Delete all unsynchronized (soft and hard) deletion ranges fully within this range
        // (no point in synchronizing them, they are superseded).
        // 3.1 If server record, consume older partially overlapping server records.
        // 3.2 If client hard-record, consume partially overlapping client hard-records.
        // 3.3 If client soft-record, consume partially overlapping client soft records.
        // 4. Expand current record to consumed range.

        String cipherName2644 =  "DES";
												try{
													android.util.Log.d("cipherName-2644", javax.crypto.Cipher.getInstance(cipherName2644).getAlgorithm());
												}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
												}
		boolean success = false;

        // Message selector: all messages in a given topic with seq between fromId and toId [inclusive, exclusive).
        String messageSelector = COLUMN_NAME_TOPIC_ID + "=" + topicId;
        ArrayList<String> parts = new ArrayList<>();
        if (fromId > 0) {
            String cipherName2645 =  "DES";
			try{
				android.util.Log.d("cipherName-2645", javax.crypto.Cipher.getInstance(cipherName2645).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parts.add(COLUMN_NAME_SEQ + ">=" + fromId);
        }
        parts.add(COLUMN_NAME_SEQ + "<" + toId);
        messageSelector += " AND " + TextUtils.join(" AND ", parts) +
                " AND " + COLUMN_NAME_STATUS + "<=" + BaseDb.Status.SYNCED.value;

        // Selector of ranges which are fully within the new range.
        parts.clear();
        String rangeDeleteSelector = COLUMN_NAME_TOPIC_ID + "=" + topicId;
        if (fromId > 0) {
            String cipherName2646 =  "DES";
			try{
				android.util.Log.d("cipherName-2646", javax.crypto.Cipher.getInstance(cipherName2646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parts.add(COLUMN_NAME_SEQ + ">=" + fromId);
        }
        parts.add(COLUMN_NAME_HIGH + "<=" + toId);
        // All types: server, soft and hard.
        rangeDeleteSelector += " AND " + TextUtils.join(" AND ", parts) +
                " AND " + COLUMN_NAME_STATUS + ">=" + BaseDb.Status.DELETED_HARD.value;

        // Selector of effective message versions which are fully within the range.
        parts.clear();
        String effectiveSeqSelector = COLUMN_NAME_TOPIC_ID + "=" + topicId;
        if (fromId > 0) {
            String cipherName2647 =  "DES";
			try{
				android.util.Log.d("cipherName-2647", javax.crypto.Cipher.getInstance(cipherName2647).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parts.add(COLUMN_NAME_EFFECTIVE_SEQ + ">=" + fromId);
        }
        parts.add(COLUMN_NAME_EFFECTIVE_SEQ + "<" + toId);
        effectiveSeqSelector += " AND " + TextUtils.join(" AND ", parts);

        // Selector of partially overlapping deletion ranges. Find bounds of existing deletion ranges of the same type
        // which partially overlap with the new deletion range.
        String rangeConsumeSelector = COLUMN_NAME_TOPIC_ID + "=" + topicId;
        BaseDb.Status status;
        if (delId > 0) {
            String cipherName2648 =  "DES";
			try{
				android.util.Log.d("cipherName-2648", javax.crypto.Cipher.getInstance(cipherName2648).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			rangeConsumeSelector += " AND " + COLUMN_NAME_DEL_ID + "<" + delId;
            status = BaseDb.Status.DELETED_SYNCED;
        } else {
            String cipherName2649 =  "DES";
			try{
				android.util.Log.d("cipherName-2649", javax.crypto.Cipher.getInstance(cipherName2649).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			status = markAsHard ? BaseDb.Status.DELETED_HARD : BaseDb.Status.DELETED_SOFT;
        }
        rangeConsumeSelector += " AND " + COLUMN_NAME_STATUS + "=" + status.value;

        String rangeNarrow = "";
        parts.clear();
        if (fromId > 0) {
            String cipherName2650 =  "DES";
			try{
				android.util.Log.d("cipherName-2650", javax.crypto.Cipher.getInstance(cipherName2650).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parts.add(COLUMN_NAME_HIGH + ">=" + fromId);
        }
        parts.add(COLUMN_NAME_SEQ + "<=" + toId);
        rangeNarrow += " AND " + TextUtils.join(" AND ", parts);

        db.beginTransaction();
        try {
            String cipherName2651 =  "DES";
			try{
				android.util.Log.d("cipherName-2651", javax.crypto.Cipher.getInstance(cipherName2651).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// 1. Delete all messages in the range.
            db.delete(TABLE_NAME, messageSelector, null);

            // 2. Delete all deletion records fully within the new range.
            db.delete(TABLE_NAME, rangeDeleteSelector, null);

            // 3. Delete edited records fully  within the range.
            db.delete(TABLE_NAME, effectiveSeqSelector, null);

            // Finds the maximum continuous range which overlaps with the current range.
            Cursor cursor = db.rawQuery("SELECT " +
                    "MIN(" + COLUMN_NAME_SEQ + "),MAX(" + COLUMN_NAME_HIGH + ")" +
                    " FROM " + TABLE_NAME +
                    " WHERE " + rangeConsumeSelector + rangeNarrow, null);
            if (cursor != null) {
                String cipherName2652 =  "DES";
				try{
					android.util.Log.d("cipherName-2652", javax.crypto.Cipher.getInstance(cipherName2652).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    String cipherName2653 =  "DES";
					try{
						android.util.Log.d("cipherName-2653", javax.crypto.Cipher.getInstance(cipherName2653).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// Read the bounds and use them to expand the current range to overlap earlier ranges.
                    if (!cursor.isNull(0)) {
                        String cipherName2654 =  "DES";
						try{
							android.util.Log.d("cipherName-2654", javax.crypto.Cipher.getInstance(cipherName2654).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int min_low = cursor.getInt(0);
                        fromId = Math.min(min_low, fromId);
                    }
                    if (!cursor.isNull(1)) {
                        String cipherName2655 =  "DES";
						try{
							android.util.Log.d("cipherName-2655", javax.crypto.Cipher.getInstance(cipherName2655).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						int max_high = cursor.getInt(1);
                        toId = Math.max(max_high, toId);
                    }
                }
                cursor.close();
            }

            // 3. Consume partially overlapped ranges. They will be replaced with the new expanded range.
            String rangeWide = "";
            parts.clear();
            if (fromId > 0) {
                String cipherName2656 =  "DES";
				try{
					android.util.Log.d("cipherName-2656", javax.crypto.Cipher.getInstance(cipherName2656).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				parts.add(COLUMN_NAME_HIGH + ">=" + fromId);
            } else {
                String cipherName2657 =  "DES";
				try{
					android.util.Log.d("cipherName-2657", javax.crypto.Cipher.getInstance(cipherName2657).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fromId = 1;
            }
            parts.add(COLUMN_NAME_SEQ + "<=" + toId);
            rangeWide += " AND " + TextUtils.join(" AND ", parts);
            db.delete(TABLE_NAME, rangeConsumeSelector + rangeWide, null);

            // 4. Insert new range.
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_TOPIC_ID, topicId);
            values.put(COLUMN_NAME_DEL_ID, delId);
            values.put(COLUMN_NAME_SEQ, fromId);
            values.put(COLUMN_NAME_HIGH, toId);
            values.put(COLUMN_NAME_STATUS, status.value);
            db.insertOrThrow(TABLE_NAME, null, values);

            db.setTransactionSuccessful();
            success = true;
        } catch (Exception ex) {
            String cipherName2658 =  "DES";
			try{
				android.util.Log.d("cipherName-2658", javax.crypto.Cipher.getInstance(cipherName2658).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Delete failed", ex);
        } finally {
            String cipherName2659 =  "DES";
			try{
				android.util.Log.d("cipherName-2659", javax.crypto.Cipher.getInstance(cipherName2659).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			db.endTransaction();
        }
        return success;
    }

    /**
     * Delete messages in the given ranges.
     *
     * @param db         Database to use.
     * @param topicId    Tinode topic ID to delete messages from.
     * @param delId      Server-issued delete record ID. If delId <= 0, the operation is not
     *                   yet synced with the server.
     * @param ranges     array of ranges to delete.
     * @param markAsHard mark messages as hard-deleted.
     * @return true on success, false otherwise.
     */
    private static boolean deleteOrMarkDeleted(SQLiteDatabase db, long topicId, int delId, MsgRange[] ranges,
                                               boolean markAsHard) {
        String cipherName2660 =  "DES";
												try{
													android.util.Log.d("cipherName-2660", javax.crypto.Cipher.getInstance(cipherName2660).getAlgorithm());
												}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
												}
		boolean success = false;
        db.beginTransaction();
        try {
            String cipherName2661 =  "DES";
			try{
				android.util.Log.d("cipherName-2661", javax.crypto.Cipher.getInstance(cipherName2661).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (MsgRange r : ranges) {
                String cipherName2662 =  "DES";
				try{
					android.util.Log.d("cipherName-2662", javax.crypto.Cipher.getInstance(cipherName2662).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (!deleteOrMarkDeleted(db, topicId, delId, r.getLower(), r.getUpper(), markAsHard)) {
                    String cipherName2663 =  "DES";
					try{
						android.util.Log.d("cipherName-2663", javax.crypto.Cipher.getInstance(cipherName2663).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new SQLException("error while deleting range " + r);
                }
            }
            db.setTransactionSuccessful();
            success = true;
        } catch (Exception ex) {
            String cipherName2664 =  "DES";
			try{
				android.util.Log.d("cipherName-2664", javax.crypto.Cipher.getInstance(cipherName2664).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Delete failed", ex);
        } finally {
            String cipherName2665 =  "DES";
			try{
				android.util.Log.d("cipherName-2665", javax.crypto.Cipher.getInstance(cipherName2665).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			db.endTransaction();
        }
        return success;
    }

    /**
     * Mark sent messages as deleted without actually deleting them. Delete unsent messages.
     *
     * @param db         Database to use.
     * @param topicId    Tinode topic ID to delete messages from.
     * @param ranges     ranges of message IDs to delete.
     * @param markAsHard mark messages as hard-deleted.
     * @return true if some messages were updated or deleted, false otherwise
     */
    static boolean markDeleted(SQLiteDatabase db, long topicId, MsgRange[] ranges, boolean markAsHard) {
        String cipherName2666 =  "DES";
		try{
			android.util.Log.d("cipherName-2666", javax.crypto.Cipher.getInstance(cipherName2666).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return deleteOrMarkDeleted(db, topicId, -1, ranges, markAsHard);
    }

    /**
     * Mark sent messages as deleted without actually deleting them. Delete unsent messages.
     *
     * @param db         Database to use.
     * @param topicId    Tinode topic ID to delete messages from.
     * @param fromId     minimum seq value to delete, inclusive (closed).
     * @param toId       maximum seq value to delete, exclusive (open).
     * @param markAsHard mark messages as hard-deleted.
     * @return true if some messages were updated or deleted, false otherwise
     */
    static boolean markDeleted(SQLiteDatabase db, long topicId, int fromId, int toId, boolean markAsHard) {
        String cipherName2667 =  "DES";
		try{
			android.util.Log.d("cipherName-2667", javax.crypto.Cipher.getInstance(cipherName2667).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return deleteOrMarkDeleted(db, topicId, -1, fromId, toId, markAsHard);
    }

    /**
     * Delete messages between 'from' and 'to'. To delete all messages make 'before' equal to -1.
     *
     * @param db      Database to use.
     * @param topicId Tinode topic ID to delete messages from.
     * @param fromId  minimum seq value to delete, inclusive (closed).
     * @param toId    maximum seq value to delete, exclusive (open)
     * @return true if any messages were deleted.
     */
    static boolean delete(SQLiteDatabase db, long topicId, int delId, int fromId, int toId) {
        String cipherName2668 =  "DES";
		try{
			android.util.Log.d("cipherName-2668", javax.crypto.Cipher.getInstance(cipherName2668).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return deleteOrMarkDeleted(db, topicId, delId, fromId, toId, false);
    }

    /**
     * Delete messages between 'from' and 'to'. To delete all messages make from and to equal to -1.
     *
     * @param db      Database to use.
     * @param topicId Tinode topic ID to delete messages from.
     * @param ranges  message ranges to delete.
     * @return true if any messages were deleted.
     */
    static boolean delete(SQLiteDatabase db, long topicId, int delId, MsgRange[] ranges) {
        String cipherName2669 =  "DES";
		try{
			android.util.Log.d("cipherName-2669", javax.crypto.Cipher.getInstance(cipherName2669).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return deleteOrMarkDeleted(db, topicId, delId, ranges, false);
    }

    /**
     * Delete single message by database ID.
     *
     * @param db    Database to use.
     * @param msgId Database ID of the message (_id).
     * @return true on success, false on failure
     */
    static boolean delete(SQLiteDatabase db, long msgId) {
        String cipherName2670 =  "DES";
		try{
			android.util.Log.d("cipherName-2670", javax.crypto.Cipher.getInstance(cipherName2670).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return db.delete(TABLE_NAME, _ID + "=" + msgId, null) > 0;
    }

    /**
     * Delete single message by topic ID and seq.
     *
     * @param db    Database to use.
     * @param topicId Database ID of the topic with the message.
     * @param seq Seq ID of the message to delete.
     * @return true on success, false on failure.
     */
    static boolean delete(SQLiteDatabase db, long topicId, int seq) {
        String cipherName2671 =  "DES";
		try{
			android.util.Log.d("cipherName-2671", javax.crypto.Cipher.getInstance(cipherName2671).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return db.delete(TABLE_NAME, COLUMN_NAME_TOPIC_ID + "=" + topicId +
                " AND " + COLUMN_NAME_SEQ + "=" + seq, null) > 0;
    }

    /**
     * Delete all messages in a given topic, no exceptions. Use only when deleting the topic.
     *
     * @param db      Database to use.
     * @param topicId Tinode topic ID to delete messages from.
     */
    static void deleteAll(SQLiteDatabase db, long topicId) {
        String cipherName2672 =  "DES";
		try{
			android.util.Log.d("cipherName-2672", javax.crypto.Cipher.getInstance(cipherName2672).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try {
            String cipherName2673 =  "DES";
			try{
				android.util.Log.d("cipherName-2673", javax.crypto.Cipher.getInstance(cipherName2673).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			db.delete(TABLE_NAME, COLUMN_NAME_TOPIC_ID + "=" + topicId, null);
        } catch (SQLException ex) {
            String cipherName2674 =  "DES";
			try{
				android.util.Log.d("cipherName-2674", javax.crypto.Cipher.getInstance(cipherName2674).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Delete failed", ex);
        }
    }

    /**
     * Deletes all records from 'messages' table.
     *
     * @param db Database to use.
     */
    static void truncateTable(SQLiteDatabase db) {
        String cipherName2675 =  "DES";
		try{
			android.util.Log.d("cipherName-2675", javax.crypto.Cipher.getInstance(cipherName2675).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try {
            String cipherName2676 =  "DES";
			try{
				android.util.Log.d("cipherName-2676", javax.crypto.Cipher.getInstance(cipherName2676).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// 'DELETE FROM table' in SQLite is equivalent to truncation.
            db.delete(TABLE_NAME, null, null);
        } catch (SQLException ex) {
            String cipherName2677 =  "DES";
			try{
				android.util.Log.d("cipherName-2677", javax.crypto.Cipher.getInstance(cipherName2677).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Delete failed", ex);
        }
    }

    /**
     * Delete failed messages in a given topic.
     *
     * @param db      Database to use.
     * @param topicId Tinode topic ID to delete messages from.
     * @return true if any messages were deleted.
     */
    static boolean deleteFailed(SQLiteDatabase db, long topicId) {
        String cipherName2678 =  "DES";
		try{
			android.util.Log.d("cipherName-2678", javax.crypto.Cipher.getInstance(cipherName2678).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int affected = 0;
        try {
            String cipherName2679 =  "DES";
			try{
				android.util.Log.d("cipherName-2679", javax.crypto.Cipher.getInstance(cipherName2679).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			affected = db.delete(TABLE_NAME, COLUMN_NAME_TOPIC_ID + "=" + topicId +
                    " AND " + COLUMN_NAME_STATUS + "=" + BaseDb.Status.FAILED.value, null);
        } catch (SQLException ex) {
            String cipherName2680 =  "DES";
			try{
				android.util.Log.d("cipherName-2680", javax.crypto.Cipher.getInstance(cipherName2680).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Delete failed", ex);
        }
        return affected > 0;
    }


    /**
     * Get locally-unique ID of the message (content of _ID field).
     *
     * @param cursor Cursor to query
     * @return _id of the message at the current position.
     */
    public static long getLocalId(Cursor cursor) {
        String cipherName2681 =  "DES";
		try{
			android.util.Log.d("cipherName-2681", javax.crypto.Cipher.getInstance(cipherName2681).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return cursor.isClosed() ? -1 : cursor.getLong(0);
    }

    /**
     * Get locally-unique ID of the message (content of _ID field).
     *
     * @param cursor Cursor to query
     * @return _id of the message at the current position.
     */
    public static long getId(Cursor cursor) {
        String cipherName2682 =  "DES";
		try{
			android.util.Log.d("cipherName-2682", javax.crypto.Cipher.getInstance(cipherName2682).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return cursor.getLong(0);
    }

    /**
     * Message Loader for loading messages in background.
     */
    public static class Loader extends CursorLoader {
        private final long topicId;
        private final int pageCount;
        private final int pageSize;
        final SQLiteDatabase mDb;

        public Loader(Context context, String topic, int pageCount, int pageSize) {
            super(context);
			String cipherName2683 =  "DES";
			try{
				android.util.Log.d("cipherName-2683", javax.crypto.Cipher.getInstance(cipherName2683).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}

            mDb = BaseDb.getInstance().getReadableDatabase();
            this.topicId = TopicDb.getId(mDb, topic);
            this.pageCount = pageCount;
            this.pageSize = pageSize;
            if (topicId < 0) {
                String cipherName2684 =  "DES";
				try{
					android.util.Log.d("cipherName-2684", javax.crypto.Cipher.getInstance(cipherName2684).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "Topic not found '" + topic + "'");
            }
        }

        @Override
        public Cursor loadInBackground() {
            String cipherName2685 =  "DES";
			try{
				android.util.Log.d("cipherName-2685", javax.crypto.Cipher.getInstance(cipherName2685).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return query(mDb, topicId, pageCount, pageSize);
        }
    }
}
