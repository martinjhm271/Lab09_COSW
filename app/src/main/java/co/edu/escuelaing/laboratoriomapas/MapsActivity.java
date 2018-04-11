package co.edu.escuelaing.laboratoriomapas;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private final int ACCESS_LOCATION_PERMISSION_CODE = 0;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private TextView address;
    private LocationLocal l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        address = (TextView) findViewById( R.id.address );


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationRequest = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mGoogleApiClient.connect();

        final FloatingActionButton  button3 = findViewById(R.id.floatingActionButton);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addLocation();
            }
        });

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
        showMyLocation();
    }

    @SuppressLint("MissingPermission")
    public void showMyLocation() {
        if (mMap != null) {
            String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION};
            if (hasPermissions(this, permissions)) {
                mMap.setMyLocationEnabled(true);

                if(getIntent().getExtras()!=null){
                    l=(LocationLocal)getIntent().getExtras().get("newLocation");
                    Location targetLocation = new Location("");
                    targetLocation.setLatitude(l.getLatitude());
                    targetLocation.setLongitude(l.getLongitude());
                    if (targetLocation != null) {
                        addMarkerAndZoom(targetLocation, l.getName()+" "+l.getDescription(), 15);
                    }
                }
                else{
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (lastLocation != null) {
                        addMarkerAndZoom(lastLocation, "My Location", 15);
                    }
                }


            } else {
                ActivityCompat.requestPermissions(this, permissions, ACCESS_LOCATION_PERMISSION_CODE);
            }
        }
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public void addMarkerAndZoom(Location location, String title, int zoom) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == -1) {
                return;
            }
        }
        System.out.println(requestCode);
        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION_CODE:
                showMyLocation();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        if (hasPermissions(this, permissions)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            showMyLocation();
                            stopLocationUpdates();
                        }
                    });
        }else {
            ActivityCompat.requestPermissions(this, permissions, ACCESS_LOCATION_PERMISSION_CODE);
        }


    }

    @Override
    public void onConnectionSuspended( int i )
    {
        LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, (PendingIntent) null);
    }

    public void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates( mGoogleApiClient, new LocationListener()
        {
            @Override
            public void onLocationChanged( Location location )
            {

            }
        } );
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onFindAddressClicked( View view ) {
        startFetchAddressIntentService();
    }

    public void startFetchAddressIntentService()
    {
        @SuppressLint("MissingPermission") Location lastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
        if(l!=null){
            lastLocation = new Location("");
            lastLocation.setLatitude(l.getLatitude());
            lastLocation.setLongitude(l.getLongitude());
        }
        if ( lastLocation != null )
        {
            AddressResultReceiver addressResultReceiver = new AddressResultReceiver( new Handler() );
            addressResultReceiver.setAddressResultListener( new AddressResultListener()
            {
                @Override
                public void onAddressFound( final String address )
                {
                    runOnUiThread( new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            MapsActivity.this.address.setText( address );
                            MapsActivity.this.address.setVisibility( View.VISIBLE );
                        }
                    } );


                }
            } );
            Intent intent = new Intent( this, FetchAddressIntentService.class );
            intent.putExtra( FetchAddressIntentService.RECEIVER, addressResultReceiver );
            intent.putExtra( FetchAddressIntentService.LOCATION_DATA_EXTRA, lastLocation );
            startService( intent );
        }
    }

    public void addLocation(){
            Intent intent = new Intent( this, AddLocationActivity.class );
            startActivity(intent);

    }

}

