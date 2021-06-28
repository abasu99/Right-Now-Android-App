package com.example.dbtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static androidx.core.content.ContextCompat.getSystemService;


public class homeActivity extends AppCompatActivity implements LocationListener {
    TextView viewEmergencyNumber;
    EditText emergencyNumber;
    Button savebtn,sosbtn;
    private LocationManager locationManager;
    float lat=0.0f;
    float lng=0.0f;
    public Criteria criteria;
    public String bestProvider;
    String num="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final dbHelper db= new dbHelper(this);
        num=db.getEmerNum();
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            Toast.makeText(homeActivity.this, "Please switch on your location", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.home);
        viewEmergencyNumber = (TextView) findViewById(R.id.viewEmergencyNumber);
        emergencyNumber = (EditText) findViewById(R.id.emergencyNumber);
        savebtn=(Button)findViewById(R.id.savebtn);
        sosbtn=(Button)findViewById(R.id.sosbtn);

        if(num!=null){
            viewEmergencyNumber.setText(num);
        }


        if (ActivityCompat.checkSelfPermission(homeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(homeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(homeActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(homeActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.SEND_SMS}, 1);

        }

        locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);//Instance of LocationManger
        criteria = new Criteria();// Instance of criteria
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();// Best Provider of the service you are requesting

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            lat = (float) location.getLatitude();
            lng = (float) location.getLongitude();

        }
        else{
            //This is what you need:Get fresh LAt LONG
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        }

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number=emergencyNumber.getText().toString();
                db.insertContactData(number);
                num=db.getEmerNum();
                viewEmergencyNumber.setText(num);
                emergencyNumber.setText("");
                Toast.makeText(getApplicationContext(),"Number is saved !",Toast.LENGTH_LONG).show();
            }
        });

        final String msg = "I am in emergency. Please come to this location : google.com/maps/?q="+lat+","+lng;
        sosbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(num,null,msg,null,null);
                Toast.makeText(getApplicationContext(),"Message sent to "+num,Toast.LENGTH_LONG).show();
                emergencyNumber.setVisibility(View.GONE);
                savebtn.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public void onLocationChanged(Location location) {
        lat = (float) (location.getLatitude());
        lng = (float) (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}


