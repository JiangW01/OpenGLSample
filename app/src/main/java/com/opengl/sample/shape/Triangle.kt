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
 * 描述 ：绘制三角形
 */
class Triangle(context: Context) : BaseShape(context) {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0

    //数学坐标系
    private val vertexs = floatArrayOf(
        0.0f, 0.5f, 0.0f,//顶点
        -0.5f, -0.5f, 0.0f,//左下
        0.5f, -0.5f, 0.0f //右下
    )

    private val vertexCount = vertexs.size / COORDS_VERTEX_3D //顶点个数

    // 颜色，rgba
    private var color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)


    //成员变量
//    private lateinit var  colorBuffer:FloatBuffer//颜色缓冲
//    private val COLOR_PER_VERTEX = 4 //向量维度
//    private val vertexColorStride = COLOR_PER_VERTEX * 4 // 4*4=16
//    private val colors = floatArrayOf(
//        1f, 0f, 0.0f, 1.0f,//红
//        0f, 1.0f,0.0f, 1.0f,//绿
//        0.0f, 0.0f,1.0f, 1.0f//蓝
//    )


    override fun setup() {
        //初始化顶点字节缓冲区
        vertexBuffer = asFloatBuffer(vertexs)
        val vertexShader = loadVertexShaderAssets(context, "triangle.vert")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "triangle.frag")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
    }


    override fun draw(mvpMatrix: FloatArray?) {
        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program, "vPosition", vertexBuffer)
        mvpMatrix?.let {
            val matrixHandle = GLES20.glGetUniformLocation(program,"uMVPMatrix")
            GLES20.glUniformMatrix4fv(matrixHandle,1,false,mvpMatrix,0)
        }
        //获取片元着色器的vColor成员的句柄
        glUseUniform4v(program, "vColor", color)
        //绘制三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}