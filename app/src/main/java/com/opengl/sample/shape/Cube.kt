package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：绘制立方体
 */
class Cube(context: Context) : BaseShape(context) {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0

    //数学坐标系
    private val vertexs = floatArrayOf(
        0.5f, 0.5f, 0.5f,
        -0.5f, 0.5f, 0.5f,
        -0.5f, -0.5f, 0.5f,
        0.5f, -0.5f, 0.5f,
        0.5f, 0.5f, -0.5f,
        -0.5f, 0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        0.5f, -0.5f, -0.5f
    )

    private val vertexCount = vertexs.size / COORDS_VERTEX_3D //顶点个数

    // 颜色，rgba
    private var color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)



    override fun setup() {
        //初始化顶点字节缓冲区
        vertexBuffer = asFloatBuffer(vertexs)
        val vertexShader = loadVertexShaderAssets(context, "\n//普通的着色器代码\nattribute vec4 vPosition;\nuniform mat4 uMVPMatrix;\nvoid main(){\n    gl_Position = uMVPMatrix*vPosition;\n}\n\n//顶点颜色传递给片元颜色\n//attribute vec3 vPosition; //顶点坐标\n//attribute vec4 aColor;//顶点颜色\n//varying  vec4 vColor;//片元颜色\n//void main() {\n//  gl_Position = vec4(vPosition,1);\n// vColor = aColor;//将顶点颜色传给片元\n//}\n\n//带矩阵顶点颜色传递给片元颜色\n//attribute vec3 vPosition; //顶点坐标\n//uniform mat4 uMVPMatrix; //总变换矩阵\n//attribute vec4 aColor;//顶点颜色\n//varying  vec4 vColor;//片元颜色\n//void main() {\n // gl_Position = uMVPMatrix*vec4(vPosition,1);\n // vColor = aColor;//将顶点颜色传给片元\n//}")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "\n\n//纯色三角形\nprecision mediump float;\nuniform vec4 vColor;\nvoid main(){\n    gl_FragColor = vColor;\n}\n\n//彩色三角形\n//precision mediump float;\n//varying vec4 vColor;\n//void main(){\n//    gl_FragColor = vColor;\n//}")//片元着色
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexCount)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}