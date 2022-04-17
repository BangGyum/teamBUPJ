package com.banggyum.test;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.ZoomControlView;

import java.security.acl.Permission;

public class MapsNaverActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static class LocationConfirmDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.location_activation_confirm)
                    .setPositiveButton(R.string.yes, (dialog, whichButton) -> {
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((MapsNaverActivity)activity).continueLocationTracking();
                        }
                    })
                    .setNegativeButton(R.string.no, (dialog, whichButton) -> {
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((MapsNaverActivity)activity).cancelLocationTracking();
                        }
                    })
                    .setOnCancelListener(dialog -> {
                        Activity activity = getActivity();
                        if (activity != null) {
                            ((MapsNaverActivity)activity).cancelLocationTracking();
                        }
                    })
                    .create();
        }
    }

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private FusedLocationSource locationSource;
    @Nullable
    private Runnable locationActivationCallback;
    private NaverMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps_naver);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        MapFragment mapFragment = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_view);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(new NaverMapOptions().locationButtonEnabled(true));
            getSupportFragmentManager().beginTransaction().add(R.id.map_view, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        locationSource.setActivationHook(continueCallback -> {
            locationActivationCallback = continueCallback;
            new LocationConfirmDialogFragment().show(getSupportFragmentManager(), null);
        });
    }

    private void continueLocationTracking() {
        if (locationActivationCallback != null) {
            locationActivationCallback.run();
            locationActivationCallback = null;
            locationSource.setActivationHook(null);
        }
    }

    private void cancelLocationTracking() {
        map.setLocationTrackingMode(LocationTrackingMode.None);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                map.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.addOnOptionChangeListener(() -> {
            LocationTrackingMode mode = naverMap.getLocationTrackingMode();
            locationSource.setCompassEnabled(mode == LocationTrackingMode.Follow || mode == LocationTrackingMode.Face);
        });
    }
}