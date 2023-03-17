package com.example.userlocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class GeoLocationActivity : AppCompatActivity() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var  latitudeTV: TextView
    private lateinit var  longitudeTV: TextView

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private var isGPSEnabled = false
    private var isNetworkEnabled = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_location)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        latitudeTV = findViewById(R.id.latitude)
        longitudeTV = findViewById(R.id.longitude)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                Log.d("UNI", "51 haspermission " )

                getLocation()
            }
        }
    }

    private fun getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.d("UNI", "62  " )

                locationResult.lastLocation.apply {
                    // Do something with the new location
                    Log.d("UNI", "67  " )

                    if(this != null){
                        Log.d("UNI", "70 displaying coordinates   " )

                        val latitude = this.latitude
                        val longitude = this.longitude
                        latitudeTV.text = latitude.toString()
                        longitudeTV.text = latitude.toString()
                        Toast.makeText(applicationContext, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
                        Log.d("UNI", "75 displayed coordinates   " )

                    }else{
                        Log.d("UNI", "75  " )

                        Toast.makeText(
                            applicationContext,
                            "Unable to capture location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
                Log.d("UNI", "85 removing location updates" )

                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }

        Log.d("UNI", "91  locationRequest" )

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).apply {
            setMinUpdateDistanceMeters(0f)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Log.d("UNI", "109 need permission 2nd" )

            Toast.makeText(applicationContext,"Need permission", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("UNI", "115" )

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}