package com.opengl.sample.egl

import android.opengl.*
import android.view.Surface
import com.opengl.sample.video.decode.egl.EGLConfigAttrs
import com.opengl.sample.video.decode.egl.EGLContextAttrs


/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/9
 * EglHelper egl使用帮助类
 * egl使用步骤
 * 1.创建与本地窗口系统的连接
 *  调用 eglGetDisplay 方法得到 EGLDisplay
 * 2.初始化 EGL 方法
 *  调用 eglInitialize 方法初始化
 * 3.确定渲染表面的配置信息
 *  调用 eglChooseConfig 方法得到 EGLConfig
 * 4.创建渲染上下文
 *  通过 EGLDisplay 和 EGLConfig ，调用 eglCreateContext 方法创建渲染上下文，得到 EGLContext
 * 5.创建渲染表面
 *   通过 EGLDisplay 和 EGLConfig ，调用 eglCreateWindowSurface 方法创建渲染表面，得到 EGLSurface
 *   如果是离屏渲染的话就调用 eglCreatePbufferSurface
 * 6.绑定上下文
 *   通过 eglMakeCurrent 方法将 EGLSurface、EGLContext、EGLDisplay 三者绑定，接下来就可以使用 OpenGL 进行绘制了
 * 7.交换缓冲
 *   当用 OpenGL 绘制结束后，使用 eglSwapBuffers 方法交换前后缓冲，将绘制内容显示到屏幕上
 * 8.释放 EGL 环境
 *   绘制结束，不再需要使用 EGL 时，取消 eglMakeCurrent 的绑定，销毁  EGLDisplay、EGLSurface、EGLContext。
 */
class EGLHelper constructor(private val display: Int) {


    private var mEGLDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var mEGLConfig: EGLConfig? = null
    private var mEGLContext: EGLContext = EGL14.EGL_NO_CONTEXT
    private var mEGLSurface: EGLSurface = EGL14.EGL_NO_SURFACE


    constructor() : this(EGL14.EGL_DEFAULT_DISPLAY)


    /**
     * 创建OpenGL环境
     */
    fun createGL(surface: Surface, shareContext: EGLContext = EGL14.EGL_NO_CONTEXT) {
        createEGLDisplay()
        mEGLConfig = getConfig(EGLConfigAttrs())
        mEGLContext = createContext(mEGLConfig, shareContext)
        mEGLSurface = createWindowSurface(surface)
        makeCurrent()
    }


    /**
     * 1.创建与本地窗口系统的连接
     *  调用 eglGetDisplay 方法得到 EGLDisplay
     * 2.初始化 EGL 方法
     *  调用 eglInitialize 方法初始化
     */
    private fun createEGLDisplay() {
        mEGLDisplay = EGL14.eglGetDisplay(display)
        //获取版本号，[0]为版本号，[1]为子版本号
        val versions = IntArray(2)
        EGL14.eglInitialize(mEGLDisplay, versions, 0, versions, 1)
    }


    /**
     * 3.确定渲染表面的配置信息
     *  调用 eglChooseConfig 方法得到 EGLConfig
     *  @param attrs 渲染表面的配置信息
     */
    private fun getConfig(attrs: EGLConfigAttrs): EGLConfig? {
        val configs = arrayOfNulls<EGLConfig>(1)
        val configNum = IntArray(1)
        EGL14.eglChooseConfig(mEGLDisplay, attrs.build(), 0, configs, 0, 1, configNum, 0)
        //选择的过程可能出现多个，也可能一个都没有，这里只用一个
        if (configNum[0] > 0) {
            if (attrs.isDefault()) {
                mEGLConfig = configs[0]
            }
            return configs[0]
        }
        return null
    }


