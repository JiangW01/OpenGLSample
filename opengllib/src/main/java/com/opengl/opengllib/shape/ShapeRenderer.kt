package com.opengl.opengllib.shape

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.opengl.opengllib.shape.shapes.Line
import com.opengl.opengllib.shape.shapes.Point
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/15
 * 描述 ：
 */
class ShapeRenderer(resources: Resources) :GLSurfaceView.Renderer {


    var shape:BaseShape = Line(resources)

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        shape.render()
    }
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0,width,height)
        shape.onSurfaceSizeChange(width,height)
    }
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        shape.create()
    }
}