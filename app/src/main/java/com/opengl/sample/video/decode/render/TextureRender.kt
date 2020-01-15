package com.opengl.sample.video.decode.render

import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import com.opengl.sample.video.decode.filter.BaseFilter
import com.opengl.sample.video.decode.egl.EGLHelper
import com.opengl.sample.video.decode.utils.loadTexture
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 *@version:
 *@FileDescription: 创建显卡可执行程序Program
 *@Author:Jing
 *@Since:2019/3/27
 *@ChangeList:
 */
class TextureRender(var width: Int, var height: Int, val filter: BaseFilter = BaseFilter()) {


    private var textureId: Int = 0

    private var surfaceTexture: SurfaceTexture

    private var pixelBuf: ByteBuffer

    private val mGLThreadName: String = Thread.currentThread().name
    private val mEGLHelper by lazy {
        EGLHelper()
    }

    init {
        mEGLHelper.createGLESWithPBuffer(width, height)
        filter.create()
        textureId = loadTexture(isOes = true)
        surfaceTexture = SurfaceTexture(textureId)
        pixelBuf = ByteBuffer.allocate(width * height * 4)
        pixelBuf.order(ByteOrder.LITTLE_ENDIAN)
    }


    fun getSurfaceTexture(): SurfaceTexture {
        return surfaceTexture
    }

    fun render() {
        checkThread()
        this.render(surfaceTexture)
    }

    fun render(st: SurfaceTexture) {
        st.updateTexImage()
        st.getTransformMatrix(filter.textureMatrix)
        filter.render(textureId)
    }

    private fun checkThread() {
        if (Thread.currentThread().name != mGLThreadName) {
            throw RuntimeException("getBitmap: This thread does not craete the OpenGL context.")
        }
    }

    fun produceBitmap(): Bitmap {
        checkThread()
        pixelBuf.rewind()
        GLES20.glReadPixels(
            0, 0, width, height, GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE, pixelBuf
        )
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        pixelBuf.rewind()
        bmp.copyPixelsFromBuffer(pixelBuf)
        return bmp
    }

}