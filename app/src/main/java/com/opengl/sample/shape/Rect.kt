package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：绘制矩形
 */
class Rect(context: Context) : BaseShape(context) {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0

    //数学坐标系
    private val vertexs = floatArrayOf(
        0.5f, 0.5f, 0.0f,
        -0.5f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    private val vertexCount = vertexs.size / COORDS_VERTEX_3D //顶点个数

    // 颜色，rgba
    private var color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)



    override fun setup() {
        //初始化顶点字节缓冲区
        vertexBuffer = asFloatBuffer(vertexs)
        val vertexShader = loadVertexShaderAssets(context, "normalshape.vert")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "normalshape.frag")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
    }


    override fun draw(mvpMatrix: FloatArray?) {
        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program, "vPosition", vertexBuffer)
        //获取片元着色器的vColor成员的句柄
        glUseUniform4v(program, "vColor", color)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}