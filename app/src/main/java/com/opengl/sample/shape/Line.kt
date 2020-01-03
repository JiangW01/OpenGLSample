package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/3
 * 描述 ：绘制线
 */
class Line(context: Context) : BaseShape(context) {


    private lateinit var databuffer: FloatBuffer

    private val line = floatArrayOf(
        0.2f, 0.2f,
        0.2f, 0.8f,
        0.8f, 0.8f,
        0.8f, 0.2f
    )


    private val vertexCount = line.size / COORDS_VERTEX_2D

    private var program = 0
    private var positionHandle = 0
    private var colorHandle = 0

    private val colors = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)

    override fun setup() {
        databuffer = asFloatBuffer(line)
        val vertexShader = loadVertexShaderAssets(context, "line.vert")
        val fragShader = loadFragShaderAssets(context, "line.frag")
        program = glCreateProgram(vertexShader, fragShader)
        colorHandle = GLES20.glGetUniformLocation(program, "vColor")
    }

    override fun draw(mvpMatrix: FloatArray?) {
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program,"vPosition", databuffer,size = COORDS_VERTEX_2D)
        colorHandle = glUseUniform4v(program,"vColor",colors)
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertexCount)
        GLES20.glLineWidth(10f)
        //禁止顶点数组的句柄
        GLES20.glDisableVertexAttribArray(positionHandle)

    }
}