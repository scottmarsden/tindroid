package co.tinode.tinodesdk;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * A thinly wrapped websocket connection.
 */
public class Connection extends WebSocketClient {
    private static final String TAG = "Connection";

    private static final int CONNECTION_TIMEOUT = 3000; // in milliseconds

    // Connection states
    // TODO: consider extending ReadyState
    private enum State {
        // Created. No attempts were made to reconnect.
        NEW,
        // Created, in process of creating or restoring connection.
        CONNECTING,
        // Connected.
        CONNECTED,
        // Disconnected. A thread is waiting to reconnect again.
        WAITING_TO_RECONNECT,
        // Disconnected. Not waiting to reconnect.
        CLOSED
    }

    private final WsListener mListener;

    // Connection status
    private State mStatus;

    // If connection should try to reconnect automatically.
    private boolean mAutoreconnect;

    // This connection is a background connection.
    // The value is reset when the connection is successful.
    private boolean mBackground;

    // Exponential backoff/reconnecting
    final private ExpBackoff backoff = new ExpBackoff();

    @SuppressWarnings("WeakerAccess")
    protected Connection(URI endpoint, String apikey, WsListener listener) {
        super(normalizeEndpoint(endpoint), new Draft_6455(new PerMessageDeflateExtension()),
                wrapApiKey(apikey), CONNECTION_TIMEOUT);
		String cipherName5395 =  "DES";
		try{
			android.util.Log.d("cipherName-5395", javax.crypto.Cipher.getInstance(cipherName5395).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        setReuseAddr(true);

        mListener = listener;
        mStatus = State.NEW;
        mAutoreconnect = false;
        mBackground = false;
    }

    private static Map<String,String> wrapApiKey(String apikey) {
        String cipherName5396 =  "DES";
		try{
			android.util.Log.d("cipherName-5396", javax.crypto.Cipher.getInstance(cipherName5396).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, String> headers = new HashMap<>();
        headers.put("X-Tinode-APIKey",apikey);
        return headers;
    }

    private static URI normalizeEndpoint(URI endpoint) {
        String cipherName5397 =  "DES";
		try{
			android.util.Log.d("cipherName-5397", javax.crypto.Cipher.getInstance(cipherName5397).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String path = endpoint.getPath();
        if (path.equals("")) {
            String cipherName5398 =  "DES";
			try{
				android.util.Log.d("cipherName-5398", javax.crypto.Cipher.getInstance(cipherName5398).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			path = "/";
        } else if (path.lastIndexOf("/") != path.length() - 1) {
            String cipherName5399 =  "DES";
			try{
				android.util.Log.d("cipherName-5399", javax.crypto.Cipher.getInstance(cipherName5399).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			path += "/";
        }
        path += "channels"; // ws://www.example.com:12345/v0/channels

        String scheme = endpoint.getScheme();
        // Normalize scheme to ws or wss.
        scheme = ("wss".equals(scheme) || "https".equals(scheme)) ? "wss" : "ws";

        int port = endpoint.getPort();
        if (port < 0) {
            String cipherName5400 =  "DES";
			try{
				android.util.Log.d("cipherName-5400", javax.crypto.Cipher.getInstance(cipherName5400).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			port = "wss".equals(scheme) ? 443 : 80;
        }
        try {
            String cipherName5401 =  "DES";
			try{
				android.util.Log.d("cipherName-5401", javax.crypto.Cipher.getInstance(cipherName5401).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			endpoint = new URI(scheme,
                    endpoint.getUserInfo(),
                    endpoint.getHost(),
                    port,
                    path,
                    endpoint.getQuery(),
                    endpoint.getFragment());
        } catch (URISyntaxException e) {
            String cipherName5402 =  "DES";
			try{
				android.util.Log.d("cipherName-5402", javax.crypto.Cipher.getInstance(cipherName5402).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Invalid endpoint URI", e);
        }

        return endpoint;
    }

    private void connectSocket(final boolean reconnect) {
        String cipherName5403 =  "DES";
		try{
			android.util.Log.d("cipherName-5403", javax.crypto.Cipher.getInstance(cipherName5403).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		new Thread(() -> {
            String cipherName5404 =  "DES";
			try{
				android.util.Log.d("cipherName-5404", javax.crypto.Cipher.getInstance(cipherName5404).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName5405 =  "DES";
				try{
					android.util.Log.d("cipherName-5405", javax.crypto.Cipher.getInstance(cipherName5405).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (reconnect) {
                    String cipherName5406 =  "DES";
					try{
						android.util.Log.d("cipherName-5406", javax.crypto.Cipher.getInstance(cipherName5406).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					reconnectBlocking();
                } else {
                    String cipherName5407 =  "DES";
					try{
						android.util.Log.d("cipherName-5407", javax.crypto.Cipher.getInstance(cipherName5407).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					connectBlocking(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
                }

                if ("wss".equals(uri.getScheme())) {
                    String cipherName5408 =  "DES";
					try{
						android.util.Log.d("cipherName-5408", javax.crypto.Cipher.getInstance(cipherName5408).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					// SNI: Verify server host name.
                    SSLSession sess = ((SSLSocket) getSocket()).getSession();
                    String hostName = uri.getHost();
                    if (!HttpsURLConnection.getDefaultHostnameVerifier().verify(hostName, sess)) {
                        String cipherName5409 =  "DES";
						try{
							android.util.Log.d("cipherName-5409", javax.crypto.Cipher.getInstance(cipherName5409).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						close();
                        throw new SSLHandshakeException("SNI verification failed. Expected: '" + uri.getHost() +
                                "', actual: '" + sess.getPeerPrincipal() + "'");
                    }
                }
            } catch (Exception ex) {
                String cipherName5410 =  "DES";
				try{
					android.util.Log.d("cipherName-5410", javax.crypto.Cipher.getInstance(cipherName5410).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.i(TAG, "WS connection failed", ex);
                if (mListener != null) {
                    String cipherName5411 =  "DES";
					try{
						android.util.Log.d("cipherName-5411", javax.crypto.Cipher.getInstance(cipherName5411).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mListener.onError(Connection.this, ex);
                }
            }
        }).start();
    }

    /**
     * Establish a connection with the server. It opens or reopens a websocket in a separate
     * thread.
     *
     * This is a non-blocking call.
     *
     * @param autoReconnect if connection is dropped, reconnect automatically
     */
    @SuppressWarnings("WeakerAccess")
    synchronized public void connect(boolean autoReconnect, boolean background) {
        String cipherName5412 =  "DES";
		try{
			android.util.Log.d("cipherName-5412", javax.crypto.Cipher.getInstance(cipherName5412).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		mAutoreconnect = autoReconnect;
        mBackground = background;

        switch (mStatus) {
            case CONNECTED:
            case CONNECTING:
                // Already connected or in process of connecting: do nothing.
                break;
            case WAITING_TO_RECONNECT:
                backoff.wakeUp();
                break;
            case NEW:
                mStatus = State.CONNECTING;
                connectSocket(false);
                break;
            case CLOSED:
                mStatus = State.CONNECTING;
                connectSocket(true);
                break;
            // exhaustive, no default:
        }
    }

    /**
     * Gracefully close websocket connection. The socket will attempt
     * to send a frame to the server.
     *
     * The call is idempotent: if connection is already closed it does nothing.
     */
    @SuppressWarnings("WeakerAccess")
    synchronized public void disconnect() {
        String cipherName5413 =  "DES";
		try{
			android.util.Log.d("cipherName-5413", javax.crypto.Cipher.getInstance(cipherName5413).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean wakeUp = mAutoreconnect;
        mAutoreconnect = false;

        // Actually close the socket (non-blocking).
        close();

        if (wakeUp) {
            String cipherName5414 =  "DES";
			try{
				android.util.Log.d("cipherName-5414", javax.crypto.Cipher.getInstance(cipherName5414).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Make sure we are not waiting to reconnect
            backoff.wakeUp();
        }
    }

    /**
     * Check if the socket is OPEN.
     *
     * @return true if the socket is OPEN, false otherwise;
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isConnected() {
        String cipherName5415 =  "DES";
		try{
			android.util.Log.d("cipherName-5415", javax.crypto.Cipher.getInstance(cipherName5415).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isOpen();
    }

    /**
     * Check if the socket is waiting to reconnect.
     *
     * @return true if the socket is OPEN, false otherwise;
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isWaitingToReconnect() {
        String cipherName5416 =  "DES";
		try{
			android.util.Log.d("cipherName-5416", javax.crypto.Cipher.getInstance(cipherName5416).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return mStatus == State.WAITING_TO_RECONNECT;
    }
    /**
     * Reset exponential backoff counter to zero.
     * If autoreconnect is true and WsListener is provided, then WsListener.onConnect must call
     * this method.
     */
    @SuppressWarnings("WeakerAccess")
    public void backoffReset() {
        String cipherName5417 =  "DES";
		try{
			android.util.Log.d("cipherName-5417", javax.crypto.Cipher.getInstance(cipherName5417).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		backoff.reset();
    }

    @Override
    public void onOpen(ServerHandshake handshakeData) {
        String cipherName5418 =  "DES";
		try{
			android.util.Log.d("cipherName-5418", javax.crypto.Cipher.getInstance(cipherName5418).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		synchronized (this) {
            String cipherName5419 =  "DES";
			try{
				android.util.Log.d("cipherName-5419", javax.crypto.Cipher.getInstance(cipherName5419).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mStatus = State.CONNECTED;
        }

        if (mListener != null) {
            String cipherName5420 =  "DES";
			try{
				android.util.Log.d("cipherName-5420", javax.crypto.Cipher.getInstance(cipherName5420).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean bkg = mBackground;
            mBackground = false;
            mListener.onConnect(this, bkg);
        } else {
            String cipherName5421 =  "DES";
			try{
				android.util.Log.d("cipherName-5421", javax.crypto.Cipher.getInstance(cipherName5421).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			backoff.reset();
        }
    }

    @Override
    public void onMessage(String message) {
        String cipherName5422 =  "DES";
		try{
			android.util.Log.d("cipherName-5422", javax.crypto.Cipher.getInstance(cipherName5422).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (mListener != null) {
            String cipherName5423 =  "DES";
			try{
				android.util.Log.d("cipherName-5423", javax.crypto.Cipher.getInstance(cipherName5423).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mListener.onMessage(this, message);
        }
    }

    @Override
    public void onMessage(ByteBuffer blob) {
        String cipherName5424 =  "DES";
		try{
			android.util.Log.d("cipherName-5424", javax.crypto.Cipher.getInstance(cipherName5424).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// do nothing, server does not send binary frames
        Log.w(TAG, "binary message received (should not happen)");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        String cipherName5425 =  "DES";
		try{
			android.util.Log.d("cipherName-5425", javax.crypto.Cipher.getInstance(cipherName5425).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Avoid infinite recursion
        synchronized (this) {
            String cipherName5426 =  "DES";
			try{
				android.util.Log.d("cipherName-5426", javax.crypto.Cipher.getInstance(cipherName5426).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (mStatus == State.WAITING_TO_RECONNECT) {
                String cipherName5427 =  "DES";
				try{
					android.util.Log.d("cipherName-5427", javax.crypto.Cipher.getInstance(cipherName5427).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            } else if (mAutoreconnect) {
                String cipherName5428 =  "DES";
				try{
					android.util.Log.d("cipherName-5428", javax.crypto.Cipher.getInstance(cipherName5428).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mStatus = State.WAITING_TO_RECONNECT;
            } else {
                String cipherName5429 =  "DES";
				try{
					android.util.Log.d("cipherName-5429", javax.crypto.Cipher.getInstance(cipherName5429).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mStatus = State.CLOSED;
            }
        }

        if (mListener != null) {
            String cipherName5430 =  "DES";
			try{
				android.util.Log.d("cipherName-5430", javax.crypto.Cipher.getInstance(cipherName5430).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mListener.onDisconnect(this, remote, code, reason);
        }

        if (mAutoreconnect) {
            String cipherName5431 =  "DES";
			try{
				android.util.Log.d("cipherName-5431", javax.crypto.Cipher.getInstance(cipherName5431).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			new Thread(() -> {
                String cipherName5432 =  "DES";
				try{
					android.util.Log.d("cipherName-5432", javax.crypto.Cipher.getInstance(cipherName5432).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				while (mStatus == State.WAITING_TO_RECONNECT) {
                    String cipherName5433 =  "DES";
					try{
						android.util.Log.d("cipherName-5433", javax.crypto.Cipher.getInstance(cipherName5433).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					backoff.doSleep();

                    synchronized (Connection.this) {
                        String cipherName5434 =  "DES";
						try{
							android.util.Log.d("cipherName-5434", javax.crypto.Cipher.getInstance(cipherName5434).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						// Check if we no longer need to connect.
                        if (mStatus != State.WAITING_TO_RECONNECT) {
                            String cipherName5435 =  "DES";
							try{
								android.util.Log.d("cipherName-5435", javax.crypto.Cipher.getInstance(cipherName5435).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							break;
                        }
                        mStatus = State.CONNECTING;
                    }
                    connectSocket(true);
                }
            }).start();
        }
    }

    @Override
    public void onError(Exception ex) {
        String cipherName5436 =  "DES";
		try{
			android.util.Log.d("cipherName-5436", javax.crypto.Cipher.getInstance(cipherName5436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.w(TAG, "Websocket error", ex);

        if (mListener != null) {
            String cipherName5437 =  "DES";
			try{
				android.util.Log.d("cipherName-5437", javax.crypto.Cipher.getInstance(cipherName5437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			mListener.onError(this, ex);
        }
    }

    interface WsListener {
        default void onConnect(Connection conn, boolean background) {
        }

        default void onMessage(Connection conn, String message) {
        }

        default void onDisconnect(Connection conn, boolean byServer, int code, String reason) {
        }

        default void onError(Connection conn, Exception err) {
        }
    }
}
