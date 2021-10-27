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
import java.util.function.DoubleToIntFunction;

//import br.unifacs.AVN1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    FusedLocationProviderClient client;
    private GoogleMap mMap;
    private TextView textCoordenadas, textVelocidade;
    private SharedPreferences sharedPrefs;
    private double latitude;
    private double longitude;
    private boolean velocidade, mapaSatelite, isTrafegoOn, grau, grauMinuto, grauMinutoSegundo;
    private String texto;
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

        sharedPrefs = getSharedPreferences("Seetings", Context.MODE_PRIVATE);
        textCoordenadas = (TextView) findViewById(R.id.textCoordenadas);
        textVelocidade = (TextView) findViewById(R.id.textVelocidade);
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

                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.getUiSettings().setZoomControlsEnabled(true);
                            mMap.getUiSettings().isRotateGesturesEnabled();

                            mapaSatelite = sharedPrefs.getBoolean("radioSat", true);
                            if(mapaSatelite){
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            } else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            isTrafegoOn = sharedPrefs.getBoolean("switchOnOff", true);
                            if(isTrafegoOn){
                                mMap.setTrafficEnabled(true);
                            }else mMap.setTrafficEnabled(false);

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
                    sharedPrefs = getSharedPreferences("Seetings", Context.MODE_PRIVATE);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String NorthOrSouth = (int)latitude >= 0 ? " N" : " S";
                    String EastOrWest = (int)longitude >= 0 ? " E" : " W";
                    grau = sharedPrefs.getBoolean("radioDec", true);
                    grauMinuto = sharedPrefs.getBoolean("radioMinDec", true);
                    grauMinutoSegundo = sharedPrefs.getBoolean("radioMSDec", true);
                    if(grau) {
                        int latOnlyGrau = (int)latitude;
                        int lonOnlyGrau = (int)longitude;
                        textCoordenadas.setText("  Coordenadas: " + latOnlyGrau + NorthOrSouth + ";  " + lonOnlyGrau + EastOrWest);
                    }else if (grauMinuto) {
                        String strLatitude = Location.convert(location.getLatitude(), Location.FORMAT_MINUTES).replace(":","° ") + "\'" +NorthOrSouth;
                        String strLongitude = Location.convert(location.getLongitude(), Location.FORMAT_MINUTES).replace(":","° ") + "\'" + EastOrWest;
                        textCoordenadas.setText("  Coordenadas: " + strLatitude + ";  " + strLongitude);
                    }
                    else if (grauMinutoSegundo) {
                        String strLatitudeSeconds = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + NorthOrSouth;
                        String strLongitudeSeconds = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + EastOrWest;
                        textCoordenadas.setText("  Coordenadas: " + strLatitudeSeconds + "  \n                              " + strLongitudeSeconds);
                    }
                    velocidade = sharedPrefs.getBoolean("radioKmh", true);
                    texto= String.valueOf(location.getSpeed());
                    if(velocidade){
                        textVelocidade.setText("  Velocidade: " + texto + " Km/h\n");
                    }else textVelocidade.setText("  Velocidade: " + texto + " Mph\n");

                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                Log.i("Teste", locationAvailability.isLocationAvailable() + "");
            }
        };
        client.requestLocationUpdates(locationRequest, locationCallback, null);
//        Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
//        case "Grau-Minuto decimal":
//            String strLatitude = Location.convert(location.getLatitude(), Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + latSN;
//            String strLongitude = Location.convert(location.getLongitude(), Location.FORMAT_MINUTES).replace(":","° ").replace(",","\' ") + lonEW;
//            longLat.setText("LATITUDE: " + strLatitude + "\n\n" + "LONGITUDE: " + strLongitude + "\n");
//            break;
//        case "Grau-Minuto-Segundo decimal":
//            String strLatitudeSeconds = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + latSN;
//            String strLongitudeSeconds = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS).replaceFirst(":","° ").replace(":","\' ").replace(",",".") + "\" " + lonEW;
//            longLat.setText("LATITUDE: " + strLatitudeSeconds + "\n\n" + "LONGITUDE: " + strLongitudeSeconds + "\n");
//            break;
//        case "Grau decimal":
//            longLat.setText("LATITUDE: " + latitude + "\n\n" + "LONGITUDE: " + longitude + "\n");
//            break;

}