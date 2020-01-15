package com.opengl.sample.video.decode.egl

import android.opengl.EGL14
import javax.microedition.khronos.egl.EGL10

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/13
 * 描述 ：
 */
class EGLConfigAttrs {


    private var red = 8
    private var green = 8
    private var blue = 8
    private var alpha = 8
    private var depth = 8
    private var bufferSize = 32
    private var renderType = EGL14.EGL_OPENGL_ES2_BIT
    private var surfaceType = EGL10.EGL_WINDOW_BIT

    private var makeDefault = true

    fun red(red: Int): EGLConfigAttrs {
        this.red = red
        return this
    }

    fun green(green: Int): EGLConfigAttrs {
        this.green = green
        return this
    }

    fun bufferSize(bufferSize: Int): EGLConfigAttrs {
        this.bufferSize = green
        return this
    }


    fun blue(blue: Int): EGLConfigAttrs {
        this.blue = blue
        return this
    }

    fun alpha(alpha: Int): EGLConfigAttrs {
        this.alpha = alpha
        return this
    }

    fun depth(depth: Int): EGLConfigAttrs {
        this.depth = depth
        return this
    }

    fun renderType(type: Int): EGLConfigAttrs {
        this.renderType = type
        return this
    }

    fun surfaceType(type: Int): EGLConfigAttrs {
        this.surfaceType = type
        return this
    }

    fun makeDefault(def: Boolean): EGLConfigAttrs {
        this.makeDefault = def
        return this
    }

    fun isDefault(): Boolean {
        return makeDefault
    }

    fun build(): IntArray {
        return intArrayOf(
            EGL14.EGL_BUFFER_SIZE, bufferSize,
            EGL10.EGL_RED_SIZE, red, //指定RGB中的R大小（bits）
            EGL10.EGL_GREEN_SIZE, green, //指定G大小
            EGL10.EGL_BLUE_SIZE, blue, //指定B大小
            EGL10.EGL_ALPHA_SIZE, alpha, //指定Alpha大小，以上四项实际上指定了像素格式
            EGL10.EGL_DEPTH_SIZE, depth, //指定深度缓存(Z Buffer)大小
            EGL10.EGL_RENDERABLE_TYPE, renderType, //指定渲染api类别, 如上一小节描述，这里或者是硬编码的4(EGL14.EGL_OPENGL_ES2_BIT)
            EGL10.EGL_SURFACE_TYPE, surfaceType, //渲染类型
            EGL10.EGL_NONE //总是以EGL14.EGL_NONE结尾
        )
    }

}