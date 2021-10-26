package br.unifacs.AVN1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//import br.unifacs.AVN1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    FusedLocationProviderClient client;
    private GoogleMap mMap;
    private TextView textLatitude, textLongitude, textVelocidade;
    private SharedPreferences sharedPrefs;
    List<Location> local = new ArrayList<Location>();
    private String cordenada, velocidade, orientacao, trafego, tipo;
    public static final String SHARED_PREFES = "sharedPrefes";
    //private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(this);

        //binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPrefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        textLatitude = (TextView) findViewById(R.id.textLatitude);
        textLongitude = (TextView) findViewById(R.id.textLongitude);
        //textVelocidade = (TextView) findViewById(R.id.text_Velocidade);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location localizacaoAtual = locationManager.getLastKnownLocation(provider);

        client.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.i("Teste", location.getLatitude() + " " + location.getLongitude());

                            mMap.setMinZoomPreference(12.0f);
                            mMap.setMaxZoomPreference(40.0f);

                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().isRotateGesturesEnabled();

                            LatLng actualLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(actualLocation).title("Localização Atual"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(actualLocation, 15));


                        } else {
                            Log.i("Teste", "null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(8 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("Teste",
                                locationSettingsResponse
                                        .getLocationSettingsStates().isNetworkLocationPresent() + "");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(MapsActivity.this, 10);
                            } catch (IntentSender.SendIntentException e1) {
                            }
                        }
                    }
                });

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null){
                    Log.i("Teste 2", "local is null");
                    return;
                }

                for(Location location : locationResult.getLocations()) {
                    Log.i("Teste 2", location.getLatitude() + "");
                    sharedPrefs = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    textLatitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
                    textLongitude.setText("Longitude: " +String.valueOf(location.getLongitude()));

                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                Log.i("Teste", locationAvailability.isLocationAvailable() + "");
            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, null);
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public String coordenadas(double lat){
        double valGrau, valMin, valSeg, auxSeg = 0;
        String result = "";
        String retorno = "";
        String identificador = "COORDENADA";
        String identificador2 = "COORDENADA2";
        String identificador3 = "COORDENADA3";
        double aux = lat;

        lat = Math.abs(lat);

        Boolean Coordenada2 = sharedPrefs.getBoolean("Coordenada_2", false);
        Boolean Coordenada3 = sharedPrefs.getBoolean("Coordenada_3", false);

        if(Coordenada2 == false && Coordenada3 == false){
            //para grau
            lat = Math.abs(lat);

            valGrau = Math.floor(lat);
            result = formatarFloat(valGrau, identificador) + "º";

            auxSeg = Math.floor((lat - valGrau) * 60);
            valMin = Math.floor((lat - valGrau) * 60) / 60;
            result += formatarFloat(valMin, identificador2);

            valSeg = (Math.floor((lat - valGrau - auxSeg / 60) * 3600 * 1000) / 1000) / 3600;
            result += formatarFloat(valSeg, identificador3);
        }

        if(Coordenada2 == true){
            //para grau e minutos
            valGrau = Math.floor(lat);
            result = formatarFloat(valGrau, identificador) + "º";

            valMin = Math.floor((lat - valGrau) * 60);
            result += formatarFloat(valMin, identificador) + ".";

            valSeg = (Math.floor((lat - valGrau - valMin / 60) * 3600 * 1000) / 1000) / 3600;
            result += formatarFloat(valSeg, identificador3) + "'";
        }

        if(Coordenada3 == true){
            //para grau, minutos e segundos
            valGrau = Math.floor(lat);
            result = formatarFloat(valGrau, identificador) + "º";

            valMin = Math.floor((lat - valGrau) * 60);
            result += formatarFloat(valMin, identificador) + "'";

            valSeg = Math.round((lat - valGrau - valMin / 60) * 3600 * 1000) / 1000;
            result += formatarFloat(valSeg, identificador) + "''";
        }
        if(aux <= -0){
            retorno = "-" + result;
        }
        else{
            retorno = result;
        }
        return retorno;
    }

    public String formatarFloat(double numero, String identificador){
        String retorno = "";
        if(identificador.equals("VELOCIDADE")){
            DecimalFormat formatter = new DecimalFormat("0.00");
            try{
                retorno = formatter.format(numero);
            }catch(Exception ex){
                System.err.println("Erro ao formatar numero: " + ex);
            }
        }
        if(identificador.equals("COORDENADA")){
            DecimalFormat formatter = new DecimalFormat("00");
            try{
                retorno = formatter.format(numero);
            }catch(Exception ex){
                System.err.println("Erro ao formatar numero: " + ex);
            }
        }
        if(identificador.equals("COORDENADA2")){
            DecimalFormat formatter = new DecimalFormat("#.00");
            try{
                retorno = formatter.format(numero);
                retorno = retorno.replace(",", "");
            }catch(Exception ex){
                System.err.println("Erro ao formatar numero: " + ex);
            }
        }
        if(identificador.equals("COORDENADA3")){
            DecimalFormat formatter = new DecimalFormat("#.000");
            try{
                retorno = formatter.format(numero);
                retorno = retorno.replace(",", "");
            }catch(Exception ex){
                System.err.println("Erro ao formatar numero: " + ex);
            }
        }
        return retorno;
    }

    public void getShared(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFES, MODE_PRIVATE);

        cordenada = sharedPreferences.getString("cordenadas", "unknown");
        velocidade = sharedPreferences.getString("velocidade", "unknown");
        orientacao = sharedPreferences.getString("orientacao ", "unknown");
        trafego = sharedPreferences.getString("trafegon", "unknown");
        tipo = sharedPreferences.getString("tipo", "unknown");
    }

}