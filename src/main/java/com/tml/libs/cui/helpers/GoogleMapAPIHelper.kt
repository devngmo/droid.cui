package com.tml.libs.cui.helpers

import android.app.Activity
import android.content.IntentSender
import com.google.android.gms.common.api.*
import com.google.android.gms.location.*
import com.tml.libs.cutils.AppUtils


/**
 * Created by TML on 15/03/2018.
 */
class GoogleMapAPIHelper {
    companion object {
        val REQUEST_CODE_CHECK_LOCATION_SETTINGS = 2000

        fun askUserForUsingGPSIfNeed(a: Activity,
                                     lsRequest: LocationSettingsRequest,
                                     askForPermission:Boolean
                                    ) {
            if (askForPermission) {
                AppUtils.askForPermissionIfNeed(a,
                        arrayOf(
                                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                android.Manifest.permission.ACCESS_FINE_LOCATION
                        ))
            }


            val client = LocationServices.getSettingsClient(a)
            val task = client.checkLocationSettings(lsRequest)

            task.addOnSuccessListener(a) { }

            task.addOnFailureListener(a) { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    CommonStatusCodes.RESOLUTION_REQUIRED ->
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            val resolvable = e as ResolvableApiException
                            resolvable.startResolutionForResult(a,
                                    REQUEST_CODE_CHECK_LOCATION_SETTINGS)
                        } catch (sendEx: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }// Location settings are not satisfied. However, we have no way
                // to fix the settings so we won't show the dialog.
            }
        }

        fun requestWithPendingResult(locRequest:LocationRequest, apiClient:GoogleApiClient ) {
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locRequest)
            builder.setAlwaysShow(true)
            var lsr = builder.build()

            val result = LocationServices.SettingsApi.checkLocationSettings(
                    apiClient,
                    lsr
            )

            //result.setResultCallback(a)
        }
    }
}
