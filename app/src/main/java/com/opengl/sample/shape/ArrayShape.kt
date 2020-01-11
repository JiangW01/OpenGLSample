package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/8
 * 描述 ：glDrawArray简单封装
 *  绘制一些基本的简单图形
 */
class ArrayShape(
    context: Context,
    private val vertexs: FloatArray,
    private val colors: FloatArray,
    private val drawType: Int
) : BaseShape(context) {


    private lateinit var vertexBuffer: FloatBuffer

    private lateinit var colorBuffer: FloatBuffer

    private var program: Int = 0
    private var positionHandle: Int = 0
    private val COLOR_PER_VERTEX = 4 //向量维度
    private val vertexColorStride = COLOR_PER_VERTEX * 4 // 4*4=16


    override fun setup() {
        vertexBuffer = asFloatBuffer(vertexs)
        colorBuffer = asFloatBuffer(colors)
        val vertexShader = loadVertexShaderAssets(context, "\n\n\nattribute vec3 vPosition;\nuniform mat4 uMVPMatrix;\nattribute vec4 aColor;//顶点颜色\nvarying  vec4 vColor;//片元颜色\nvoid main(){\n    gl_Position = uMVPMatrix*vec4(vPosition,1);\n    vColor = aColor;//将顶点颜色传给片元\n    gl_PointSize=10.0;//设置点的大小，默认为0\n}\n\n")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "\n\nprecision mediump float;\nvarying vec4 vColor;\nvoid main(){\n    gl_FragColor = vColor;\n}")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
    }

    override fun draw(mvpMatrix: FloatArray?) {
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program, "vPosition", vertexBuffer)
        glUseAttribute(program, "aColor", colorBuffer, stride = vertexColorStride)
        mvpMatrix?.let {
            val matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0)
        }

        GLES20.glDrawArrays(drawType, 0, vertexs.size / COORDS_VERTEX_3D)
        GLES20.glLineWidth(5f)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}