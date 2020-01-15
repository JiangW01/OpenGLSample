package com.opengl.sample.texture

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import com.opengl.sample.fbo.FrameBufferHelper
import com.opengl.sample.utils.asFloatBuffer
import com.opengl.sample.video.decode.utils.getOriginalTextureCo
import com.opengl.sample.video.decode.utils.getOriginalVertexCo
import com.opengl.sample.utils.glCreateProgram
import com.opengl.videodecoder.filter.IFliter
import com.opengl.videodecoder.texture.IRender
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
open class FrameBufferRender(private val filter: IFliter) : IRender {

    private var program = 0

    private var vertexBuffer: FloatBuffer
    private var textureBuffer: FloatBuffer

    //矩阵
    private var textureMatrix = FloatArray(16)
    private var vertexMatrix = FloatArray(16)

    //句柄
    private var vertexHandle = 0
    private var textureVertexHandle = 0
    private var vertexMatrixHandle = 0
    private var textureMatrixHandle = 0
    private var textureHandle = 0
    private var frameBufferHelper = FrameBufferHelper()

    init {
        vertexBuffer = asFloatBuffer(getOriginalVertexCo())
        textureBuffer = asFloatBuffer(getOriginalTextureCo())
        Matrix.setIdentityM(textureMatrix, 0)
        Matrix.setIdentityM(vertexMatrix, 0)
    }


    override fun create() {
        val vertexShader = filter.getVertexShader()
        val fragShader = filter.getFragShader()
        program = glCreateProgram(vertexShader, fragShader)
        if (program > 0) {
            vertexHandle = GLES20.glGetAttribLocation(program, "aVertexCo")
            textureVertexHandle = GLES20.glGetAttribLocation(program, "aTextureCo")
            vertexMatrixHandle = GLES20.glGetUniformLocation(program, "uVertexMatrix")
            textureMatrixHandle = GLES20.glGetUniformLocation(program, "uTextureMatrix")
            textureHandle = GLES20.glGetUniformLocation(program, "uTexture")
            filter.create(program)
        }
    }

    override fun render(textureId: Int) {
        val bindResult = frameBufferHelper.bindFrameBuffer()
        println("bindResult = $bindResult")
        onClear()
        onUseProgram()
        filter.render(textureId)
        onSetExpandData()
        onBindTexture(textureId)
        onDraw()
        val unbindResult = frameBufferHelper.unBindFrameBuffer()
        println("unbindResult = $unbindResult")
    }

    override fun sizeChanged(width: Int, height: Int) {
        filter.sizeChanged(width,height)
        val result = frameBufferHelper.bindFrameBuffer(width,height)
        println("result = $result")
    }


    override fun destroy() {
        filter.destroy()
        GLES20.glDeleteProgram(program)
        frameBufferHelper.destroyFrameBuffer()
    }

    private fun onClear() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    private fun onUseProgram() {
        GLES20.glUseProgram(program)
    }

    //设置矩阵变化信息
    private fun onSetExpandData() {
        GLES20.glUniformMatrix4fv(vertexMatrixHandle, 1, false, vertexMatrix, 0)
        GLES20.glUniformMatrix4fv(textureMatrixHandle, 1, false, textureMatrix, 0)
    }

    //绑定纹理
    private fun onBindTexture(textureId: Int) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(textureHandle, 0)
    }

    //解除绑定纹理
    private fun unBindTexture() {
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0)
    }

    private fun onDraw() {
        GLES20.glEnableVertexAttribArray(vertexHandle)
        GLES20.glVertexAttribPointer(vertexHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glEnableVertexAttribArray(textureVertexHandle)
        GLES20.glVertexAttribPointer(textureVertexHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(vertexHandle)
        GLES20.glDisableVertexAttribArray(textureVertexHandle)
    }

    fun getVertexMatrix(): FloatArray {
        return vertexMatrix
    }

    fun setVertexMatrix(matrix: FloatArray) {
        this.vertexMatrix = matrix
    }


    fun setTextureBuffer(textureVertexs: FloatArray) {
        textureBuffer = asFloatBuffer(textureVertexs)
    }

    fun getFrameBufferTextureId():Int{
        return frameBufferHelper.getCacheTextureId()
    }

}