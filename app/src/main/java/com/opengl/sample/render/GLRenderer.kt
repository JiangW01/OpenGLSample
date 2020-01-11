package com.opengl.sample.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.opengl.sample.shape.*
import java.lang.Math.min
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：
 */
class GLRenderer(context: Context) : GLSurfaceView.Renderer {

    //    var shape: BaseShape = Cube(context)
    //    var shape: BaseShape = Circle(context)
//    var shape: BaseShape = Rect(context)
    var shape: BaseShape = Triangle(context)
//    var shape: BaseShape = TextureRect(context)
//    var shape: BaseShape = SampleShape(context)
//    var shape: BaseShape = Point(context)
//    var shape: BaseShape = Line(context)

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private var rotate = 0f

    override fun onDrawFrame(gl: GL10?) {
        println("GLRenderer onDrawFrame")
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT )
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
//        Matrix.setRotateM(modelMatrix,0,rotate,0f,1f,0f)
//        Matrix.translateM(modelMatrix,0,0f,0f,rotate/90f)
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)
        shape.draw(mvpMatrix)
        rotate++
        if(rotate == 360f){
            rotate = 0f
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        println("GLRenderer onSurfaceChanged")
//        val side = min(width, height)
//        val x = (width - side) / 2
//        val y = (height - side) / 2
//        GLES20.glViewport(x, y, side, side)
        GLES20.glViewport(0, 0, width, height)
        val aspectRatio =  max(width,height) * 1.0f / min(width,height)
        println("width = $width height = $height aspectRatio = $aspectRatio")
        if (width > height) {
            Matrix.orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,0f,10f)
        } else {
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,0f,10f)
        }

//        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 2f, 7f)

//        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

//        Matrix.perspectiveM(projectionMatrix, 0, 90f, aspectRatio, 1f, 10f)
//

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        shape.setup()
    }
}