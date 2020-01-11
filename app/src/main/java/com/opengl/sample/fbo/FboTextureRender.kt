package com.opengl.sample.fbo

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.util.Log
import com.opengl.sample.texture.ITextureShape
import com.opengl.sample.utils.*
import java.nio.*

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：绘制纹理
 */
class FboTextureRender(private val context: Context) : ITextureShape {


    //顶点缓冲
    private lateinit var vertexBuffer: FloatBuffer

    private var program = 0
    private var positionHandle = 0

    //数学坐标系
    private val vertexs = floatArrayOf(
        -1f, -1f, 0.0f, //bottom,left
        1f, -1f, 0.0f,//bottom,right
        -1f, 1f, 0.0f,//top,left
        1f, 1f, 0.0f//top,right
    )

    //成员变量
    private lateinit var textureCooBuffer: FloatBuffer//颜色缓冲
    private val  TEXTURE_PER_VERTEX = 2 //数组中每个顶点的坐标数
    private val vertexColorStride = TEXTURE_PER_VERTEX * 4 // 4*4=16
    //fbo 纹理坐标
    private val textureCoods = floatArrayOf(
        0f, 0f,  //bottom,left
        1f, 0f,//bottom,right
        0f, 1f,//top,left
        1f, 1f//top,right
    )

    private var fboHelper :FBOHelper? = null
    private var imageTextureId = 0

    override fun setup() {
        //初始化顶点字节缓冲区
        vertexBuffer = asFloatBuffer(vertexs)
        textureCooBuffer = asFloatBuffer(textureCoods)
        val vertexShader = loadVertexShaderAssets(context, "\nattribute vec3 vPosition;\nuniform mat4 uMVPMatrix;\nattribute vec2 aTextureCoordinates;\nvarying vec2 vTextureCoordinates;\nvoid main(){\n    gl_Position = uMVPMatrix*vec4(vPosition,1);\n    vTextureCoordinates = aTextureCoordinates;\n}\n\n")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "\n\nprecision mediump float;\nuniform sampler2D uTextureUnit;\nvarying vec2 vTextureCoordinates;\nvoid main(){\n    gl_FragColor = texture2D(uTextureUnit,vTextureCoordinates);\n}\n")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
       if (program>= 0){
           val inputStream = context.assets.open("")
           val bitmap = BitmapFactory.decodeStream(inputStream)
           if (bitmap == null) {
               Log.e("GL", "Could not load bitmap")
               return
           }
           fboHelper = FBOHelper(bitmap)
           fboHelper?.createFBO()
           imageTextureId  = loadTextureFromBitmap(bitmap)
       }

    }




    override fun draw(mvpMatrix: FloatArray?,textureId:Int) {
        //绑定fbo
        fboHelper?.bindFramebuffer()

        // 将程序添加到OpenGL ES环境中
        GLES20.glUseProgram(program)

        //绑定渲染纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, imageTextureId)

        positionHandle = glUseAttribute(program, "vPosition", vertexBuffer)
        glUseAttribute(program, "aTextureCoordinates", textureCooBuffer, stride = vertexColorStride)
        mvpMatrix?.let {
            val matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0)
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,vertexs.size/ COORDS_VERTEX_3D)

        GLES20.glDisableVertexAttribArray(positionHandle)

        //解绑纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        //解绑fbo
        fboHelper?.unbindFramebuffer()

    }

    fun getFboTextureId():Int{
        return fboHelper?.getFboTextureId() ?:0
    }
}