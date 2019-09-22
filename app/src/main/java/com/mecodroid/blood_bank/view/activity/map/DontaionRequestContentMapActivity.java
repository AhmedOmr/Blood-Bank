package com.mecodroid.blood_bank.view.activity.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mecodroid.blood_bank.R;
import com.mecodroid.blood_bank.helper.GPSTracker;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DontaionRequestContentMapActivity extends FragmentActivity implements OnMapReadyCallback, DirectionCallback {

    @BindView(R.id.directionsShowDonation)
    Button directionsShowDonation;
    @BindView(R.id.directionsCalculateDistance)
    TextView directionsCalculateDistance;
    private GoogleMap mMap;
    private GPSTracker gps;
    private double latitude, longitude;

    double latitudePoint, longitudePoint;
    private LatLng origin;
    private LatLng destination;
    private String serverKey = "AIzaSyC1ORmVcWN0PKrDxJbHSdvvvy6_NHaP0pw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request_content_map);
        ButterKnife.bind(this);
        init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapShowDonation);
        mapFragment.getMapAsync(this);
    }

    private void init() {
        Intent getMap = getIntent();
        latitudePoint =  getMap.getDoubleExtra("latitudePoint",230.0);
        longitudePoint =  getMap.getDoubleExtra("longitudePoint",4560.0);
        origin = new LatLng(latitude, -longitude);
        destination = new LatLng(latitudePoint, -longitudePoint);

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
        gps = new GPSTracker(this, this);

        // Check if GPS enabled
        if (gps.getIsGPSTrackingEnabled()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            gps.showSettingsAlert();
        }

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.setMyLocationEnabled(true);


        // Add a marker in Sydney and move the camera
        LatLng locLatlang = new LatLng(latitudePoint, longitudePoint);
        mMap.addMarker(new MarkerOptions().position(locLatlang).title("Patient Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locLatlang,13.0f));

    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.directionsShowDonation)
    public void onViewClicked() {
        requestDirection();
        directionsCalculateDistance.setText(calculateDistance(longitude, latitude, longitudePoint, latitudePoint) + " M");
    }

    private double calculateDistance(double fromLong, double fromLat,
                                     double toLong, double toLat) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }

    public void requestDirection() {
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }


    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(directionsShowDonation, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();

        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            mMap.addMarker(new MarkerOptions().position(origin));
            mMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);

        } else {
            Toast.makeText(DontaionRequestContentMapActivity.this,direction.getStatus()+"\n"+direction.getErrorMessage(),Toast.LENGTH_SHORT).show();
            Snackbar.make(directionsShowDonation, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(directionsShowDonation, t.getMessage(), Snackbar.LENGTH_SHORT).show();

    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }


}
