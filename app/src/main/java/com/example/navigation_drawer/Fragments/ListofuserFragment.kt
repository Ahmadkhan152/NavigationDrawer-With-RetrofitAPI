package com.example.navigation_drawer.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation_drawer.*
import com.example.navigation_drawer.Activities.DeleteUpdataActivity
import com.example.navigation_drawer.Activities.HomePage
import com.example.navigation_drawer.Classes.Listofusers
import com.example.navigation_drawer.Classes.MyCustomDialog
import com.example.navigation_drawer.Classes.MyIntercepter
import com.example.navigation_drawer.Classes.RecyclerviewAdapter
import com.example.navigation_drawer.Retrofit_Interfaces.InterfaceCustomDialog
import com.example.navigation_drawer.Retrofit_Interfaces.RetrofitInterfaceUserList
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ListofuserFragment(val token:String) : Fragment(),InterfaceCustomDialog {
    lateinit var recyclerview:RecyclerView
    var adapter: RecyclerviewAdapter?=null
    var isScrolling: Boolean = false
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0
    var pageNo:Int=1
    var users:Listofusers?=null
    var obj:Listofusers?=null
    lateinit var progressbar:ProgressBar
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->

        Toast.makeText(requireActivity(),"Hello",Toast.LENGTH_SHORT).show()
        if (result?.resultCode == Activity.RESULT_OK) {

            val position=result?.data?.getIntExtra(POSITION,-1)
            users?.data?.removeAt(position!!)
            adapter?.notifyDataSetChanged()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_listofuser, container, false)
        setHasOptionsMenu(true)
        progressbar=view.findViewById(R.id.progressbar)
        recyclerview=view.findViewById(R.id.recyclerview)
        recyclerview.layoutManager=LinearLayoutManager(context)
        val client= OkHttpClient.Builder().apply {
            addInterceptor(MyIntercepter(token))
        }.build()
        val rf: Retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInterfaceUserList.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        var API=rf.create(RetrofitInterfaceUserList::class.java)
        var call=API.listRepos("$pageNo")
        call.enqueue(

            object : Callback<Listofusers?> {
                override fun onFailure(call: Call<Listofusers?>, t: Throwable) {
                    Toast.makeText(context,FAIL+t.message,Toast.LENGTH_SHORT).show()
                }
                
                override fun onResponse(
                    call: Call<Listofusers?>,
                    response: Response<Listofusers?>
                ) {
                    Toast.makeText(context,"${token}",Toast.LENGTH_SHORT).show()
                    val TAG="TOEKN12"
                    Log.i(TAG,"The Token iS: ${token}")
                    progressbar.visibility = View.GONE
                    if (response.body()!=null)
                        users= response.body() as Listofusers
                    if (users!=null)
                        adapter= context?.let { RecyclerviewAdapter(it, users!!,token,this@ListofuserFragment,pageNo) }
                    recyclerview.adapter=adapter
                    recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                            super.onScrollStateChanged(recyclerView, newState)

                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true
                            }
                        }
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            currentItems = (recyclerView.layoutManager as LinearLayoutManager).childCount
                            totalItems = (recyclerView.layoutManager as LinearLayoutManager).itemCount
                            scrollOutItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            if (pageNo< users!!.total_pages)
                            {
                                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                                    progressbar.visibility=View.VISIBLE
                                    isScrolling = false
                                    pageNo++


                                    var call1=API.listRepos("$pageNo")
                                    call1.enqueue(
                                        object : Callback<Listofusers?> {
                                            override fun onFailure(
                                                call: Call<Listofusers?>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    context,
                                                    FAIL + t.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            override fun onResponse(
                                                call: Call<Listofusers?>,
                                                response: Response<Listofusers?>
                                            ) {
                                                progressbar.visibility=View.INVISIBLE
                                                if (response.body() != null)
                                                    users = response.body() as Listofusers
                                                if (users!=null)
                                                    adapter= context?.let { RecyclerviewAdapter(it, users!!,token,this@ListofuserFragment,pageNo) }
                                                adapter?.notifyDataSetChanged()
                                            }
                                        }
                                    )
                                }
                            }
                            else
                                Toast.makeText(context,PAGENOTFOUND,Toast.LENGTH_SHORT).show()
                        }

                    })

                }
            }
        )
        return view
    }
    override fun createDialog(position:Int) {
        val obj=MyCustomDialog(true,users!!,position,token)
        obj.show(parentFragmentManager, TAG)
    }

    override fun launchActivity(position:Int) {
        val intent=Intent(context, DeleteUpdataActivity::class.java)
        Toast.makeText(context,"${users!!.data[position].name}",Toast.LENGTH_SHORT).show()
        intent.putExtra(ID,users!!.data[position].id)
        intent.putExtra(NAME,users!!.data[position].name)
        intent.putExtra(EMAIL,users!!.data[position].email)
        intent.putExtra(TOKEN,token)
        intent.putExtra(POSITION,position)
        intent.putExtra(LOCATION,users!!.data[position].location)
        intent.putExtra(DATE,users!!.data[position].createdat)
        intent.putExtra(PICTURE,users!!.data[position].profilepicture)
        getContent.launch(intent)
    }

    override fun updateEntry() {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(MyIntercepter(token))
        }.build()
        val rf: Retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInterfaceUserList.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        var API = rf.create(RetrofitInterfaceUserList::class.java)
        var call = API.listRepos("$pageNo")
        call.enqueue(

            object : Callback<Listofusers?> {
                override fun onFailure(call: Call<Listofusers?>, t: Throwable) {
                    Toast.makeText(context, FAIL + t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Listofusers?>,
                    response: Response<Listofusers?>
                ) {
                    if (response.body() != null)
                        users = response.body() as Listofusers
                    if (users != null)
                        adapter = context?.let {
                            RecyclerviewAdapter(
                                it,
                                users!!,
                                token,
                                this@ListofuserFragment,
                                pageNo
                            )
                        }
                    recyclerview.adapter = adapter

                }
            }
        )
    }

    private fun onLogin() {
        val obj=Listofusers(0,0,0,0, arrayListOf())
        MyCustomDialog(false, obj,-1, token).show(parentFragmentManager, TAG)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.addbutton,menu)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.btnAdd){
            onLogin()
        }
        val alertDialog: AlertDialog.Builder= AlertDialog.Builder(context)
        if (item.itemId==R.id.btnLogout){
            alertDialog.setTitle("ALERT!")
            alertDialog.setMessage("Do You Want To Continue")
            alertDialog.setPositiveButton("YES") { a, b ->
                val sharedPreferences:SharedPreferences=requireContext().getSharedPreferences(MY_PREFERENCE,
                    Context.MODE_PRIVATE
                )
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