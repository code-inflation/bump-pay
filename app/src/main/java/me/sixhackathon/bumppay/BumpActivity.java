package me.sixhackathon.bumppay;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ch.uepaa.p2pkit.P2PKitClient;
import ch.uepaa.p2pkit.P2PKitStatusCallback;
import ch.uepaa.p2pkit.StatusResult;
import ch.uepaa.p2pkit.StatusResultHandling;
import ch.uepaa.p2pkit.discovery.InfoTooLongException;
import ch.uepaa.p2pkit.discovery.P2PListener;
import ch.uepaa.p2pkit.discovery.entity.Peer;
import ch.uepaa.p2pkit.internal.messaging.MessageTooLargeException;
import ch.uepaa.p2pkit.messaging.MessageListener;
import me.sixhackathon.bumppay.util.AsyncMessageRunner;
import me.sixhackathon.bumppay.util.BumpDetector;
import me.sixhackathon.bumppay.util.OnBumpListener;

public abstract class BumpActivity extends AppCompatActivity {

    public static final String APP_KEY = "eyJzaWduYXR1cmUiOiJpNSs1MHNZT1dTNVhlcFlOck9LNnJhNS9raGVneDVnMVZRK3owb2J4akRWUXMzL295T3ExWVJwa21VL0JDZWNnY21KM1JSbTlRU1hUY1hzMWVUdVJnbGp4blNLRGdocVMzd1pzWkQwVVIxbUZkbEJONjdiSFZFcHM5b3lkV2FRWXVkOTNxcmg4ZUpTdHI2SFJ1Rnk3TStMeHhlQTVlN0I4dE5jRkNzRlhnQjQ9IiwiYXBwSWQiOjE0NTksInZhbGlkVW50aWwiOjE2OTUxLCJhcHBVVVVJRCI6IkY2OEY0OEE0LUY5MDEtNDRERi05NDk5LTlCRjI0MEMzMkI5NiJ9";
    protected SensorManager sensorManager;
    protected BumpDetector bumpDetector;
    protected Sensor accelerometer;

    protected boolean accepting = false;

    protected Set<Peer> peers = new HashSet<>();

    protected final P2PListener mP2PDiscoveryListener = new P2PListener() {

        @Override
        public void onP2PStateChanged(final int state) {}

        @Override
        public void onPeerDiscovered(final Peer peer) {
            Log.i(MainActivity.class.toString(), "Peer found: " + peer.getDiscoveryInfo());
            if (peer.getDiscoveryInfo() != null) {
                peers.add(peer);
            }
        }

        @Override
        public void onPeerLost(final Peer peer) {
            peers.remove(peer);
        }

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {}
    };

    protected final P2PKitStatusCallback mStatusCallback = new P2PKitStatusCallback() {
        @Override
        public void onEnabled() {
            Log.i(MainActivity.class.toString(), "p2pkit enabled!");

            P2PKitClient.getInstance(getApplicationContext()).getDiscoveryServices().addP2pListener(mP2PDiscoveryListener);

            try {
                P2PKitClient.getInstance(getApplicationContext()).getDiscoveryServices().setP2pDiscoveryInfo("Hello p2pkit".getBytes());
            } catch (InfoTooLongException e) {
                Log.i(MainActivity.class.toString(), "P2PListener | The discovery info is too long");
            }
        }

        @Override
        public void onSuspended() {}

        @Override
        public void onError(StatusResult result) {}
    };

    protected final MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onMessageStateChanged(final int state) {
            Log.i(MainActivity.class.toString(), "MessageListener | State changed: " + state);
        }

        @Override
        public void onMessageReceived(final long timestamp, final UUID origin, final String type, final byte[] message) {
            Log.i(MainActivity.class.toString(), "MessageListener | Message received: From=" + origin + " type=" + type + " message=" + new String(message));
            if (accepting){
                Log.i(MainActivity.class.toString(), "Accepting payment from " + origin);

                // TODO handle payment acceptment

                accepting = false;
            }
        }
    };


    protected void initializeBumpDetection() {
        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bumpDetector = new BumpDetector(new OnBumpListener() {
            @Override
            public void onBump() {
                messagePeers();
                //Log.i(MainActivity.class.toString(), "Bumped with Peer: " + bumpedPeer.getNodeId());
            }
        });
    }

    protected Peer messagePeers(){

        accepting = true;

        for (Peer peer : peers) {
            // TODO send message to all connected peers
            try {
                boolean forwarded = P2PKitClient.getInstance(getApplicationContext()).getMessageServices().sendMessage(peer.getNodeId(), "text/plain", "Hello!".getBytes());
                if (forwarded){
                    Log.i(MainActivity.class.toString(), "Message sent to " + peer.getNodeId());
                }
            } catch (MessageTooLargeException e) {
                e.printStackTrace();
            }
            new AsyncMessageRunner(peer, getApplicationContext()).execute();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                accepting = false;
            }
        }, 10000);
        return null;
    }

    protected void enableP2PKit() {
        final StatusResult result = P2PKitClient.isP2PServicesAvailable(this);

        if (result.getStatusCode() == StatusResult.SUCCESS) {
            P2PKitClient client = P2PKitClient.getInstance(this);
            client.enableP2PKit(mStatusCallback, APP_KEY);
        } else {
            StatusResultHandling.showAlertDialogForStatusError(this, result);
        }

        P2PKitClient.getInstance(this).getMessageServices().addMessageListener(mMessageListener);
    }
}
