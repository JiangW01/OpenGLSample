package com.opengl.sample.render

import android.content.Context
import android.opengl.GLSurfaceView
import android.widget.ImageView
import com.opengl.sample.fliter.*
import com.opengl.sample.texture.TextureRender
import com.opengl.sample.utils.loadTextureFromAssets
import com.opengl.sample.texture.ScaleType
import com.opengl.sample.texture.TextureFboRender
import com.opengl.sample.utils.getMatrix
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
//    val filter = SobelFilter(context.resources)
//    val filter = Sobel2Filter(context.resources)
//    val filter = GaussFilter(context.resources)
//    val filter = BaseFilter(context.resources)
//    val filter = FilterFilter(context.resources)
    val filter = GrayFilter(context.resources)
//    val filter = BeautyFilter(context.resources)
//    val filter = WaterColorFilter(context.resources)


//    private val render = TextureRender(filter)
    private val render = TextureFboRender(filter)

    private var textureId = 0
    private var size = Pair(0,0)

    init {
        size = loadBitmapSizeFromAssets(context,"image/md.jpg")
    }

    override fun onDrawFrame(gl: GL10?) {
        render.render(textureId)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        render.sizeChanged(width,height)
        getMatrix(render.getVertexMatrix(),ScaleType.FIT_CENTER,
            size.first,size.second,width,height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        textureId = loadTextureFromAssets(context,"image/md.jpg")
        render.create()
    }
}