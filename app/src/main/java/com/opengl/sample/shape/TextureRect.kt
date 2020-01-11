package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.*

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：绘制三角形
 */
class TextureRect(context: Context) : BaseShape(context) {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0

    //数学坐标系
    private val vertexs = floatArrayOf(
        //正面
        -1f, 1f, 0.0f,
        -1f, -1f, 0.0f,
        1f, -1f, 0.0f,
        -1f, 1f, 0.0f,
        1f, 1f, 0.0f,
        1f, -1f, 0.0f
    )
//    private val indexs = shortArrayOf(
//        //正面
//        0, 1, 2,
//        0, 2, 3)
//    private lateinit var indexBuffer: ShortBuffer


    private var s = 1f//s纹理坐标系数
    private var t = 1f//t纹理坐标系数

    //成员变量
    private lateinit var textureCooBuffer: FloatBuffer//颜色缓冲
    private var textureId = 0
    private val  TEXTURE_PER_VERTEX = 2 //数组中每个顶点的坐标数
    private val vertexColorStride = TEXTURE_PER_VERTEX * 4 // 4*4=16
    private val textureCoods = floatArrayOf(
        0f,0f,
        0f,t,
        s,t,
        0f,0f,
        s,0f,
        s,t
    )


    override fun setup() {
        //初始化顶点字节缓冲区
        vertexBuffer = asFloatBuffer(vertexs)
//        indexBuffer = asShortBuffer(indexs)
        textureCooBuffer = asFloatBuffer(textureCoods)
        textureId = loadTextureFromAssets(context,"",GLES20.GL_REPLACE,GLES20.GL_REPLACE)
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
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureId)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexs.size/ COORDS_VERTEX_3D)

//        GLES20.glDrawElements(
//            GLES20.GL_TRIANGLES,
//            indexs.size,
//            GLES20.GL_UNSIGNED_SHORT,
//            indexBuffer
//        )
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}