package co.com.autolagos.rtaxi_pasajero;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
         ImageView rtaxilogo = (ImageView) findViewById(R.id.logortaxi);




         rtaxilogo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent myIntent = new Intent(Splash.this, mapActivity.class);
                 myIntent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                 Splash.this.startActivity(myIntent);
             }
         });



    }
}
