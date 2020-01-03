package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/3
 * 描述 ：绘制点
 */
class Point(context: Context) :BaseShape(context){


    private lateinit var pointBuffer:FloatBuffer

    private val point = floatArrayOf(
        -0.5f,0.5f,0f
    )

    private var program = 0

    private val colors = floatArrayOf(1.0f,1.0f,1.0f,1.0f)

    override fun setup() {
        pointBuffer = asFloatBuffer(point)
        val vertexShader = loadVertexShaderAssets(context,"point.vert")
        val fragShader = loadFragShaderAssets(context,"point.frag")
        program = glCreateProgram(vertexShader,fragShader)
    }

    override fun draw(mvpMatrix: FloatArray?) {
        GLES20.glUseProgram(program)
        glUseAttribute(program,"vPosition",pointBuffer)
        glUseUniform4v(program,"vColor",colors)
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,1)
    }
}