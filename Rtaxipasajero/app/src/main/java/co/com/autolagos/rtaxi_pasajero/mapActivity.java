package co.com.autolagos.rtaxi_pasajero;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.style.LocaleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class mapActivity extends FragmentActivity
        implements OnMapReadyCallback, TextToSpeech.OnInitListener {

    GoogleMap googlemaps;
    static FusedLocationProviderClient mFusedLocationClient;
    static double lat, lng;
    static int vuelta=0;
    static ImageView buttonRtaxi;
    public TextToSpeech mTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        buttonRtaxi = (ImageView) findViewById(R.id.buttonRtaxi);

        buttonRtaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent myIntent = new Intent(mapActivity.this, Splash.class);
                myIntent.setFlags(FLAG_ACTIVITY_REORDER_TO_FRONT);
                mapActivity.this.startActivity(myIntent);
            }
        });





        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mapActivity.this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         mTTS = new TextToSpeech(this, this);




    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {



                            lat = location.getLatitude();
                            lng = location.getLongitude();


                            int height = 80;
                            int width = 80;
                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.puntero_1);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


                            final LatLng MELBOURNE = new LatLng(lat, lng);
                            Marker melbourne = googleMap.addMarker(new MarkerOptions()
                                    .position(MELBOURNE));




                            melbourne.setIcon((BitmapDescriptorFactory.fromBitmap(smallMarker)));
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            googleMap.animateCamera(cameraUpdate);
                            CameraPosition cameraPosition = new CameraPosition.Builder().
                                    target(MELBOURNE).
                                    tilt(90).
                                    zoom(17).
                                    bearing(0).
                                    build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            Calendar c = Calendar.getInstance();
                            int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

                            if ((timeOfDay >= 6) && (timeOfDay <= 17)) {
                                MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.my_map_style);
                                googleMap.setMapStyle(style);
                            } else {
                                MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.my_map_day);
                                googleMap.setMapStyle(style);
                            }
                            if (location == null) {
                                Toast.makeText(getApplicationContext(), "no location", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {

            Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_LONG).show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_GRANTED) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PERMISSION_GRANTED) {

                googlemaps.setMyLocationEnabled(true);

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {

       if(vuelta<=0) {
           Toast.makeText(getApplicationContext(), "Seleccione su destino.", Toast.LENGTH_LONG).show();

           Locale locSpanish = new Locale("spa", "MEX");
           mTTS.setLanguage(locSpanish);
           mTTS.speak("seleccione su destino", TextToSpeech.QUEUE_ADD, null, null);

           vuelta++;
       }


    }
}




