package me.sixhackathon.bumppay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import me.sixhackathon.bumppay.restlayer.PaymentManager;

public class NewPayementActivity extends BumpActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payement);
        TextView textViewToPayLabel = (TextView)findViewById(R.id.textViewToPayLabel);
        textViewToPayLabel.setVisibility(View.GONE);

        Spinner dropdown = (Spinner)findViewById(R.id.b);

        String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        // initialize p2pkit and bump detection
        enableP2PKit();
        initializeBumpDetection(Integer.parseInt(dropdown.getSelectedItem().toString()));

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner dropdown = (Spinner)findViewById(R.id.b);
                bumpAmount = Integer.parseInt(dropdown.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bumpAmount = 2;
            }
        });

        Button doneButton = (Button)findViewById(R.id.buttonDone);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle payment
                PaymentManager.pay(numberOfReceiver, amountToPay);
                Log.i(NewPayementActivity.class.toString(), "Paying " + amountToPay + " CHF to " + numberOfReceiver);

                Context context = getApplicationContext();
                CharSequence text = "You just successfully paid"+ amountToPay + " CHF";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                // go back to main activity
                finish();
            }
        });
    }

    @Override
    protected void updateAmountToPay(){
        TextView textViewToPayLabel = (TextView)findViewById(R.id.textViewToPayLabel);
        textViewToPayLabel.setVisibility(View.VISIBLE);

        TextView txt = (TextView)findViewById(R.id.textViewToPay);
        txt.setText(amountToPay + " CHF");
    }

    /** Called when the user clicks the cancel button */
    public void cancelGoHome(View view) {
        //Maybe cleanup missing
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
