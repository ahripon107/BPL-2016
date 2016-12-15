package com.tigersapp.bdcricket;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.tigersapp.bdcricket.util.SharedPrefData;

import io.fabric.sdk.android.Fabric;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.muc.DefaultParticipantStatusListener;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;


/**
 * @author Ripon
 */
public class ParseApplication extends Application {

    public static final String DEFAULT_HOST = "45.55.30.137";
    public static final String HOST = "45.55.30.137";
    public static final int PORT = 5222;
    public static final String SERVICE = "vpn.rocks";

    private MultiUserChat mMultiUserChat;
    private XMPPTCPConnection xmpptcpConnection;

    private String deviceId;

    private SmackListener smackListener;

    public static final String TAG = ParseApplication.class
            .getSimpleName();

    private static ParseApplication mInstance;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Batch.Push.setGCMSenderId("115406524067");

        // TODO : switch to live Batch Api Key before shipping
        Batch.setConfig(new Config("DEV57E37B3B16984AF3E02BFED16D2")); // devloppement
        //Batch.setConfig(new Config("57E37B3B165FCB2CDC07E64724BB5C")); // live

        mInstance = this;

        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static synchronized ParseApplication getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }


    public void setSmackListener(SmackListener smackListener) {
        this.smackListener = smackListener;
    }

    public boolean join() {
        try {
            initConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void initConnection() {
        if(xmpptcpConnection == null || !xmpptcpConnection.isConnected()) {
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            config.setCompressionEnabled(false);
            config.setDebuggerEnabled(true);
            config.setServiceName(SERVICE);
            config.setHost(HOST);
            config.setPort(PORT);

            xmpptcpConnection = new XMPPTCPConnection(config.build());
            xmpptcpConnection.addConnectionListener(new ConnectionListener() {
                @Override
                public void connected(XMPPConnection connection) {

                    final AccountManager accountManager = AccountManager.getInstance(xmpptcpConnection);
                    accountManager.sensitiveOperationOverInsecureConnection(true);

                    try {
                        accountManager.createAccount(deviceId, deviceId);

                        xmpptcpConnection.login(deviceId, deviceId);

                        Presence presence = new Presence(Presence.Type.available);
                        connection.sendPacket(presence);
                    } catch (Exception ex) {
                        ex.printStackTrace();

                        try {
                            xmpptcpConnection.login(deviceId, deviceId);

                            Presence presence = new Presence(Presence.Type.available);
                            connection.sendPacket(presence);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void authenticated(XMPPConnection connection, boolean resumed) {

                    AndFilter messageFilter = new AndFilter(new FromMatchesFilter("livestreambangla@conference."+SERVICE,true)
                            , MessageTypeFilter.GROUPCHAT);

                    messageFilter = new AndFilter(messageFilter, new StanzaFilter() {
                        @Override
                        public boolean accept(Stanza stanza) {
                            Message msg = (Message) stanza;

                            return msg.getBody() != null;
                        }

                    });

                    xmpptcpConnection.addPacketListener(new StanzaListener() {
                        @Override
                        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                            try {
                                final Message message = (Message) packet;

                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(smackListener != null) smackListener.onReceiveMessage(message);
                                    }
                                });
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, messageFilter);

                    setmMultiUserChat();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (smackListener != null) {
                                smackListener.onLoginSuccess();
                            }
                        }
                    });
                }

                @Override
                public void connectionClosed() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(smackListener != null) {
                                smackListener.onLoginFailed();
                            }
                        }
                    });
                }

                @Override
                public void connectionClosedOnError(Exception e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(smackListener != null) {
                                smackListener.onLoginFailed();
                            }
                        }
                    });
                }

                @Override
                public void reconnectionSuccessful() {
                }

                @Override
                public void reconnectingIn(int seconds) {
                }

                @Override
                public void reconnectionFailed(Exception e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(smackListener != null) {
                                smackListener.onLoginFailed();
                            }
                        }
                    });
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        xmpptcpConnection.connect();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void setmMultiUserChat() {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(xmpptcpConnection);
        mMultiUserChat = manager.getMultiUserChat("livestreambangla@conference." + SERVICE);
        mMultiUserChat.addParticipantStatusListener(new DefaultParticipantStatusListener() {
            @Override
            public void joined(String participant) {
                super.joined(participant);

                Log.d("XMPP", participant + " joined");
            }

            @Override
            public void left(String participant) {
                super.left(participant);

                Log.d("XMPP", participant + " left");
            }
        });

        if (!mMultiUserChat.isJoined()) {
            boolean createNow = false;

            try {
                DiscussionHistory history = new DiscussionHistory();
                history.setMaxStanzas(0);
                mMultiUserChat.createOrJoin(deviceId, deviceId, history, xmpptcpConnection.getPacketReplyTimeout());
                mMultiUserChat.changeNickname(SharedPrefData.getNickName(getApplicationContext()));

                createNow = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (createNow) {
                try {
                    mMultiUserChat.sendConfigurationForm(new Form(DataForm.Type.submit));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void leftMulitUserChat() throws SmackException.NotConnectedException{
        mMultiUserChat.leave();
    }

    public void setNewNickName(String newNickName) {
        try {
            mMultiUserChat.changeNickname(newNickName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MultiUserChat getmMultiUserChat() {
        return mMultiUserChat;
    }

}
