package com.opengl.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opengl.sample.view.GLView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GLView(this))
    }
}
