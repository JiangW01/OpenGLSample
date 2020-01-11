package com.opengl.sample.fliter

import android.content.res.Resources
import android.opengl.GLES20

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/11
 * 描述 ：
 */
class BeautyFilter(resources: Resources) :
    BaseFilter(resources, "shader/beauty/beauty.vert", "shader/beauty/beauty.frag") {

    private var width = 0f
    private var height = 0f
    private var widthHandle = 0
    private var heightHandle = 0
    private var mGLaaCoef: Int = 0
    private var mGLmixCoef: Int = 0
    private var mGLiternum: Int = 0
    var level: Int = 10
        set(value) {
            beauty = setBeautyLevel(value)
            field = value
        }
    private var beauty = Beauty()


    init {
        beauty = setBeautyLevel(1)
    }

    override fun create(program: Int) {
        super.create(program)
        widthHandle = GLES20.glGetUniformLocation(program, "uWidth")
        heightHandle = GLES20.glGetUniformLocation(program, "uHeight")

        mGLaaCoef = GLES20.glGetUniformLocation(program, "uACoef")
        mGLmixCoef = GLES20.glGetUniformLocation(program, "uMixCoef")
        mGLiternum = GLES20.glGetUniformLocation(program, "uIternum")
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
        GLES20.glUniform1f(mGLaaCoef, beauty.b)
        GLES20.glUniform1f(mGLmixCoef, beauty.c)
        GLES20.glUniform1i(mGLiternum, beauty.a)
    }

    private fun setBeautyLevel(level: Int): Beauty {
        return when (level) {
            1 -> Beauty(1, 0.19f, 0.54f)
            2 -> Beauty(2, 0.29f, 0.54f)
            3 -> Beauty(3, 0.17f, 0.39f)
            4 -> Beauty(3, 0.25f, 0.54f)
            5 -> Beauty(4, 0.13f, 0.54f)
            6 -> Beauty(4, 0.19f, 0.69f)
            else -> Beauty(0, 0f, 0f)
        }
    }

    class Beauty(val a: Int = 0, val b: Float = 0f, val c: Float = 0f)

}