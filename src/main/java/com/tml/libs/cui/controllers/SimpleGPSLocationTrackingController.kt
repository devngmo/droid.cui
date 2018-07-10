package com.tml.libs.cui.controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.tml.libs.cui.helpers.GoogleMapAPIHelper
import com.tml.libs.cutils.StaticLogger

interface LocationTrackingControllerInterface {
    fun startTracking()
    fun stopTracking()
    fun getCurrentLocation():Location?
}

interface OnLocationChangedListener {
    fun locationTrackingOnLocationChanged(locationResult:LocationResult?)
    fun onRequestLocationUpdateException(ex:Exception)
    fun onLocationUpdateStopped()
    fun onRequestLocationUpdateCreated()
}

public val MILISECONDS_PER_SECOND = 1000
open class SimpleGPSLocationTrackingController(val activity:Activity, val locListener:OnLocationChangedListener) : LocationTrackingControllerInterface {
    var locationManager: LocationManager
    var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mLocationCallback: LocationCallback
    @SuppressLint("RestrictedApi")
    var mLocationRequest = LocationRequest()
    var locationUpdateRequested = false
    var startDetectTime: Long = 0
    var locationRequestCreated = false

    var location : Location? = null

    override fun getCurrentLocation(): Location? {
        if (location == null)
            return getLastKnownLocation()
        return location
    }

    init {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        setupLocationCallback()
    }

    fun setupLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations.size > 0) {
                    location = locationResult.locations[0]
                    locListener.locationTrackingOnLocationChanged(locationResult)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun createLocationRequest() {
        if (!locationRequestCreated) {
            locationRequestCreated = true
            mLocationRequest.interval = 200
            mLocationRequest.fastestInterval = 100
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest)
                    .setAlwaysShow(true)

            GoogleMapAPIHelper.askUserForUsingGPSIfNeed(activity, builder.build(), false)
        }

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, null)
            locListener.onRequestLocationUpdateCreated()

        } catch (ex: Exception) {
            locListener.onRequestLocationUpdateException(ex)
        }

    }

    override fun stopTracking() {
        StaticLogger.I(this, "[LOCATION TRACKING] stop")
        locationUpdateRequested = false
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        } catch (ex: Exception) {
        }
        locListener.onLocationUpdateStopped()
    }

    override fun startTracking() {
        StaticLogger.I(this, "[LOCATION TRACKING] start")
        startDetectTime = System.currentTimeMillis()

        if (locationUpdateRequested)
            return

        locationUpdateRequested = true
        createLocationRequest()
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(): Location? {
        val gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val wifiLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (wifiLoc != null) {
            return wifiLoc
        }
        if (gpsLoc != null) {
            return gpsLoc
        }
        return null
    }

    companion object {
        @JvmStatic fun newInstance(activity:Activity, listener:OnLocationChangedListener) : SimpleGPSLocationTrackingController {
            return SimpleGPSLocationTrackingController(activity, listener)
        }
    }
}