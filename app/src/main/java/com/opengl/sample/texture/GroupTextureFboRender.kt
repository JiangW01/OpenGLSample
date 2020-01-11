package com.opengl.sample.texture

import android.content.res.Resources
import com.opengl.sample.fliter.BaseFilter

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
class GroupTextureFboRender(resources: Resources) : TextureRender(BaseFilter(resources)) {

    private val renders = mutableListOf<TextureFboRender>()

    fun addFilter(filter: BaseFilter): GroupTextureFboRender {
        renders.add(TextureFboRender(filter))
        return this
    }

    fun addRender(render: TextureFboRender): GroupTextureFboRender {
        renders.add(render)
        return this
    }

    override fun render(textureId: Int) {
        var tempTextureId = textureId
        renders.forEach {
            tempTextureId = it.renderToFboTexture(tempTextureId)
        }
        return super.render(tempTextureId)
    }

    override fun sizeChanged(width: Int, height: Int) {
        super.sizeChanged(width, height)
        renders.forEach {
            it.sizeChanged(width,height)
        }
    }


    override fun destroy() {
        renders.forEach {
            it.destroy()
        }
        super.destroy()
    }



}