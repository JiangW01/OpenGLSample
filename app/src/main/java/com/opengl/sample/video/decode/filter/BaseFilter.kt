package com.opengl.sample.video.decode.filter

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.opengl.sample.video.decode.utils.getOriginalTextureCo
import com.opengl.sample.video.decode.utils.getOriginalVertexCo
import com.opengl.sample.video.decode.utils.setIdentityM
import com.video.decode.utils.*
import java.nio.FloatBuffer


open class BaseFilter(
    private val vertexShaderSource: String = BASE_VERTEX_SHADER,
    private val fragmentShaderSource: String = BASE_FRAGMENT_SHADER
) {


    companion object {
        const val BASE_VERTEX_SHADER = """
        attribute vec4 vPosition;
        attribute vec4 vTexCoordinate;
        uniform mat4 postionTransform;
        uniform mat4 textureTransform;
        varying vec2 v_TexCoordinate;
        void main () {
            v_TexCoordinate = (textureTransform * vTexCoordinate).xy;
            gl_Position = postionTransform*vPosition;
        }
        """

        const val BASE_FRAGMENT_SHADER = """
        #extension GL_OES_EGL_image_external : require
        precision highp float;
        uniform samplerExternalOES texture;
        varying highp vec2 v_TexCoordinate;
        void main () {
            vec4 color = texture2D(texture, v_TexCoordinate);
            gl_FragColor = color;
        }
        """
    }

    //颜色矩阵
    var textureMatrix = FloatArray(16)
    //顶点矩阵
    var positionMatrix = FloatArray(16)

    private val vertexCoords = getOriginalVertexCo()


    // 纹理坐标
    private val textureCoords = getOriginalTextureCo()

    private var textureBuffer: FloatBuffer


    private var vertexBuffer: FloatBuffer


    var positionAttr = 0
    var textureCoordinateAttr = 0
    var textureUniform = 0
    var textureTransformUniform = 0
    var positionTransformUniform = 0

    private var program = 0

    init {
        setIdentityM(textureMatrix)
        setIdentityM(positionMatrix)
        vertexBuffer = asFloatBuffer(vertexCoords)
        textureBuffer = asFloatBuffer(textureCoords)
    }


    fun create() {
        program = glCreateProgram(vertexShaderSource, fragmentShaderSource)
        positionAttr = GLES20.glGetAttribLocation(program, "vPosition")
        checkLocation(positionAttr, "check vPosition")
        textureCoordinateAttr = GLES20.glGetAttribLocation(program, "vTexCoordinate")
        checkLocation(textureCoordinateAttr, "check vTexCoordinate")
        textureUniform = GLES20.glGetUniformLocation(program, "texture")
        checkLocation(textureUniform, "check texture")
        textureTransformUniform = GLES20.glGetUniformLocation(program, "textureTransform")
        checkLocation(textureTransformUniform, "check textureTransform")
        positionTransformUniform = GLES20.glGetUniformLocation(program, "postionTransform")
        checkLocation(positionTransformUniform, "check postionTransform")
    }


    fun render(textureId: Int) {
        onClear()
        onUseProgram()
        onSetExpandData()
        onBindTexture(textureId)
        onDraw()
        unBindTexture()
    }


    private fun onClear() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    private fun onUseProgram() {
        GLES20.glUseProgram(program)
    }

    private fun onSetExpandData() {
        GLES20.glUniformMatrix4fv(
            textureTransformUniform, 1, false,
            textureMatrix, 0
        )
        GLES20.glUniformMatrix4fv(
            positionTransformUniform, 1, false,
            positionMatrix, 0
        )
    }

    private fun onBindTexture(textureId: Int) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glUniform1i(textureUniform, 0)
    }

    private fun unBindTexture(){
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    private fun onDraw() {
        glEnableVertexAttribPointer(positionAttr,vertexBuffer,size = COORDS_VERTEX_2D)
        glEnableVertexAttribPointer(textureCoordinateAttr,textureBuffer,size = COORDS_VERTEX_2D)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,vertexCoords.size/ COORDS_VERTEX_2D)
        GLES20.glDisableVertexAttribArray(positionAttr)
        GLES20.glDisableVertexAttribArray(textureCoordinateAttr)
    }

    private fun checkLocation(location: Int, label: String) {
        if (location < 0) {
            throw RuntimeException("Unable to locate '$label' in program")
        }
    }

}