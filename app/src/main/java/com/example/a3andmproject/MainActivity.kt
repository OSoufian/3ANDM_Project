package com.example.a3andmproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timer = Timer(false).schedule(object: TimerTask(){
            override fun run() {
                val intentHome = Intent(this@MainActivity, HomeActivity::class.java);
                startActivity(intentHome);
            }
        }, 2000);

        
    }
}