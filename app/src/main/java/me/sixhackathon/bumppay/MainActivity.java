package me.sixhackathon.bumppay;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    public static final String APP_KEY = "eyJzaWduYXR1cmUiOiJpNSs1MHNZT1dTNVhlcFlOck9LNnJhNS9raGVneDVnMVZRK3owb2J4akRWUXMzL295T3ExWVJwa21VL0JDZWNnY21KM1JSbTlRU1hUY1hzMWVUdVJnbGp4blNLRGdocVMzd1pzWkQwVVIxbUZkbEJONjdiSFZFcHM5b3lkV2FRWXVkOTNxcmg4ZUpTdHI2SFJ1Rnk3TStMeHhlQTVlN0I4dE5jRkNzRlhnQjQ9IiwiYXBwSWQiOjE0NTksInZhbGlkVW50aWwiOjE2OTUxLCJhcHBVVVVJRCI6IkY2OEY0OEE0LUY5MDEtNDRERi05NDk5LTlCRjI0MEMzMkI5NiJ9";
    private SensorManager sensorManager;
    private BumpDetector bumpDetector;
    private Sensor accelerometer;

    private Set<Peer> peers = new HashSet<>();

    private final P2PListener mP2PDiscoveryListener = new P2PListener() {

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

    private final P2PKitStatusCallback mStatusCallback = new P2PKitStatusCallback() {
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

    private final MessageListener mMessageListener = new MessageListener() {

        @Override
        public void onMessageStateChanged(final int state) {
            Log.i(MainActivity.class.toString(), "MessageListener | State changed: " + state);
        }

        @Override
        public void onMessageReceived(final long timestamp, final UUID origin, final String type, final byte[] message) {
            Log.i(MainActivity.class.toString(), "MessageListener | Message received: From=" + origin + " type=" + type + " message=" + new String(message));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // initialize p2pkit
        enableP2PKit();

        initializeBumpDetection();
    }

    private void initializeBumpDetection() {
        sensorManager = (SensorManager) getSystemService(getApplicationContext().SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        bumpDetector = new BumpDetector(new OnBumpListener() {
            @Override
            public void onBump() {
                Peer bumpedPeer = findResponsiblePeer();
                //Log.i(MainActivity.class.toString(), "Bumped with Peer: " + bumpedPeer.getNodeId());
            }
        });
    }

    private Peer findResponsiblePeer(){
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
        }

        // TODO wait for message of other peer

        // TODO return the bumped peer
        return null;
    }

    private void enableP2PKit() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
