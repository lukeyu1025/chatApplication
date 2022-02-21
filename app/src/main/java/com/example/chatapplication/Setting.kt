package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Setting : AppCompatActivity() {
    private  lateinit var toolbar:androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toolbar=findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener{
            finish()
        }
    }
}