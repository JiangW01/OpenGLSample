package com.opengl.sample.render

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.opengl.sample.matrix.MatrixStack
import com.opengl.sample.shape.*
import java.lang.Math.min
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：
 */
class GLWorldRenderer(context: Context) : GLSurfaceView.Renderer {

    var world: BaseShape = WorldShape(context)

    private val matrixStack:MatrixStack = MatrixStack()

    private var rotate = 0f

    override fun onDrawFrame(gl: GL10?) {
        println("GLRenderer onDrawFrame")
        //清除颜色缓存和深度缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        matrixStack.save()
//        matrixStack.save()
//        matrixStack.translateM(-1.5f)
//        matrixStack.scaleM(0.5f,1f,0.5f)
//        matrixStack.rotateM(rotate,y = 1f)
//        world.draw(matrixStack.getResult())
//        matrixStack.getModelMatrix().forEachIndexed { index, fl ->
//            println("index = $index, value = $fl")
//        }
//        matrixStack.restore()
//        matrixStack.getModelMatrix().forEachIndexed { index, fl ->
//            println("index = $index, value = $fl")
//        }
//        matrixStack.translateM(1.5f)
        matrixStack.rotateM(rotate, y = 1f)
        world.draw(matrixStack.getResult())
        matrixStack.restore()
        rotate++
        if(rotate == 360f){
            rotate = 0f
        }
        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        println("GLRenderer onSurfaceChanged")
        GLES20.glViewport(0, 0, width, height)
        val aspectRatio =  width * 1.0f / height
        matrixStack.frustumM(aspectRatio,2f,17f)
        matrixStack.setLookAtM(2f,2f,10f)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        world.setup()
    }
}