package me.sixhackathon.bumppay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import ch.uepaa.p2pkit.P2PKitClient;
import ch.uepaa.p2pkit.discovery.InfoTooLongException;
import me.sixhackathon.bumppay.restlayer.BalanceManager;
import me.sixhackathon.bumppay.restlayer.UserManager;

public class MainActivity extends BumpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!UserManager.isLoggedIn()){
            showPhonenrDialog();
        }

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

    private void showPhonenrDialog() {
        final EditText txtPhoneNumber = new EditText(this);

        txtPhoneNumber.setText("+41796550243");

        new AlertDialog.Builder(this)
                .setTitle("Phone Number")
                .setMessage("Enter your phone number")
                .setView(txtPhoneNumber)
                .setPositiveButton("Paymit login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String phoneNumber = txtPhoneNumber.getText().toString();
                        UserManager.signIn(phoneNumber);
                        resetP2PDiscoveryInfo();
                    }
                })
                .show();
    }

    private void resetP2PDiscoveryInfo(){
        try {
            P2PKitClient.getInstance(getApplicationContext()).getDiscoveryServices().setP2pDiscoveryInfo(UserManager.getUserPhoneNumber().getBytes());
        } catch (InfoTooLongException e) {
            Log.i(MainActivity.class.toString(), "P2PListener | The discovery info is too long");
        }
    }

    private void updateBalanceUI() {
        TextView purseContent = (TextView) findViewById(R.id.purseView);

        if(UserManager.isLoggedIn()){
            BalanceManager.loadBalance();
            if (!purseContent.getText().toString().equals("")){
                int purseInt = (int)Double.parseDouble(purseContent.getText().toString());
                if (purseInt != 0 && BalanceManager.getBalance() > purseInt){

                    int delta = BalanceManager.getBalance() - purseInt;

                    CharSequence text = "You just received "+ delta + " CHF";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                }
            }
        }

        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH));
        purseContent.setText(df.format(BalanceManager.getBalance()));
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
