package com.example.navigation_drawer.Activities

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Classes.LoginResponse
import com.example.navigation_drawer.Classes.UserInfoRegister
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceRegister
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


    lateinit var etName:TextInputEditText
    lateinit var etEmail:TextInputEditText
    lateinit var etPassword:TextInputEditText
    lateinit var btnSave:Button
    lateinit var ivPicture: ImageView
    lateinit var btnPicture: Button
    var imageFromGallery: Uri?=null
    var imageFromCamera: Bitmap?=null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var name:String=""
    var email:String=""
    var password:String=""
    var checkForBitMap:Boolean=false
    private val requestCodeForCamera:Int=789

class UserRegisterActivity : AppCompatActivity() {


    private val getContentForCamera=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            activityResult:ActivityResult?->
        if (activityResult?.resultCode== RESULT_OK)
        {
            imageFromCamera=activityResult.data?.getParcelableExtra<Bitmap>("data1")
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
        setContentView(R.layout.activity_user_register)


        etName =findViewById(R.id.etName)
        etEmail =findViewById(R.id.etEmail)
        etPassword =findViewById(R.id.etPassword)
        btnSave =findViewById(R.id.btnSave)
        btnPicture =findViewById(R.id.btnPicture)
        ivPicture =findViewById(R.id.ivPicture)
        sharedPreferences=getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE)
        editor=sharedPreferences.edit()
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), requestCodeForCamera)

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
        btnSave.setOnClickListener {

            if (etName.text?.length!! < 0)
            {
                etName.error = "Empty Strings Not Allowed"
                etName.requestFocus()
            }
            else if (etEmail.text?.length!!<0)
            {
                etEmail.error = "Empty Strings Not Allowed"
                etEmail.requestFocus()
            }
            else if(etPassword.text?.length!! <0)
            {
                etPassword.error = "Empty Strings Not Allowed"
                etPassword.requestFocus()
            }
            else
            {
                name = etName.text.toString()
                email = etEmail.text.toString()
                password = etPassword.text.toString()
                val alertDialog:AlertDialog.Builder=AlertDialog.Builder(this)
                alertDialog.setTitle("ALERT!")
                alertDialog.setMessage("Do You Want To Continue")
                alertDialog.setPositiveButton("YES") { a, b ->
                    val rf: Retrofit = Retrofit.Builder()
                        .baseUrl(RetrofitInterfaceRegister.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create()).build()

                    var API=rf.create(RetrofitInterfaceRegister::class.java)
                    val obj: UserInfoRegister = UserInfoRegister(name, email, password)
                    API.addUser(obj).enqueue(
                        object : Callback<LoginResponse> {
                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@UserRegisterActivity, FAIL+t.message, Toast.LENGTH_SHORT).show()
                            }

                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                val addedUser = response.body()
                                if (addedUser?.message == "success") {
                                    Toast.makeText(
                                        this@UserRegisterActivity,
                                        SUCCESSFUL,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //intent shift data
                                    val intent: Intent =
                                        Intent(this@UserRegisterActivity, NavigatorDrawer::class.java)
                                    intent.putExtra(NAME, name)
                                    intent.putExtra(EMAIL, email)
                                    intent.putExtra(PASSWORD, password)
                                    intent.putExtra(TOKEN, addedUser?.data?.Token)
                                    intent.putExtra(CHECKFORREGISTER, true)
                                    if (checkForBitMap)
                                        intent.putExtra(BITMAPIMAGE, imageFromCamera)
                                    else
                                        intent.putExtra(IMAGEFROMGALLERY, imageFromGallery.toString())
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                    finish()
                                }
                                else
                                    Toast.makeText(this@UserRegisterActivity, FAIL,Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    alertDialog.setNegativeButton("No") { a, b ->

                    }
                }
                alertDialog.create()
                alertDialog.show()
            }




            //Register User To Retrofit
        }


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCodeForCamera ==requestCode && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            btnPicture.isEnabled=true
    }
}