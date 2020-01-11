package com.opengl.sample.fliter

import android.content.res.Resources
import android.opengl.GLES20
import java.nio.ByteBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/11
 * 描述 ：
 */
open class UseSizeFilter(resources: Resources, fragmentShader: String) :
    BaseFilter(resources, fragmentShader = fragmentShader) {

    private var width = 0f
    private var height = 0f
    private var widthHandle = 0
    private var heightHandle = 0


    override fun create(program: Int) {
        super.create(program)
        widthHandle = GLES20.glGetUniformLocation(program, "uWidth")
        heightHandle = GLES20.glGetUniformLocation(program, "uHeight")
    }

    override fun sizeChanged(width: Int, height: Int) {
        super.sizeChanged(width, height)
        this.width = width.toFloat()
        this.height = height.toFloat()
    }

    override fun render(textureId: Int) {
        super.render(textureId)
        GLES20.glUniform1f(widthHandle, width)
        GLES20.glUniform1f(heightHandle, height)
    }


}