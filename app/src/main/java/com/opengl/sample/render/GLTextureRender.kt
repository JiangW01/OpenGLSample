package com.opengl.sample.render

import android.content.Context
import android.opengl.GLSurfaceView
import com.opengl.sample.fliter.*
import com.opengl.sample.texture.TextureRender
import com.opengl.sample.utils.loadTextureFromAssets
import com.opengl.sample.texture.ScaleType
import com.opengl.sample.video.decode.utils.getMatrix
import com.opengl.sample.utils.loadBitmapSizeFromAssets
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
class GLTextureRender(val context: Context) :GLSurfaceView.Renderer{


    //设置render
//    val filter = Faltung33Filter(context.resources)
    val sobel = SobelFilter(context.resources)
    val sobel2 = Sobel2Filter(context.resources)
    val gauss = GaussFilter(context.resources)
//    val filter = BaseFilter(context.resources)
//    val filter = FilterFilter(context.resources)
    val filter = GrayFilter(context.resources)
//    val filter = BeautyFilter(context.resources)
    val waterColor = WaterColorFilter(context.resources)


    private val render = TextureRender(filter)
//    private val frameBufferRenderer = FrameBufferRender(filter)
//    private val frameBufferRenderer1 = FrameBufferRender(gauss)

    private var textureId = 0
    private var size = Pair(0,0)

    init {
        size = loadBitmapSizeFromAssets(context,"image/image.jpg")
    }

    override fun onDrawFrame(gl: GL10?) {
//        frameBufferRenderer.render(textureId)
//        val frameTextureId = frameBufferRenderer.getFrameBufferTextureId()
//        frameBufferRenderer1.setTextureBuffer(getOriginalFrameBuffferTextureCo())
//        frameBufferRenderer1.render(frameTextureId)
//        val frameTextureId1 = frameBufferRenderer1.getFrameBufferTextureId()
//        render.setTextureBuffer(getOriginalFrameBuffferTextureCo())
        render.render(textureId)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        render.sizeChanged(width,height)
//        frameBufferRenderer.sizeChanged(width,height)
//        frameBufferRenderer1.sizeChanged(width,height)
        getMatrix(
            render.getVertexMatrix(), ScaleType.FIT_CENTER,
            size.first, size.second, width, height
        )
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        textureId = loadTextureFromAssets(context,"image/image.jpg")
//        frameBufferRenderer.create()
//        frameBufferRenderer1.create()
        render.create()
    }
}