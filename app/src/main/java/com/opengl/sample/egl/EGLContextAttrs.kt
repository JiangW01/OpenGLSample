package com.opengl.sample.egl

import android.opengl.EGL14

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/13
 * 描述 ：
 */
class EGLContextAttrs {

    private var version = 2

    private var isDefault: Boolean = false

    fun version(v: Int): EGLContextAttrs {
        this.version = v
        return this
    }

    fun makeDefault(def: Boolean): EGLContextAttrs {
        this.isDefault = def
        return this
    }

    fun isDefault(): Boolean {
        return isDefault
    }

    fun build(): IntArray {
        return intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, version, EGL14.EGL_NONE)
    }
}