package com.example.navigation_drawer.Activities

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Fragments.GooglemapFragement
import com.example.navigation_drawer.Fragments.ListofuserFragment
import com.example.navigation_drawer.Fragments.WebviewFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.navigation.NavigationView

class NavigatorDrawer : AppCompatActivity() {

    lateinit var navigation:NavigationView
    lateinit var toolbar:Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarToggleButton: ActionBarDrawerToggle
    var token:String=""
    val requestCode:Int=345
    var imageFromGallery: Uri?=null
    var imageFromCamera: Bitmap?=null
    var email:String=""
    var name:String=""
    var checkForBitMap=true
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigator_drawer)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navigation=findViewById(R.id.navigation)
        drawerLayout=findViewById(R.id.drawerlayout)
        var progressBar:ProgressBar=findViewById(R.id.progresBar1)
        progressBar.visibility= View.VISIBLE
        val headerView=navigation.getHeaderView(0)
        val ivPicture=headerView.findViewById(R.id.ivPicture) as ImageView
        val tvName=headerView.findViewById(R.id.tvName) as TextView
        val tvEmail=headerView.findViewById(R.id.tvEmail) as TextView
        token=intent.getStringExtra(TOKEN).toString()
        email=intent.getStringExtra(EMAIL).toString()
        name=intent.getStringExtra(NAME).toString()
        val TAG="Token1234"
        Log.i(TAG,"The Tken Is: ${TAG}")
        checkForBitMap=intent.getBooleanExtra(CHECKFORBITMAP,false)
        if (checkForBitMap)
        {
            progressBar.visibility= View.INVISIBLE
            imageFromCamera = intent.getParcelableExtra(BITMAPIMAGE) as Bitmap?
            ivPicture.setImageBitmap(imageFromCamera)
        }
        else
        {
            progressBar.visibility= View.VISIBLE
            val image = intent.getStringExtra(IMAGEFROMGALLERY)
            if (image!=null)
            imageFromGallery=Uri.parse(image)
            ivPicture.setImageURI(imageFromGallery)
        }
        progressBar.visibility= View.INVISIBLE
        tvName.text= name
        tvEmail.text=email
        actionBarToggleButton= ActionBarDrawerToggle(this,drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(actionBarToggleButton)
        actionBarToggleButton.syncState()
        navigation.menu.getItem(0).isChecked = true
        val webViewFragment: WebviewFragment = WebviewFragment()
        val manager=supportFragmentManager
        val transaction=manager.beginTransaction()
        transaction.replace(R.id.fragment,webViewFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        drawerLayout.openDrawer(GravityCompat.START)
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        else
        {
            ActivityCompat.requestPermissions(this@NavigatorDrawer,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),requestCode)
        }
        navigation.setNavigationItemSelectedListener {
            if (it.itemId == R.id.home) {
                val webViewFragment: WebviewFragment = WebviewFragment()
                val manager=supportFragmentManager
                val transaction=manager.beginTransaction()
                transaction.replace(R.id.fragment,webViewFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                navigation.menu.getItem(1).isChecked = false
                navigation.menu.getItem(2).isChecked = false
                navigation.menu.getItem(0).isChecked = true
                true
            } else if (it.itemId == R.id.Place) {
                val googleFragment: GooglemapFragement = GooglemapFragement()
                val manager=supportFragmentManager
                val transaction=manager.beginTransaction()
                transaction.replace(R.id.fragment,googleFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                navigation.menu.getItem(0).isChecked = false
                navigation.menu.getItem(2).isChecked = false
                navigation.menu.getItem(1).isChecked = true
                drawerLayout.closeDrawer(GravityCompat.START)
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }else if (it.itemId == R.id.Users) {
                val listOfUserFragment: ListofuserFragment = ListofuserFragment(token)
                val manager=supportFragmentManager
                val transaction=manager.beginTransaction()
                transaction.replace(R.id.fragment,listOfUserFragment)
                transaction.addToBackStack(null)
                transaction.commit()
                navigation.menu.getItem(0).isChecked = false
                navigation.menu.getItem(1).isChecked = false
                navigation.menu.getItem(2).isChecked = true
                drawerLayout.closeDrawer(GravityCompat.START)
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

}