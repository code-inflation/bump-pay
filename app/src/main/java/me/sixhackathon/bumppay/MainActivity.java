package me.sixhackathon.bumppay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import me.sixhackathon.bumppay.restlayer.BalanceManager;

public class MainActivity extends BumpActivity {

    private double purse = BalanceManager.getBalance();

    public double getPurse() {
        return purse;
    }

    public void setPurse(float purse) {
        this.purse = purse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateBalanceUI();

        // ugly hack to update balanceUI periodically
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                updateBalanceUI();
                handler.postDelayed( this, 1000 );
            }
        }, 1000 );

        // initialize p2pkit
        enableP2PKit();
        initializeBumpDetection();
    }

    private void updateBalanceUI() {
        purse = BalanceManager.getBalance();
        TextView purseContent = (TextView) findViewById(R.id.purseView);
        purseContent.setText(Double.toString(this.getPurse()));
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

    /**Called whenever the user clicks the new payement button */
    public void newPayement (View view){
        Intent intent = new Intent(this, NewPayementActivity.class);
        startActivity(intent);
    }
}
