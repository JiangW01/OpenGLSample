package com.opengl.sample.texture

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import com.opengl.sample.video.decode.egl.EGLHelper
import com.opengl.sample.fliter.GrayFilter
import com.opengl.sample.utils.loadBitmapSizeFromAssets
import com.opengl.sample.utils.loadTextureFromAssets
import java.nio.IntBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/13
 * 描述 ：绘制bitmap
 *  通过EGL在后台渲染
 *  需保证在同一线程，创建跟获取
 */
class BitmapRender(context: Context) {

    private var pixelBuf: IntBuffer
    private var pixelArray: IntArray
    private val mEGLHelper by lazy {
        EGLHelper()
    }

    private val mGLThreadName :String = Thread.currentThread().name

    private val textureRender: TextureRender = TextureRender(GrayFilter(context.resources))
    private var width = 0
    private var height = 0

    private var textureId = 0

    init {
        val size = loadBitmapSizeFromAssets(context,"image/image.jpg")
        width = size.first
        height = size.second
        mEGLHelper.createGLESWithPBuffer(width, height)
        textureId = loadTextureFromAssets(context,"image/image.jpg")
        textureRender.create()
        textureRender.sizeChanged(width,height)
        pixelBuf = IntBuffer.allocate(width * height )
        pixelArray = IntArray(width*height)
    }


    fun release(){
        textureRender.destroy()
        mEGLHelper.destroyGLES()
        pixelBuf.rewind()
        textureId = 0
    }

    fun getBitmap(): Bitmap {
        if (Thread.currentThread().name != mGLThreadName) {
            throw RuntimeException("getBitmap: This thread does not craete the OpenGL context.")
        }
        textureRender.render(textureId)
        return convertToBitmap()
    }

    private fun convertToBitmap(): Bitmap {
        pixelBuf.rewind()
        GLES20.glReadPixels(
            0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
            pixelBuf
        )
        val tempArray = pixelBuf.array()
        //将倒置镜像反转为右侧上法线
        for (i in 0 until height) {
            System.arraycopy(tempArray, i * width, pixelArray, (height - i - 1) * width, width)
        }
        pixelBuf.rewind()
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelArray))
        return bitmap
    }




}