package com.example.navigation_drawer.Classes

import android.net.wifi.p2p.WifiP2pDevice.FAILED
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.navigation_drawer.ERROR
import com.example.navigation_drawer.FAIL
import com.example.navigation_drawer.R
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceDeleteObject
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceUpdateObject
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceUserCreated
import com.example.navigation_drawer.SUCCESSFUL
import com.google.android.material.textfield.TextInputEditText
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyCustomDialog(val checkForEdit:Boolean,var list:Listofusers,val position:Int,val token:String):DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.round_corner);
        val view= inflater.inflate(R.layout.design_customdialog,container,false)

        val etName:TextInputEditText=view.findViewById(R.id.etName)
        val etEmail:TextInputEditText=view.findViewById(R.id.etEmail)
        val etLocation:TextInputEditText=view.findViewById(R.id.etLocation)
        val btnSaveForCreate:Button=view.findViewById(R.id.btnSaveForCreate)
        val btnSaveForEdit:Button=view.findViewById(R.id.btnSaveForEdit)
        val btnClear:Button=view.findViewById(R.id.btnClear)
        if (checkForEdit)
        {
            btnSaveForEdit.visibility=View.VISIBLE
            btnSaveForCreate.visibility=View.INVISIBLE
            btnClear.visibility=View.VISIBLE
            etName.setText(list.data[position].name)
            etEmail.setText(list.data[position].email)
            etLocation.setText(list.data[position].location)
        }
        else
        {
            btnClear.visibility=View.INVISIBLE
            btnSaveForEdit.visibility=View.INVISIBLE
            btnSaveForCreate.visibility=View.VISIBLE
        }
        var name:String=""
        var email:String=""
        var location:String=""

        btnClear.setOnClickListener {
            etName.text?.clear()
            etEmail.text?.clear()
            etLocation.text?.clear()
        }
        btnSaveForEdit.setOnClickListener {
            if (etName.text?.length==0)
            {
                etName.error= ERROR
                etName.requestFocus()
            }
            else if(etEmail.text?.length==0)
            {
                etEmail.error=ERROR
                etEmail.requestFocus()
            }
            else if(etLocation.text?.length==0)
            {
                etLocation.error= ERROR
                etLocation.requestFocus()
            }
            else {
                name = etName.text.toString()
                email = etEmail.text.toString()
                location = etLocation.text.toString()


                val client= OkHttpClient.Builder().apply {
                    addInterceptor(MyIntercepter(token))
                }.build()
                val rf: Retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitInterfaceUpdateObject.baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build()

                var API=rf.create(RetrofitInterfaceUpdateObject::class.java)
                val obj=UserInfoUpdate(list.data[position].id,name,email,location)
                API.updateUser(list.data[position].id,obj).enqueue(
                    object : Callback<Userinformation> {
                        override fun onFailure(call: Call<Userinformation>, t: Throwable) {
                            Toast.makeText(context, FAIL+t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<Userinformation>,
                            response: Response<Userinformation>
                        ) {
                            val addedUser = response.body()

                            Toast.makeText(context, SUCCESSFUL, Toast.LENGTH_SHORT).show()
                            dialog?.cancel()
                        }
                    }
                )
            }
        }
        btnSaveForCreate.setOnClickListener {
            if (etName.text?.length==0)
            {
                etName.error= ERROR
                etName.requestFocus()
            }
            else if(etEmail.text?.length==0)
            {
                etEmail.error=ERROR
                etEmail.requestFocus()
            }
            else if(etLocation.text?.length==0)
            {
                etLocation.error= ERROR
                etLocation.requestFocus()
            }
            else
            {
                name=etName.text.toString()
                email=etEmail.text.toString()
                location=etLocation.text.toString()



                val rf: Retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitInterfaceUserCreated.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build()

                var API=rf.create(RetrofitInterfaceUserCreated::class.java)
                val obj: UserCreatedInfo = UserCreatedInfo(name,email,location)
                API.addUser(obj).enqueue(
                    object : Callback<Userinformation> {
                        override fun onFailure(call: Call<Userinformation>, t: Throwable) {
                            Toast.makeText(context, FAIL+t.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(
                            call: Call<Userinformation>,
                            response: Response<Userinformation>
                        ) {
                            Toast.makeText(context, SUCCESSFUL,Toast.LENGTH_SHORT).show()
                            dialog?.cancel()
                        }
                    }
                )


            }
        }


        return view
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    fun createDialog()
    {
        dialog?.create()
        dialog?.show()
    }


}