    /**
     * 4.创建渲染上下文
     *  通过 EGLDisplay 和 EGLConfig、EGLContextAttrs ，调用 eglCreateContext 方法创建渲染上下文，得到 EGLContext
     *  @param config 配置信息
     *  @param share 共享上下文
     *  @param attrs 上下文配置信息
     *  @return EGLContext 上下文
     */
    private fun createContext(
        config: EGLConfig? = null,
        share: EGLContext = EGL14.EGL_NO_CONTEXT,
        attrs: EGLContextAttrs = EGLContextAttrs().makeDefault(true)
    ): EGLContext {
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY || config == null) {
            return EGL14.EGL_NO_CONTEXT
        }
        return EGL14.eglCreateContext(mEGLDisplay, config, share, attrs.build(), 0)
    }

    /**
     * 5.创建渲染表面
     * 通过 EGLDisplay 和 EGLConfig ，调用 eglCreateWindowSurface 方法创建渲染表面，得到 EGLSurface
     * 如果是离屏渲染的话就调用 eglCreatePbufferSurface
     * @param surface 渲染表面，可以使surfaceView、TextureView
     */
    private fun createWindowSurface(surface: Surface, config: EGLConfig?): EGLSurface {
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY || config == null) {
            return EGL14.EGL_NO_SURFACE
        }
        val surfaceAttribs = intArrayOf(EGL14.EGL_NONE)
        return EGL14.eglCreateWindowSurface(
            mEGLDisplay,
            config,
            surface,
            surfaceAttribs,
            0
        )
    }


    private fun createWindowSurface(surface: Surface): EGLSurface {
        return createWindowSurface(surface, mEGLConfig)
    }

    /**
     * 5.离屏渲染
     * 通过 EGLDisplay 和 EGLConfig ，调用 eglCreateWindowSurface 方法创建渲染表面，得到 EGLSurface
     * 如果是离屏渲染的话就调用 eglCreatePbufferSurface
     * @param width 宽度
     * @param height 宽度
     * @param config 渲染配置
     */
    private fun createPBufferSurface(
        width: Int,
        height: Int,
        config: EGLConfig? = mEGLConfig
    ): EGLSurface {
        if (config == null)
            return EGL14.EGL_NO_SURFACE
        return EGL14.eglCreatePbufferSurface(
            mEGLDisplay,
            config,
            intArrayOf(EGL14.EGL_WIDTH, width, EGL14.EGL_HEIGHT, height, EGL14.EGL_NONE),
            0
        )
    }


    /**
     * 6.绑定上下文
     *  通过 eglMakeCurrent 方法将 EGLSurface、EGLContext、EGLDisplay 三者绑定，接下来就可以使用 OpenGL 进行绘制了
     */
    private fun makeCurrent(): Boolean {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY && mEGLContext != EGL14.EGL_NO_CONTEXT && mEGLSurface != EGL14.EGL_NO_SURFACE) {
            return EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext)
        }
        return false
    }

    /**
     * 创建一个GLES的窗口
     * @param surface 渲染窗口
     * @param attrs 渲染配置信息
     * @param ctxAttrs 上下文配置信息
     */
    fun createGLESWithSurface(
        surface: Surface,
        attrs: EGLConfigAttrs = EGLConfigAttrs(),
        ctxAttrs: EGLContextAttrs = EGLContextAttrs(),
        shareContext: EGLContext = EGL14.EGL_NO_CONTEXT
    ): Boolean {
        createEGLDisplay()
        mEGLConfig = getConfig(attrs)
        if (mEGLConfig == null) {
            return false
        }
        mEGLContext = createContext(mEGLConfig, shareContext, ctxAttrs.makeDefault(true))
        if (mEGLContext === EGL14.EGL_NO_CONTEXT) {
            return false
        }
        mEGLSurface = createWindowSurface(surface)
        if (mEGLSurface == EGL14.EGL_NO_SURFACE) {
            return false
        }
        if (!makeCurrent()) {
            return false
        }
        return true
    }

    /**
     * 离屏渲染
     * @param width 宽度
     * @param width 宽度
     * @param attrs 渲染配置信息
     * @param ctxAttrs 上下文配置信息
     */
    fun createGLESWithPBuffer(
        width: Int,
        height: Int,
        attrs: EGLConfigAttrs = EGLConfigAttrs(),
        ctxAttrs: EGLContextAttrs = EGLContextAttrs(),
        shareContext: EGLContext = EGL14.EGL_NO_CONTEXT
    ): Boolean {
        createEGLDisplay()
        mEGLConfig = getConfig(attrs)
        if (mEGLConfig == null) {
            return false
        }
        mEGLContext = createContext(mEGLConfig, shareContext, ctxAttrs.makeDefault(true))
        if (mEGLContext === EGL14.EGL_NO_CONTEXT) {
            return false
        }
        mEGLSurface = createPBufferSurface(width, height)
        if (mEGLSurface == EGL14.EGL_NO_SURFACE) {
            return false
        }
        if (!makeCurrent()) {
            return false
        }
        return true
    }

    /**
     *
     * 8.释放 EGL 环境
     *   绘制结束，不再需要使用 EGL 时，取消 eglMakeCurrent 的绑定，销毁  EGLDisplay、EGLSurface、EGLContext。
     */
    fun destroyGLES(surface: EGLSurface = mEGLSurface, context: EGLContext = mEGLContext): Boolean {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglMakeCurrent(
                mEGLDisplay,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_SURFACE,
                EGL14.EGL_NO_CONTEXT
            )
        }
        if (surface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroySurface(mEGLDisplay, surface)
        }
        if (context != EGL14.EGL_NO_CONTEXT) {
            EGL14.eglDestroyContext(mEGLDisplay, context)
        }
        EGL14.eglTerminate(mEGLDisplay)
        mEGLDisplay = EGL14.EGL_NO_DISPLAY
        mEGLSurface = EGL14.EGL_NO_SURFACE
        mEGLContext = EGL14.EGL_NO_CONTEXT
        mEGLConfig = null
        return true
    }

    /**
     * 7.交换缓冲
     *  当用 OpenGL 绘制结束后，使用 eglSwapBuffers 方法交换前后缓冲，将绘制内容显示到屏幕上
     */
    fun swapBuffers(surface: EGLSurface = mEGLSurface) {
        if (mEGLDisplay == EGL14.EGL_NO_SURFACE) {
            return
        }
        EGL14.eglSwapBuffers(mEGLDisplay, surface)
    }

}