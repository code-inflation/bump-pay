package me.sixhackathon.bumppay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ch.uepaa.p2pkit.P2PKitClient;
import ch.uepaa.p2pkit.P2PKitStatusCallback;
import ch.uepaa.p2pkit.StatusResult;
import ch.uepaa.p2pkit.StatusResultHandling;
import ch.uepaa.p2pkit.discovery.InfoTooLongException;
import ch.uepaa.p2pkit.discovery.P2PListener;
import ch.uepaa.p2pkit.discovery.entity.Peer;

public class MainActivity extends AppCompatActivity {

    private final P2PListener mP2PDiscoveryListener = new P2PListener() {
        @Override
        public void onP2PStateChanged(final int state) {}

        @Override
        public void onPeerDiscovered(final Peer peer) {
            Log.i(MainActivity.class.toString(), "Peer found: " + peer.getDiscoveryInfo());
        }

        @Override
        public void onPeerLost(final Peer peer) {}

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {}
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
        final StatusResult result = P2PKitClient.isP2PServicesAvailable(this);
        P2PKitStatusCallback mStatusCallback = new P2PKitStatusCallback() {
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

        if (result.getStatusCode() == StatusResult.SUCCESS) {
            P2PKitClient client = P2PKitClient.getInstance(this);
            client.enableP2PKit(mStatusCallback, "eyJzaWduYXR1cmUiOiJpNSs1MHNZT1dTNVhlcFlOck9LNnJhNS9raGVneDVnMVZRK3owb2J4akRWUXMzL295T3ExWVJwa21VL0JDZWNnY21KM1JSbTlRU1hUY1hzMWVUdVJnbGp4blNLRGdocVMzd1pzWkQwVVIxbUZkbEJONjdiSFZFcHM5b3lkV2FRWXVkOTNxcmg4ZUpTdHI2SFJ1Rnk3TStMeHhlQTVlN0I4dE5jRkNzRlhnQjQ9IiwiYXBwSWQiOjE0NTksInZhbGlkVW50aWwiOjE2OTUxLCJhcHBVVVVJRCI6IkY2OEY0OEE0LUY5MDEtNDRERi05NDk5LTlCRjI0MEMzMkI5NiJ9");
        } else {
            StatusResultHandling.showAlertDialogForStatusError(this, result);
        }
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
