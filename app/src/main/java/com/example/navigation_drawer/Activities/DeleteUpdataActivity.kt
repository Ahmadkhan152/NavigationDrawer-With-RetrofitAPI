package com.example.navigation_drawer.Activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Classes.MyIntercepter
import com.example.navigation_drawer.Classes.Userinformation
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceDeleteObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DeleteUpdataActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_updata)


        val tvID: TextView =findViewById(R.id.tvID)
        val tvName: TextView =findViewById(R.id.tvName)
        val tvMail: TextView =findViewById(R.id.tvEmail)
        val tvLocation: TextView =findViewById(R.id.tvLocation)
        val tvDate: TextView =findViewById(R.id.tvCreateDate)
        val btnDelete: Button =findViewById(R.id.btnDelete)
        val ivPicture: ImageView =findViewById(R.id.ivPicture)

        val id=intent.getIntExtra(ID,-1)
        val position=intent.getIntExtra(POSITION,-1)
        val name=intent.getStringExtra(NAME)
        val email=intent.getStringExtra(EMAIL)
        val date=intent.getStringExtra(DATE)
        val location=intent.getStringExtra(LOCATION)
        val picture=intent.getStringExtra(PICTURE)
        val token=intent.getStringExtra(TOKEN)
        tvID.text = id.toString()
        tvName.text = name
        tvMail.text = email
        tvLocation.text = location
        tvDate.text = date
        Glide.with(this).load(picture)
            .into(ivPicture)
        btnDelete.setOnClickListener {
            val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
            alertDialog.setTitle("Alert")
            alertDialog.setMessage("Are You Sure")
            alertDialog.setPositiveButton("Yes") { dialog, which ->

                val client = OkHttpClient.Builder().apply {
                    addInterceptor(MyIntercepter(token!!))
                }.build()
                val rf: Retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitInterfaceDeleteObject.baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build()

                var API = rf.create(RetrofitInterfaceDeleteObject::class.java)
                API.deleteUser(id).enqueue(
                    object : Callback<Userinformation> {
                        override fun onFailure(call: Call<Userinformation>, t: Throwable) {
                            Toast.makeText(this@DeleteUpdataActivity, FAIL + t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<Userinformation>,
                            response: Response<Userinformation>
                        ) {
                            val addedUser = response.body()
                            Toast.makeText(this@DeleteUpdataActivity, SUCCESSFUL, Toast.LENGTH_SHORT).show()
                            val intent=Intent()

                            intent.putExtra(POSITION,position)
                            setResult(RESULT_OK,intent)
                            finish()
                        }
                    }
                )
            }
            alertDialog.setNegativeButton("No") { dialog, which ->
                finish()
            }
            alertDialog.create()
            alertDialog.show()
        }
    }
}