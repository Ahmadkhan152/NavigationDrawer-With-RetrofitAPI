package com.example.navigation_drawer.Classes

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Activities.NavigatorDrawer
import com.example.navigation_drawer.Retrofit_Interfaces.InterfaceCustomDialog
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceDeleteObject
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceLogin
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceUpdateObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecyclerviewAdapter(
    var context: Context,
    val listOfUsers:Listofusers,
    val token:String,
    val interfaceCustomDialog: InterfaceCustomDialog,
    val pageNo:Int
) : RecyclerView.Adapter<RecyclerviewAdapter.viewHolder>()
{

    class viewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val tvID:TextView=itemView.findViewById(R.id.tvID)
        val tvName:TextView=itemView.findViewById(R.id.tvName)
        val tvMail:TextView=itemView.findViewById(R.id.tvEmail)
        val tvLocation:TextView=itemView.findViewById(R.id.tvLocation)
        val tvDate:TextView=itemView.findViewById(R.id.tvCreateDate)
        val btnEdit:ImageView=itemView.findViewById(R.id.btnEdit)
        val btnDelete:ImageView=itemView.findViewById(R.id.btnDelete)
        val ivPicture:ImageView=itemView.findViewById(R.id.ivPicture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var myView = LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false)
        return viewHolder(myView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.itemView.setOnClickListener {
           interfaceCustomDialog.launchActivity(position)
        }
        holder.tvID.text = listOfUsers.data[position].id.toString()
        holder.tvName.text = listOfUsers.data[position].name
        holder.tvMail.text = listOfUsers.data[position].email
        holder.tvLocation.text = listOfUsers.data[position].location
        holder.tvDate.text = listOfUsers.data[position].createdat
        Glide.with(context).load(listOfUsers.data[position].profilepicture)
            .into(holder.ivPicture)
        holder.btnEdit.setOnClickListener {
            interfaceCustomDialog.createDialog(position)
        }
        holder.btnDelete.setOnClickListener {

        }
    }
    override fun getItemCount(): Int {
        return listOfUsers.data.size

    }


}