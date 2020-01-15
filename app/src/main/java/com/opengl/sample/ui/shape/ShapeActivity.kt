package com.opengl.sample.ui.shape

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.opengl.opengllib.shape.ShapeRenderer
import com.opengl.opengllib.shape.shapes.Point
import com.opengl.sample.R
import kotlinx.android.synthetic.main.activity_shape.*

class ShapeActivity : AppCompatActivity() {


    private var shapes = arrayListOf<ShapeType>(
        ShapeType.POINT,
        ShapeType.LINE
    )

    private lateinit var shapeRenderer: ShapeRenderer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shape)
        spinner.onItemSelectedListener  = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                val shapeType = shapes[position]
//                when(shapeType){
//                    ShapeType.POINT->{
//                        shapeRenderer.shape = Point(this@ShapeActivity.resources)
//                        glSurfaceView.requestRender()
//                    }
//                    ShapeType.LINE->{
//                        shapeRenderer.shape = Point(this@ShapeActivity.resources)
//                        glSurfaceView.requestRender()
//                    }
//                }
//                shapeRenderer
            }
        }
        glSurfaceView.setEGLContextClientVersion(2)
        shapeRenderer = ShapeRenderer(this.resources)
        glSurfaceView.setRenderer(shapeRenderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        glSurfaceView.requestRender()
    }
}
