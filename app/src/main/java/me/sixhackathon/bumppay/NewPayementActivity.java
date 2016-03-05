package me.sixhackathon.bumppay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class NewPayementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payement);

        Spinner dropdown = (Spinner)findViewById(R.id.b);
        String[] items = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    /** Called when the user clicks the cancel button */
    public void cancelGoHome(View view) {
        //Maybe cleanup missing
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
