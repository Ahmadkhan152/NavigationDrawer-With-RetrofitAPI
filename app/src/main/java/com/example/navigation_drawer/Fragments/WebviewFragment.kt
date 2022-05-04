package com.example.navigation_drawer.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.navigation_drawer.Activities.HomePage
import com.example.navigation_drawer.Activities.UserRegisterActivity
import com.example.navigation_drawer.CHECK
import com.example.navigation_drawer.LOGOUT
import com.example.navigation_drawer.MY_PREFERENCE
import com.example.navigation_drawer.R

class WebviewFragment : Fragment() {
    lateinit var webview: WebView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_webview, container, false)
        setHasOptionsMenu(true)
        webview=view.findViewById(R.id.webview)

        webview.webViewClient= WebViewClient()
        webview.apply{
            loadUrl("https://surveyauto.com/")
            settings.javaScriptEnabled=true
            settings.safeBrowsingEnabled=true
        }



        return view
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