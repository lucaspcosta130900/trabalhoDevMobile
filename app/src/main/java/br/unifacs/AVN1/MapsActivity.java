package br.unifacs.AVN1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

//import br.unifacs.AVN1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient client;
    private GoogleMap mMap;
    private TextView textCoordenadas, textVelocidade;
    private SharedPreferences sharedPrefs;
    private double latitude;
    private double longitude;
    private boolean velocidade, mapaSatelite, isTrafegoOn, grau, grauMinuto, grauMinutoSegundo, courseUp, northUp, semOrientacao;
    private String texto;
    private Marker marker;
    private Circle circulo = null;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(12.0f);
        mMap.setMaxZoomPreference(40.0f);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mapaSatelite = sharedPrefs.getBoolean("radioSat", true);
        if (mapaSatelite) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        isTrafegoOn = sharedPrefs.getBoolean("switchOnOff", true);
        if (isTrafegoOn) {
            mMap.setTrafficEnabled(true);
        } else mMap.setTrafficEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
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

        client.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            LatLng sydney = new LatLng(-34,151);
                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sidney"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                            textCoordenadas.setText("coordenadas(-33.87365)");
                            velocidade = sharedPrefs.getBoolean("radioKmh", true);
                               if(velocidade){
                                    textVelocidade.setText("  Velocidade: 0 Km/h\n");
                                }else textVelocidade.setText("  Velocidade: 0 Mph\n");
                            LatLng cidade = new LatLng(-33.87365, 151.20689);
                            mMap.addMarker(new MarkerOptions()
                                    .position(cidade)
                                    .title("Local padrão")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cidade, 15));
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
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {

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


                    LatLng actualLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    float rotacao = location.getBearing();
                    rotacao = rotacao;

                    if (marker != null) {
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(actualLocation)
                            .title("Estou aqui")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                            .rotation(rotacao));

                    if(circulo != null){
                        circulo.remove();
                    }
                    circulo = mMap.addCircle(new CircleOptions()
                            .center(actualLocation)
                            .radius(18)
                            .strokeColor(R.color.black)
                            .fillColor(R.color.black)
                            .strokeWidth(location.getAccuracy())
                    );

                    courseUp = sharedPrefs.getBoolean("radioCourseUp", true);
                    northUp = sharedPrefs.getBoolean("radioNorthUp", true);
                    semOrientacao = sharedPrefs.getBoolean("radioNone", true);
                    if (northUp == true) {
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLocation, 19));
                    }
                    if (courseUp == true) {
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(actualLocation)
                                .bearing(rotacao)
                                .zoom(19)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
                    }
                    if (courseUp == false && northUp == false) {
                        mMap.getUiSettings().setRotateGesturesEnabled(true);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLocation, 19));
                    }
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            }
        };

        client.requestLocationUpdates(locationRequest, locationCallback, null);
    }

}