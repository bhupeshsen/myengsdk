package com.coderivium.p4rcintegrationsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.p4rc.sdk.AppConfig;
import com.p4rc.sdk.OnCompanyDetailsCallback;
import com.p4rc.sdk.OnGamePointsCallback;
import com.p4rc.sdk.P4RC;
import com.p4rc.sdk.model.CompanyDetails;
import com.p4rc.sdk.model.gamelist.GamePoints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Task;

public class MainActivity2 extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 88;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    Button materialButton;
    ProgressBar progressLoader;

    String baseUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
      //  requestLocationPermission();
        materialButton = findViewById(R.id.webViewBtn);
        progressLoader = findViewById(R.id.progressLoader);
        progressLoader.setVisibility(View.VISIBLE);
        materialButton.setVisibility(View.GONE);
        gameCompanyDetails();
        materialButton.setOnClickListener(v->{

        });
        materialButton.setVisibility(View.GONE);

    }
    public  void loadWebView(){
        requestLocationPermission();
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        if(ActivityCompat.checkSelfPermission(MainActivity2.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Location();
        }
        else{
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
    private  void gameCompanyDetails(){
        P4RC.getInstance().getCompanyDetails(new OnCompanyDetailsCallback() {
            @Override
            public void onSuccess(CompanyDetails data) {
                progressLoader.setVisibility(View.GONE);
                materialButton.setVisibility(View.GONE);
                Log.e("website url",data.getDomainName().toString());
                baseUrl = data.getDomainName();
                loadWebView();
            }

            @Override
            public void onError(int errorCode, String message) {
                progressLoader.setVisibility(View.GONE);
                materialButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private void Location() {

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);

        com.google.android.gms.tasks.Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.


                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        MainActivity2.this,
                                        REQUEST_LOCATION);
                            } catch (ClassCastException | IntentSender.SendIntentException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                           // sendNotification("Failed", "Setting or not avaible" );

                            break;
                    }
                }
            }
        });

    }


    private void requestLocationPermission(){

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                P4RC.getInstance().P4RCInitWeb(getApplicationContext(),baseUrl);

            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity2.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

}

public void checkPermission(String permission, int requestCode)
{
    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {

        // Requesting the permission
        ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
    }
    else {
       // Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}