package com.opengl.sample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opengl.sample.R
import com.opengl.sample.ui.shape.ShapeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnDrawShape.setOnClickListener {
            startActivity(Intent(this, ShapeActivity::class.java))
        }
    }







}
