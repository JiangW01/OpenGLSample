package com.opengl.sample.utils

import android.opengl.*
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import android.opengl.EGL14.eglGetConfigAttrib
import javax.microedition.khronos.egl.EGL10
import android.opengl.EGL14.eglTerminate
import android.opengl.EGL14.eglDestroyContext
import android.opengl.EGL14.eglDestroySurface
import android.opengl.EGL14.eglMakeCurrent
import android.opengl.EGL14.eglSwapBuffers
import android.opengl.EGL14.eglCreateWindowSurface
import android.opengl.EGL14.eglCreateContext
import android.opengl.EGL14.eglChooseConfig
import android.opengl.EGL14.eglInitialize
import android.opengl.EGL14.eglGetDisplay
import javax.microedition.khronos.egl.EGLContext.getEGL


/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/9
 * 描述 ：
 */
class EGLHelper {


    private val defalutDisplay = EGL14.EGL_NO_DISPLAY

    private var eglDisplay: EGLDisplay = defalutDisplay
    private var eglConfig: EGLConfig? = null
    private var eglContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface: EGLSurface? = null

    /**
     * 创建OpenGL环境
     */
    fun createGL(surface: Surface, eglContext: EGLContext? = null) {

        // 1.获取显示设备(默认的显示设备)
        eglDisplay = eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        // 2.初始化
        val version = IntArray(2)
        if (!eglInitialize(eglDisplay, version, 0, version, 1)) {
            throw RuntimeException("EGL error " + EGL14.eglGetError())
        }
        // 设置显示设备的属性
        val configAttribs = intArrayOf(
            EGL14.EGL_BUFFER_SIZE, 32,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE,
            EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_SURFACE_TYPE,
            EGL14.EGL_WINDOW_BIT,
            EGL14.EGL_NONE
        )
        val numConfigs = IntArray(1)
        val configs = arrayOfNulls<EGLConfig>(1)
        // 3.从系统中获取对应属性的配置
        if (!eglChooseConfig(
                eglDisplay, configAttribs, 0,
                configs, 0, configs.size, numConfigs, 0
            )
        ) {
            throw RuntimeException("EGL error " + EGL14.eglGetError())
        }
        eglConfig = configs[0]
        // 4.创建EglContext
        val contextAttribs = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
        if (eglContext == null) {
            this.eglContext = eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, contextAttribs, 0)
        } else {
            this.eglContext = eglCreateContext(eglDisplay, eglConfig, eglContext, contextAttribs,0)
        }
        if (this.eglContext === EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException("EGL error " + EGL14.eglGetError())
        }
        //5. 创建渲染的Surface
        val surfaceAttribs = intArrayOf(EGL14.EGL_NONE)
        eglSurface = eglCreateWindowSurface(eglDisplay, eglConfig, surface, surfaceAttribs, 0)
        //8. 绑定EglContext和Surface到显示设备中
        eglMakeCurrent(eglDisplay, eglSurface, eglSurface, this.eglContext)
    }


    /**
     * 销毁OpenGL环境
     */
    fun destroyGL() {
        if (eglSurface != null && eglSurface != EGL10.EGL_NO_SURFACE) {
            eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT)
            eglDestroySurface(eglDisplay, eglSurface)
            eglSurface = null
        }

        if (eglContext != null) {
            eglDestroyContext(eglDisplay, eglContext)
            eglContext = EGL14.EGL_NO_CONTEXT
        }
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            eglTerminate(eglDisplay)
            eglDisplay = EGL14.EGL_NO_DISPLAY
        }
    }


    fun eglSwapBuffers() {
        // 交换显存(将surface显存和显示器的显存交换)
        eglSurface?.let {
            eglSwapBuffers(eglDisplay, it)
        }
    }

    fun getEglContext(): EGLContext {
        return eglContext
    }
}