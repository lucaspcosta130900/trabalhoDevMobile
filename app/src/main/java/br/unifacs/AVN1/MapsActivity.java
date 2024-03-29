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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FusedLocationProviderClient client;
    private GoogleMap mMap;
    private TextView textCoordenadas, textVelocidade;
    private SharedPreferences sharedPrefs;
    private double latitude, longitude;
    private boolean courseUp, northUp;
    private String texto;
    private Marker marker;
    private Circle circulo = null;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(this);

        setContentView(R.layout.activity_maps);
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

        boolean mapaSatelite, isTrafegoOn;
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
        mMap.setTrafficEnabled(isTrafegoOn);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {

                        LatLng sydney = new LatLng(-34,151);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sidney"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        textCoordenadas.setText("Coordenadas: -34 S, 151 W");
                        textVelocidade.setText("  Velocidade: 0 Km/h\n");
                        LatLng cidade = new LatLng(-33.87365, 151.20689);
                        mMap.addMarker(new MarkerOptions()
                                .position(cidade)
                                .title("Local padrão")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cidade, 15));
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

        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    boolean velocidade, grau, grauMinuto, grauMinutoSegundo, semOrientacao;
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
                            .strokeColor(R.color.purple_700)
                            .fillColor(R.color.black)
                            .strokeWidth(location.getAccuracy())
                    );

                    courseUp = sharedPrefs.getBoolean("radioCourseUp", true);
                    northUp = sharedPrefs.getBoolean("radioNorthUp", true);
                    if (northUp) {
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLocation, 19));
                    }
                    if (courseUp) {
                        mMap.getUiSettings().setRotateGesturesEnabled(false);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(actualLocation)
                                .bearing(rotacao)
                                .zoom(19)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
                    }
                    if (!courseUp && !northUp) {
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