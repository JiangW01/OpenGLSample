package com.opengl.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opengl.sample.fbo.FboRenderer
import com.opengl.sample.render.EGLRenderer
import com.opengl.sample.view.EGLSurfaceView
import com.opengl.sample.view.GLTextureView
import com.opengl.sample.view.GLView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(GLTextureView(this))
//        setContentView(GLView(this))
    }


}
