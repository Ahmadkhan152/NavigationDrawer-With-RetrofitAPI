package com.example.navigation_drawer.Activities

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Classes.LoginResponse
import com.example.navigation_drawer.Classes.UserInfoLogin
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceLogin
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomePage : AppCompatActivity() {

    lateinit var etPassword:TextInputEditText
    lateinit var etMail:TextInputEditText
    lateinit var btnPicture:Button
    lateinit var btnLogin:Button
    lateinit var btnSignup:Button
    lateinit var ivPicture:ImageView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var checkBox:CheckBox
    var imageFromGallery:Uri?=null
    var imageFromCamera: Bitmap?=null
    var checkForBitMap:Boolean=false
    var userToken:Boolean=true
    var email:String=""
    var password:String=""
    private val requestCodeForCamera:Int=789
    private val getContentForCamera=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        activityResult:ActivityResult?->
        if (activityResult?.resultCode== RESULT_OK)
        {
            imageFromCamera=activityResult.data?.getParcelableExtra<Bitmap>("data")
            ivPicture.setImageBitmap(imageFromCamera)
            checkForBitMap=true
        }
    }
    private val getContentForGallery=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            activityResult:ActivityResult?->
        if (activityResult?.resultCode== RESULT_OK)
        {
            imageFromGallery=activityResult.data?.data
            ivPicture.setImageURI(imageFromGallery)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_home)
        val logout=intent.getBooleanExtra(LOGOUT,false)
        etPassword=findViewById(R.id.etPassword)
        etMail=findViewById(R.id.etEmail)
        btnPicture=findViewById(R.id.btnPicture)
        ivPicture=findViewById(R.id.ivPicture)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignup=findViewById(R.id.btnSignup)
        checkBox=findViewById(R.id.remember)
        var progressBar:ProgressBar=findViewById(R.id.progressbar)
        btnPicture.isEnabled=false
        sharedPreferences=getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE)
        editor=sharedPreferences.edit()
        val check = sharedPreferences.getBoolean(CHECK, false)
        if (logout)
            editor.remove(MY_PREFERENCE).commit()
        if (check)
        {
            val str_mail = sharedPreferences.getString(EMAIL, "-1")
            val str_token = sharedPreferences.getString(TOKEN, "-1")
            val str_passwords = sharedPreferences.getString(PASSWORD, "-1")
            val intent=Intent(this@HomePage,NavigatorDrawer::class.java)
            intent.putExtra(EMAIL,str_mail)
            intent.putExtra(TOKEN,str_token)
            intent.putExtra(PASSWORD,str_passwords)
            startActivity(intent)
            finish()
        }

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),requestCodeForCamera)

        }
        else
            btnPicture.isEnabled=true


        btnPicture.setOnClickListener {

            val list= arrayOf(GALLERY,CAMERA)
            val alertDialog:AlertDialog.Builder=AlertDialog.Builder(this).apply {
                setTitle("Choose One")
            }
            alertDialog.setItems(list) { dialogInterface, i ->
                if (i == 1) {
                    val intent1= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    getContentForCamera.launch(intent1)
                } else {
                    val intent2= Intent(Intent.ACTION_PICK)
                    intent2.type = IMAGE_TYPE
                    getContentForGallery.launch(intent2)
                }
            }
            alertDialog.create()
            alertDialog.show()
        }
        btnSignup.setOnClickListener {
            startActivity(Intent(this, UserRegisterActivity::class.java))
        }
        btnLogin.setOnClickListener {
            if (etPassword.text?.toString()?.length==0)
            {
                etPassword.setError(ERROR)
                etPassword.requestFocus()
            }
            else if (etMail.text?.toString()?.length==0)
            {
                etMail.setError(ERROR)
                etMail.requestFocus()
            }
            else
            {
                progressBar.visibility= View.VISIBLE
                email=etMail.text.toString()
                password=etPassword.text.toString()
                val rf: Retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitInterfaceLogin.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build()

                var API=rf.create(RetrofitInterfaceLogin::class.java)
                val obj: UserInfoLogin = UserInfoLogin(email,password)
                API.addUser(obj).enqueue(
                    object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(this@HomePage, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            val addedUser = response.body()
                            if (addedUser?.message=="success")
                            {
                                if (checkBox.isChecked)
                                {
                                    editor.putString(EMAIL,email);
                                    editor.putString(TOKEN,addedUser?.data?.Token);
                                    editor.putString(PASSWORD,password);
                                    editor.putBoolean(CHECK,userToken)
                                    editor.commit();
                                }
                                val intent=Intent(this@HomePage,NavigatorDrawer::class.java)
                                intent.putExtra(TOKEN,addedUser?.data?.Token)
                                intent.putExtra(EMAIL,email)
                                intent.putExtra(PASSWORD,password)
                                if (checkForBitMap)
                                {
                                    intent.putExtra(BITMAPIMAGE,imageFromCamera)
                                    intent.putExtra(CHECKFORBITMAP,true)
                                }
                                else
                                {
                                    intent.putExtra(IMAGEFROMGALLERY,imageFromGallery.toString())
                                    intent.putExtra(CHECKFORBITMAP,false)
                                }
                                val TAG1="User Token"
                                Log.i(TAG1,"The Token Is: $addedUser?.data?.Token")
                                startActivity(intent)
                                finish()
                            }
                            else
                                Toast.makeText(this@HomePage,NOT_LOGIN,Toast.LENGTH_SHORT).show()

                        }
                    }
                )

                //startActivity(intent)
                //finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCodeForCamera==requestCode && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            btnPicture.isEnabled=true
    }
}