package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：绘制三角形
 */
class SampleShape(context: Context) : BaseShape(context) {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0

    //数学坐标系
    private val vertexs = floatArrayOf(
        //正面
        -0.5f, 0.5f, 0.5f,//左上
        -0.5f, -0.5f, 0.5f,//左下
        0.5f, -0.5f, 0.5f,//右下
        0.5f, 0.5f, 0.5f, //右上
        //背面
        -0.5f, 0.5f, -0.5f,//左上
        -0.5f, -0.5f, -0.5f,//左下
        0.5f, -0.5f, -0.5f,//右下
        0.5f, 0.5f, -0.5f, //右上
        //顶面
        -0.5f, 0.5f, -0.5f,//左上
        -0.5f, 0.5f, 0.5f,//左下
        0.5f, 0.5f, 0.5f,//右下
        0.5f, 0.5f, -0.5f, //右上
        //底面
        -0.5f, -0.5f, -0.5f,//左上
        -0.5f, -0.5f, 0.5f,//左下
        0.5f, -0.5f, 0.5f,//右下
        0.5f, -0.5f, -0.5f, //右上
        //左侧面
        -0.5f, 0.5f, -0.5f,//左上
        -0.5f, -0.5f, -0.5f,//左下
        -0.5f, -0.5f, 0.5f,//右下
        -0.5f, 0.5f, 0.5f, //右上
        //右侧面
        0.5f, 0.5f, -0.5f,//左上
        0.5f, -0.5f, -0.5f,//左下
        0.5f, -0.5f, 0.5f,//右下
        0.5f, 0.5f, 0.5f //右上
    )
    //    private val vertexs = FloatArray(3*6)
    private var radius = 0.5f
    private val indexs = shortArrayOf(
        //正面
        0, 1, 2,
        0, 2, 3,
        //背面
        0 + 4 * 1, 1 + 4 * 1, 2 + 4 * 1,
        0 + 4 * 1, 2 + 4 * 1, 3 + 4 * 1,
        //顶面
        0 + 4 * 2, 1 + 4 * 2, 2 + 4 * 2,
        0 + 4 * 2, 2 + 4 * 2, 3 + 4 * 2,
        //底面
        0 + 4 * 3, 1 + 4 * 3, 2 + 4 * 3,
        0 + 4 * 3, 2 + 4 * 3, 3 + 4 * 3,
        //左侧面
        0 + 4 * 4, 1 + 4 * 4, 2 + 4 * 4,
        0 + 4 * 4, 2 + 4 * 4, 3 + 4 * 4,
        //右侧面
        0 + 4 * 5, 1 + 4 * 5, 2 + 4 * 5,
        0 + 4 * 5, 2 + 4 * 5, 3 + 4 * 5
        )
    private lateinit var indexBuffer: ShortBuffer

    // 颜色，rgba
    private var color = floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f)


    //成员变量
    private lateinit var colorBuffer: FloatBuffer//颜色缓冲
    private val COLOR_PER_VERTEX = 4 //向量维度
    private val vertexColorStride = COLOR_PER_VERTEX * 4 // 4*4=16
    private val colors = floatArrayOf(
        //正面
        1f, 1f, 0.0f, 1.0f,//黄
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f,//绿
        0.19607843f, 1.0f, 0.55098f, 1.0f,//绿
        //正面
        1f, 1f, 0.0f, 1.0f,//黄
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f,//绿
        0.19607843f, 1.0f, 0.55098f, 1.0f,//绿
        //正面
        1f, 1f, 0.0f, 1.0f,//黄
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f,//绿
        0.19607843f, 1.0f, 0.55098f, 1.0f,//绿
        //正面
        1f, 1f, 0.0f, 1.0f,//黄
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f,//绿
        0.19607843f, 1.0f, 0.55098f, 1.0f,//绿
        //正面
        1f, 1f, 0.0f, 1.0f,//黄
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f,//绿
        0.19607843f, 1.0f, 0.55098f, 1.0f,//绿
        //正面
        1f, 1f, 0.0f, 1.0f,//黄
        0.05882353f, 0.09411765f, 0.9372549f, 1.0f,//蓝
        0.19607843f, 1.0f, 0.02745098f, 1.0f,//绿
        0.19607843f, 1.0f, 0.55098f, 1.0f//绿
    )


    override fun setup() {


//        val startAngle = 90f
//        getVertexs(startAngle, 0)
//        getVertexs(startAngle, 1)
//        getVertexs(startAngle, 2)
//        getVertexs(startAngle, 3)
//        getVertexs(startAngle, 4)
//        getVertexs(startAngle, 5)


        //初始化顶点字节缓冲区
        vertexBuffer = asFloatBuffer(vertexs)
        indexBuffer = asShortBuffer(indexs)
        colorBuffer = asFloatBuffer(colors)
        val vertexShader = loadVertexShaderAssets(context, "\nattribute vec3 vPosition;\nuniform mat4 uMVPMatrix;\nattribute vec4 aColor;//顶点颜色\nvarying  vec4 vColor;//片元颜色\nvoid main(){\n    gl_Position = uMVPMatrix*vec4(vPosition,1);\n    vColor = aColor;//将顶点颜色传给片元\n}\n\n")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "\n\nprecision mediump float;\nvarying vec4 vColor;\nvoid main(){\n    gl_FragColor = vColor;\n}\n")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
    }

    private fun getVertexs(startAngle: Float, i: Int) {
        val angle = (startAngle + i * 60f) % 360f
        val x = cos(Math.toRadians(angle.toDouble())) * radius
        val y = sin(Math.toRadians(angle.toDouble())) * radius
        vertexs[0 + i * 3] = x.toFloat()
        vertexs[1 + i * 3] = y.toFloat()
        vertexs[2 + i * 3] = 0f
    }


    override fun draw(mvpMatrix: FloatArray?) {
        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program, "vPosition", vertexBuffer)
        glUseAttribute(program, "aColor", colorBuffer, stride = vertexColorStride)
        mvpMatrix?.let {
            val matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0)
        }
        //获取片元着色器的vColor成员的句柄
//        glUseUniform4v(program, "vColor", color)
        //绘制三角形
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexs.size / COORDS_VERTEX_3D)
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            indexs.size,
            GLES20.GL_UNSIGNED_SHORT,
            indexBuffer
        )
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}