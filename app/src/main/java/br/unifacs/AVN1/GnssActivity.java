package br.unifacs.AVN1;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.DecimalFormat;

public class GnssActivity extends AppCompatActivity implements LocationListener{
    private LocationManager locationManager;
    private LocationProvider locationProvider;
    private GnssStatusCallback gnssStatusCallback;
    private SharedPreferences sharedPrefs;
    private TextView txtCoordenadas, txtVelocidade, txtBearing;
    private double latitude, longitude;
    private String texto;
    private circuloGnssView circuloGnss;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gnss);
        circuloGnss=findViewById(R.id.circuloGnss);
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        sharedPrefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        dadosView();
        ativaGNSS();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStop() {
        super.onStop();
        desativaGNSS();
    }

    public void dadosView(){
        txtCoordenadas = (TextView) findViewById(R.id.coordenadasGnss);
        txtVelocidade = (TextView) findViewById(R.id.velocidadeGnss);
        txtBearing = (TextView) findViewById(R.id.bearingGnss);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void desativaGNSS() {
       // locationManager.removeUpdates(this);
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ativaGNSS(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GnssActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(locationProvider.getName(),
                5*1000,
                0.1f,
                GnssActivity.this);
        gnssStatusCallback = new GnssStatusCallback();
        locationManager.registerGnssStatusCallback(gnssStatusCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    ativaGNSS();
                }
                else{
                    Toast.makeText(this, "Localização desativada", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }

    public void escreverNaBarraDeStatusLocalizacao(double latitude, double longitude, double velocidade, double bearing){
        txtCoordenadas.setText(definirCoordenadas(latitude, longitude));
        txtVelocidade.setText(definirVelocidade(velocidade));
        txtBearing.setText(definirBearing(bearing));
    }

    public String definirBearing (double bearing){
        String value = "";
        value = ("Bearing: "+ String.valueOf(bearing));
        return value;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        escreverNaBarraDeStatusLocalizacao(location.getLatitude(), location.getLongitude(), location.getSpeed(), location.getBearing());
    }

    public String definirVelocidade(double velo){
        String velocidadeFormatada = "";
        boolean velocidade;
        velocidade = sharedPrefs.getBoolean("radioKmh", true);
        texto= String.valueOf(velo);
        if(velocidade){
            velocidadeFormatada = ("  Velocidade: " + texto + " Km/h\n");
        }else {
            velocidadeFormatada = ("  Velocidade: " + texto + " Mph\n");
        }
        return velocidadeFormatada;
    }

    public String definirCoordenadas(double lat, double lon) {
        boolean grau, grauMinuto, grauMinutoSegundo;
        sharedPrefs = getSharedPreferences("Seetings", Context.MODE_PRIVATE);
        latitude = lat;
        longitude = lon;
        String coordenadas = "";
        String NorthOrSouth = (int)latitude >= 0 ? " N" : " S";
        String EastOrWest = (int)longitude >= 0 ? " E" : " W";
        grau = sharedPrefs.getBoolean("radioDec", true);
        grauMinuto = sharedPrefs.getBoolean("radioMinDec", true);
        grauMinutoSegundo = sharedPrefs.getBoolean("radioMSDec", true);
        if(grau) {
            int latOnlyGrau = (int)latitude;
            int lonOnlyGrau = (int)longitude;
            coordenadas = ("  Coordenadas: " + latOnlyGrau + NorthOrSouth + ";  " + lonOnlyGrau + EastOrWest);
        }else if (grauMinuto) {
            String strLatitude = Location.convert(latitude, Location.FORMAT_MINUTES).replace(":","° ") + "\'" +NorthOrSouth;
            String strLongitude = Location.convert(longitude, Location.FORMAT_MINUTES).replace(":","° ") + "\'" + EastOrWest;
            coordenadas = ("  Coordenadas: " + strLatitude + ";  " + strLongitude);
        }
        else if (grauMinutoSegundo) {
            String strLatitudeSeconds = Location.convert(latitude, Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + NorthOrSouth;
            String strLongitudeSeconds = Location.convert(longitude, Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + EastOrWest;
            coordenadas = ("  Coordenadas: " + strLatitudeSeconds + "  \n                              " + strLongitudeSeconds);
        }
        return coordenadas;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private class GnssStatusCallback extends GnssStatus.Callback{
        public GnssStatusCallback(){
            super();
        }
        @Override
        public void onStarted(){
        }
        @Override
        public void onStopped(){
        }
        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            circuloGnss.onSatelliteStatusChanged(status);
            circuloGnss.invalidate();
        }
    }
}
