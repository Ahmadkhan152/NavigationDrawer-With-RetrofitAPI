package com.example.navigation_drawer.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Activities.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GooglemapFragement() : Fragment() {

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var longitude:Double?=null
    var latitude:Double?=null
    val requestCode:Int=345
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_googlemap_fragement, container, false)
        setHasOptionsMenu(true)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(view.context)
        supportMapFragment=childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        getCurrentLocation()
        return view
    }
    private fun getCurrentLocation() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it, android.Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),requestCode)
            val task:Task<Location> = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { it ->
                if (it!=null)
                {
                    latitude=it.latitude
                    longitude=it.longitude
                    supportMapFragment.getMapAsync {
                        googleMap=it
                        var latlong: LatLng=LatLng(latitude!!, longitude!!)
                        googleMap.addMarker(MarkerOptions().position(latlong).title("Latitude: ${latitude} And Longitude: ${longitude}"))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 10F))
                    }
                }
            }
        }
        else
        {
            val task:Task<Location> = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener { it ->
                if (it!=null)
                {
                    latitude=it.latitude
                    longitude=it.longitude
                    supportMapFragment.getMapAsync {
                        googleMap=it
                        var latlong: LatLng=LatLng(latitude!!, longitude!!)
                        googleMap.addMarker(MarkerOptions().position(latlong).title("Latitude: ${latitude} And Longitude: ${longitude}"))
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 10F))
                    }
                }
            }
        }
    }
        override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout,menu)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val alertDialog: AlertDialog.Builder= AlertDialog.Builder(context)
        if (item.itemId==R.id.btnLogout){
            alertDialog.setTitle("ALERT!")
            alertDialog.setMessage("Do You Want To Continue")
            alertDialog.setPositiveButton("YES") { a, b ->
                val sharedPreferences:SharedPreferences=requireContext().getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE)
                val editor=sharedPreferences.edit()
                editor.putBoolean(CHECK,false)
                editor.commit();
                val intent= Intent(context, HomePage::class.java)
                intent.putExtra(LOGOUT,true)
                startActivity(intent)
                activity?.finish()
            }
            alertDialog.setNegativeButton("NO") { a, b ->

            }

        }
        alertDialog.create()
        alertDialog.show()
        return super.onOptionsItemSelected(item)
    }
}