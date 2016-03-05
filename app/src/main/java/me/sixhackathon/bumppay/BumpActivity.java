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
import me.sixhackathon.bumppay.restlayer.UserManager;
import me.sixhackathon.bumppay.util.AsyncMessageRunner;
import me.sixhackathon.bumppay.util.BumpDetector;
import me.sixhackathon.bumppay.util.OnBumpListener;

public abstract class BumpActivity extends AppCompatActivity {

    public static final String APP_KEY = "eyJzaWduYXR1cmUiOiJpNSs1MHNZT1dTNVhlcFlOck9LNnJhNS9raGVneDVnMVZRK3owb2J4akRWUXMzL295T3ExWVJwa21VL0JDZWNnY21KM1JSbTlRU1hUY1hzMWVUdVJnbGp4blNLRGdocVMzd1pzWkQwVVIxbUZkbEJONjdiSFZFcHM5b3lkV2FRWXVkOTNxcmg4ZUpTdHI2SFJ1Rnk3TStMeHhlQTVlN0I4dE5jRkNzRlhnQjQ9IiwiYXBwSWQiOjE0NTksInZhbGlkVW50aWwiOjE2OTUxLCJhcHBVVVVJRCI6IkY2OEY0OEE0LUY5MDEtNDRERi05NDk5LTlCRjI0MEMzMkI5NiJ9";
    protected SensorManager sensorManager;
    protected BumpDetector bumpDetector;
    protected Sensor accelerometer;

    protected boolean accepting = false;

    protected int bumpAmount;
    protected int amountToPay;
    protected String numberOfReciever;

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
                P2PKitClient.getInstance(getApplicationContext()).getDiscoveryServices().setP2pDiscoveryInfo(UserManager.getUserPhoneNumber().getBytes());
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

                amountToPay += bumpAmount;

                String strMessage = new String(message);
                if (!strMessage.substring(5).equals("")){
                    numberOfReciever = strMessage.substring(5);
                    Log.i(BumpActivity.class.toString(), "Number of the reviever is: " + numberOfReciever);
                }

                // update amount to pay
                updateAmountToPay();
                accepting = false;
            }
        }
    };

    protected void updateAmountToPay() {
    }

    protected void initializeBumpDetection() {
        initSensors();
        bumpDetector = new BumpDetector(new OnBumpListener() {
            @Override
            public void onBump() {
                messagePeers(false);
                //Log.i(MainActivity.class.toString(), "Bumped with Peer: " + bumpedPeer.getNodeId());
            }
        });
    }

    protected void initializeBumpDetection(int bumpAmount) {
        this.bumpAmount = bumpAmount;
        initSensors();
        bumpDetector = new BumpDetector(new OnBumpListener() {
            @Override
            public void onBump() {
                messagePeers(true);
                //Log.i(MainActivity.class.toString(), "Bumped with Peer: " + bumpedPeer.getNodeId());
            }
        });
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void messagePeers(boolean asPayer){

        accepting = true;

        String message = "[BMP]";
        if (!asPayer){
            message += UserManager.getUserPhoneNumber();
        }

        for (Peer peer : peers) {
            // send message to all connected peers
            try {
                boolean forwarded = P2PKitClient.getInstance(getApplicationContext()).getMessageServices().sendMessage(peer.getNodeId(), "text/plain", message.getBytes());
                if (forwarded){
                    Log.i(MainActivity.class.toString(), "Message:" + message + " sent to " + peer.getNodeId());
                }
            } catch (MessageTooLargeException e) {
                e.printStackTrace();
            }
            // new AsyncMessageRunner(peer, getApplicationContext()).execute();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                accepting = false;
            }
        }, 10000);
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

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(bumpDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(bumpDetector);
        super.onPause();
    }
}
