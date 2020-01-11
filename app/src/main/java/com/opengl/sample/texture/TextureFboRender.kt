package com.opengl.sample.texture

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import com.opengl.sample.fbo.FrameBufferHelper
import com.opengl.sample.fliter.BaseFilter
import com.opengl.sample.utils.asFloatBuffer
import com.opengl.sample.utils.getOriginalTextureCo
import com.opengl.sample.utils.getOriginalVertexCo
import com.opengl.sample.utils.glCreateProgram
import com.opengl.videodecoder.filter.IFliter
import com.opengl.videodecoder.texture.IRender
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
class TextureFboRender(filter: IFliter) : TextureRender(filter) {

    private var frameBufferHelper = FrameBufferHelper()


    override fun sizeChanged(width: Int, height: Int) {
        super.sizeChanged(width, height)
        frameBufferHelper.createFrameBuffer(width,height)
    }


    override fun destroy() {
        frameBufferHelper.destroyFrameBuffer()
        super.destroy()
    }

    override fun render(textureId: Int) {
        val tempTextureId = renderToFboTexture(textureId)
        super.render(tempTextureId)
    }

    /**
     * 绘制内容到fbo纹理上
     * @param texture 输入纹理ID
     * @return 输出纹理ID
     */
    fun renderToFboTexture(texture: Int): Int {
        frameBufferHelper.bindFrameBuffer()
        super.render(texture)
        frameBufferHelper.unBindFrameBuffer()
        return frameBufferHelper.getCacheTextureId()
    }

}