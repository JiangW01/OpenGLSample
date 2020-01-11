package com.opengl.sample.fliter

import android.content.res.Resources
import android.opengl.GLES20
import java.nio.ByteBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/11
 * 描述 ：
 */
class Faltung33Filter(resources: Resources) :
    UseSizeFilter(resources, fragmentShader =  "shader/func/faltung3x3.frag") {

    val FILTER_SHARPEN = floatArrayOf(0f, -1f, 0f, -1f, 5f, -1f, 0f, -1f, 0f)
    val FILTER_BORDER = floatArrayOf(0f, 1f, 0f, 1f, -4f, 1f, 0f, 1f, 0f)
    val FILTER_CAMEO = floatArrayOf(2f, 0f, 2f, 0f, 0f, 0f, 3f, 0f, -6f)
    private val mFaltung: FloatArray = FILTER_CAMEO
    private var mGLFaltung: Int = 0

    override fun create(program: Int) {
        super.create(program)
        mGLFaltung = GLES20.glGetUniformLocation(program, "uFaltung")
    }


    override fun render(textureId: Int) {
        super.render(textureId)
        GLES20.glUniformMatrix3fv(mGLFaltung, 1, false, mFaltung, 0)
    }


}