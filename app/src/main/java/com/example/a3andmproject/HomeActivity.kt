package com.example.a3andmproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.a3andmproject.utils.Constant

class HomeActivity : AppCompatActivity() {
    private val apiButton: Button by lazy { findViewById<Button>(R.id.apiButton) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        apiButton.setOnClickListener {

            val queue = Volley.newRequestQueue(this)
            val url = Constant.URL

            val getRequest = object : StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // response
                    Log.d("Response", response)
                },
                Response.ErrorListener { error ->
                    Log.d("ERROR","error => " + error.toString())
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
                    return params
                }
            }

            queue.add(getRequest)
        }
    }
}