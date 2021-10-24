package br.unifacs.AVN1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botaoConfig = (Button) findViewById(R.id.buttonConfig);
        Button botaoSair = (Button) findViewById(R.id.buttonGNSS);
        Button botaoSobre = (Button) findViewById(R.id.buttonSobre);
        Button botaoNavegacao = (Button) findViewById(R.id.buttonNavegacao);
        botaoConfig.setOnClickListener(this);
        botaoSair.setOnClickListener(this);
        botaoSobre.setOnClickListener(this);
        botaoNavegacao.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonConfig:
                startActivity(new Intent(this,ConfigActivity.class));
                break;
            case R.id.buttonGNSS:
                finish();
                break;
            case R.id.buttonNavegacao:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                break;
            case R.id.buttonSobre:
                startActivity(new Intent(this,SobreActivity.class));
                break;
        }
    }


}