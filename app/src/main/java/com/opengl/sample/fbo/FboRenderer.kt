package com.opengl.sample.fbo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.opengl.sample.shape.*
import com.opengl.sample.texture.ITextureShape
import com.opengl.sample.texture.TextureRender
import com.opengl.sample.view.EGLSurfaceView
import java.lang.Math.min
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：
 */
class FboRenderer(context: Context) : EGLSurfaceView.Renderer {


    private var fboShape: FboTextureRender = FboTextureRender(context)

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private var rotate = 0f

    override fun onSurfaceCreated() {
        println("EGLRenderer onSurfaceCreated e")
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        fboShape.setup()
        println("EGLRenderer onSurfaceCreated x")
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        println("EGLRenderer onSurfaceChanged")
        GLES20.glViewport(0, 0, width, height)
        val aspectRatio =  width * 1.0f / height
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 2f, 7f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 4f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    override fun onDrawFrame() {
        println("EGLRenderer onDrawFrame")
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT )
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvpMatrix, 0)
        //FBO处理
        fboShape.draw(mvpMatrix,0)
        //通过FBO处理之后，拿到纹理id，然后渲染
        rotate++
        if(rotate == 360f){
            rotate = 0f
        }
    }


}