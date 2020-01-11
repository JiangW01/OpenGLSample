package com.opengl.sample.fliter

import android.content.res.Resources
import com.opengl.sample.texture.TextureRender
import com.opengl.sample.utils.loadFragShader
import com.opengl.sample.utils.loadFragShaderAssets
import com.opengl.sample.utils.loadVertexShader
import com.opengl.sample.utils.loadVertexShaderAssets
import com.opengl.videodecoder.filter.IFliter
import com.opengl.videodecoder.texture.IRender

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
open class BaseFilter(
    private val resources: Resources,
    private val vertexShader: String = "shader/base.vert",
    private val fragmentShader: String = "shader/base.frag"
) : IFliter {

    override fun getVertexShader(): Int {
        return loadVertexShaderAssets(resources,vertexShader)
    }

    override fun getFragShader(): Int {
        return loadFragShaderAssets(resources,fragmentShader)
    }



    override fun create(program:Int) {
    }

    override fun render(textureId: Int) {
    }

    override fun sizeChanged(width: Int, height: Int) {
    }

    override fun destroy() {
    }
}