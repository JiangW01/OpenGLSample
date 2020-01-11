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
 * 描述 ：圆锥提
 */
class TextureCone(context: Context) : BaseShape(context) {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0


    private lateinit var vertexs: FloatArray


    //成员变量
    private lateinit var textureCooBuffer: FloatBuffer//颜色缓冲
    private var textureId = 0
    private val TEXTURE_PER_VERTEX = 2 //数组中每个顶点的坐标数
    private val vertexColorStride = TEXTURE_PER_VERTEX * 4 // 4*4=16
    private lateinit var textureCoods: FloatArray

    private val radius = 0.4f
    private val splitCount = 360 //底面圆分割个数

    override fun setup() {
        //初始化顶点字节缓冲区
        val vertexCount = (splitCount + 1) * 2
        val singleAngle = 360f / splitCount
        vertexs = FloatArray(vertexCount * 3)
        //圆锥的顶点
        vertexs[0] = 0f
        vertexs[1] = 1.5f
        vertexs[2] = 0f
        for (i in 1..(splitCount + 1)) {
            val angle = (i-1) * singleAngle
            val x = cos(Math.toRadians(angle.toDouble())) * radius
            val z = -sin(Math.toRadians(angle.toDouble())) * radius
            vertexs[i * 3+0] = x.toFloat()
            vertexs[i * 3+1] = 0f
            vertexs[i * 3+2] = z.toFloat()

        }

        textureCoods = FloatArray(vertexCount * 2)
        val offset = 1f / splitCount
        textureCoods[0] = 0f
        textureCoods[1] = 0f
        for (i in 1..(splitCount + 1)) {
            val s = (i-1) * offset
            val t = 1f
            textureCoods[i * 2] = s
            textureCoods[i * 2 + 1] = t
        }

        vertexBuffer = asFloatBuffer(vertexs)
        textureCooBuffer = asFloatBuffer(textureCoods)
        textureId = loadTextureFromAssets(context, "")
        val vertexShader = loadVertexShaderAssets(context, "\nattribute vec3 vPosition;\nuniform mat4 uMVPMatrix;\nattribute vec2 aTextureCoordinates;\nvarying vec2 vTextureCoordinates;\nvoid main(){\n    gl_Position = uMVPMatrix*vec4(vPosition,1);\n    vTextureCoordinates = aTextureCoordinates;\n}\n\n")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "\n\nprecision mediump float;\nuniform sampler2D uTextureUnit;\nvarying vec2 vTextureCoordinates;\nvoid main(){\n    gl_FragColor = texture2D(uTextureUnit,vTextureCoordinates);\n}\n")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
    }


    override fun draw(mvpMatrix: FloatArray?) {
        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program, "vPosition", vertexBuffer)
        glUseAttribute(program, "aTextureCoordinates", textureCooBuffer, stride = vertexColorStride)
        mvpMatrix?.let {
            val matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0)
        }
        GLES20.glActiveTexture(textureId)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vertexs.size / COORDS_VERTEX_3D)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}