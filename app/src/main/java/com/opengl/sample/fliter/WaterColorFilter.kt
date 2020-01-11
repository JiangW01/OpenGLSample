package com.opengl.sample.fliter

import android.content.res.Resources
import android.opengl.GLES20
import java.nio.ByteBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/11
 * 描述 ：
 */
class WaterColorFilter(resources: Resources) :
    BaseFilter(resources, "shader/base.vert", "shader/effect/water_color.frag") {

    private var width = 0f
    private var height = 0f
    private var widthHandle = 0
    private var heightHandle = 0
    private var mGLNoise = 0
    private var mNoiseTextureId = 0


    override fun create(program: Int) {
        super.create(program)
        widthHandle = GLES20.glGetUniformLocation(program, "uWidth")
        heightHandle = GLES20.glGetUniformLocation(program, "uHeight")
        mGLNoise = GLES20.glGetUniformLocation(program, "uNoiseTexture")
    }

    override fun sizeChanged(width: Int, height: Int) {
        super.sizeChanged(width, height)
        this.width = width.toFloat()
        this.height = height.toFloat()
        mNoiseTextureId = createNoiseTexture(width, height)
    }

    override fun render(textureId: Int) {
        super.render(textureId)
        GLES20.glUniform1f(widthHandle, width)
        GLES20.glUniform1f(heightHandle, height)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mNoiseTextureId)
        GLES20.glUniform1i(mGLNoise, 1)
    }


    private fun createNoiseTexture(width: Int, height: Int): Int {
        val tempTexture = IntArray(1)
        GLES20.glGenTextures(1, tempTexture, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tempTexture[0])
        val length = width * height * 3
        val data = ByteArray(length)
        for (i in 0 until length) {
            data[i] = (Math.random() * 8 - 4).toByte()
        }
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGB,
            width,
            height,
            0,
            GLES20.GL_RGB,
            GLES20.GL_UNSIGNED_BYTE,
            ByteBuffer.wrap(data)
        )
        return tempTexture[0]
    }

}