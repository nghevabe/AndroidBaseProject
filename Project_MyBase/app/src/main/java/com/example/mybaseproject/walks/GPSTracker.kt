package com.example.mybaseproject.walks

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
//import com.vnpay.seslib.R

class GPSTracker constructor(context: Context) : LocationListener {
    private var mLocationRequest: LocationRequest? = null
    private val locationManager: LocationManager?
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    internal var event: EventGPS? = null


    internal var callback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            Log.d("LOC", "Location ve: locationResult")
            if (locationResult != null && locationResult!!.lastLocation != null) {
                Log.d("AAA", " P: " + locationResult!!.lastLocation.toString())
                onResultLocation(locationResult!!.lastLocation)
            }

        }
    }

    init {
        callGpsService(context)
        locationTracker(context)
        locationManager = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager
    }

    private fun callGpsService(context: Context) {
        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            onConnected(context)
        } catch (e: Exception) {
        }

    }


    private fun locationTracker(context: Context) {
        try {
            if ((ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) === PackageManager.PERMISSION_GRANTED)
            ) {
                if (locationManager != null)
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        20000,
                        20f,
                        this
                    )
            }
        } catch (e: Exception) {
        }

    }


    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat()
    }

    fun onPause() {
        stopLocationUpdates()
    }

    protected fun stopLocationUpdates() {
        try {
            if (locationManager != null)
                locationManager!!.removeUpdates(this)
            mFusedLocationClient!!.removeLocationUpdates(callback)
        } catch (e: Exception) {
        }

    }

    fun onConnected(context: Context) {
        createLocationRequest()
        Log.d("STATE", "onConnected")
        if (((ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED))
        ) {
            return
        }
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        if (code != ConnectionResult.SUCCESS) {
            Log.d("GPS", "No google play service")
            return
        }
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            callback,
            Looper.getMainLooper()
        )
            .addOnCompleteListener {
                Log.d("LOC", "Location ve: Void")
                getCurrentLocation()
            }

    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        try {
            mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                Log.d("LOC", "Location ve: getLastLocation")
                try {
                    if (task != null && task.isSuccessful) {
                        val location = task.result
                        onResultLocation(location)
                    }
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
        }

    }

    fun setListener(event: EventGPS?) {
        if (event == null) {
            mLocationRequest = null
        }
        this.event = event
    }

    interface EventGPS {
        fun successGPS()
    }

    private fun onResultLocation(location: Location?) {
        if (location != null) {
            Log.d("LOCATION", "Đã nhận: " + location!!.toString())
            if (GPSTracker.location == null)
                GPSTracker.location = location

            stopLocationUpdates()

            if (event != null)
                event!!.successGPS()
        }
    }


    override fun onLocationChanged(location: Location) {
        onResultLocation(location)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {
        Log.d("ENABLE_GPS", "P: $provider")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d("DISABLE_GPS", "P: $provider")
    }

    companion object {
        private var instance: GPSTracker? = null
        private val UPDATE_INTERVAL = 20000
        private val FATEST_INTERVAL = 10000
        private val DISPLACEMENT = 1
        var location:Location? = null

        fun run(context: Context): GPSTracker? {
            try {
                if ((GPSTracker.location == null && context is Activity && (((ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED) || ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED)))
                ) {

                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        1005
                    )
                }
                if (GPSTracker.location == null)
                    if (instance == null)
                        instance = GPSTracker(context)
                    else
                        instance!!.locationTracker(context)
            } catch (e: Exception) {
            }

            return instance
        }

        fun call(context: Context, event: EventGPS) {
            if (instance == null)
                instance = GPSTracker(context)
            else
                instance!!.locationTracker(context)
            instance!!.event = event
        }

        fun destroy() {
            if (instance != null)
                instance!!.event = null
            instance = null
        }

        fun checkEnableGPS(context: Context): Boolean {
            try {
                val locationManager =
                    context.getSystemService(Service.LOCATION_SERVICE) as LocationManager
                val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                val rs = isGPSEnabled && isNetworkEnabled
                if (rs && GPSTracker.location == null)
                    run(context)
                return rs
            } catch (e: Exception) {
            }

            return false
        }

//        fun showSettingsAlert(mContext: Context) {
//            val alertDialog = AlertDialog.Builder(mContext)
//            alertDialog.setTitle(R.string.alert_thong_bao)
//            alertDialog.setMessage(mContext.getString(R.string.request_location))
//            alertDialog.setPositiveButton(
//                mContext.getString(R.string.home_cai_dat)
//            ) { _, _ ->
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                mContext.startActivity(intent)
//            }
//            alertDialog.setNegativeButton(
//                mContext.getString(R.string.cancel),
//                object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface, which: Int) {
//                        dialog.cancel()
//                    }
//                })
//            alertDialog.show()
//        }
    }

}
