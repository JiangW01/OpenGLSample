package com.opengl.sample.view

import android.content.Context
import android.opengl.EGLContext
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.Surface
import java.lang.ref.WeakReference
import com.opengl.sample.video.decode.egl.EGLHelper




/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/9
 * 描述 ：
 */
class EGLSurfaceView : SurfaceView, SurfaceHolder.Callback {


    private var render: Renderer? = null
    private var surface: Surface? = null
    private var eglThread: EGLThread? = null
    private var eglContext: EGLContext? = null

    companion object {
        const val RENDERMODE_WHEN_DIRTY = 0
        const val RENDERMODE_CONTINUOUSLY = 1
    }

    private var renderMode = RENDERMODE_WHEN_DIRTY

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        println("EGLRenderer surfaceChanged")
        eglThread?.isChange = true
        eglThread?.width = width
        eglThread?.height = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        println("EGLRenderer surfaceDestroyed")
        eglThread?.onDestroy()
        eglThread = null
        surface = null
        eglContext = null
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        println("EGLRenderer surfaceCreated")
        surface = holder?.surface
        eglThread = EGLThread(this)
        eglThread?.isCreate = true
        eglThread?.start()
    }

    fun setRenderMode(mode: Int) {
        renderMode = mode
    }

    fun setRender(render: Renderer) {
        this.render = render
    }

    fun requestRender() {
        eglThread?.requestRender()
    }

    fun setSurfaceAndEglContext(surface: Surface, eglContext: EGLContext) {
        this.surface = surface
        this.eglContext = eglContext
    }



    class EGLThread(eglSurfaceView: EGLSurfaceView) : Thread() {

        private val weakReference = WeakReference<EGLSurfaceView>(eglSurfaceView)

        private val eglHelper = EGLHelper()
        private val lock = Object()
        var isExit = false
        var isStart = false
        var isCreate = false
        var isChange = false
        var width = 0
        var height = 0

        override fun run() {
            super.run()
            try {
                guardedRun()
            } catch (e: Exception) {
            }
        }

        private fun guardedRun() {
            isExit = false
            isStart = false
            weakReference.get()?.let {
                it.surface?.let { surface ->
                    eglHelper.createGLESWithSurface(surface)
                }
            }
            while (true) {
                if (isExit) {
                    release()
                    break
                }
                if (isStart) {
                    val eglSurfaceView = weakReference.get()
                    eglSurfaceView?.let {
                        if (it.renderMode == RENDERMODE_WHEN_DIRTY) {
                            synchronized(lock) {
                                lock.wait()
                            }
                        } else if (it.renderMode == RENDERMODE_CONTINUOUSLY) {
                            Thread.sleep(1000 / 60)
                        }
                    }
                }
                onCreate()
                onChange(width, height)
                onDraw()
                isStart = true
            }

        }

        private fun onCreate() {
            if (!isCreate || weakReference.get()?.render == null)
                return
            isCreate = false
            weakReference.get()?.render?.onSurfaceCreated()
        }

        private fun onChange(width: Int, height: Int) {
            if (!isChange || weakReference.get()?.render == null)
                return
            isChange = false
            weakReference.get()?.render?.onSurfaceChanged(width, height)
        }

        private fun onDraw() {
            if (weakReference.get()?.render == null)
                return
            weakReference.get()?.render?.onDrawFrame()
            //第一次的时候手动调用一次 不然不会显示ui
            if (!isStart) {
                weakReference.get()?.render?.onDrawFrame()
            }
            eglHelper.swapBuffers()
        }

        fun requestRender() {
            if (lock != null) {
                synchronized(lock) {
                    lock.notifyAll()
                }
            }
        }

        fun onDestroy() {
            isExit = true
            //释放锁
            requestRender()
        }

        fun release() {
            eglHelper.destroyGLES()

        }

    }

    interface Renderer {
        fun onSurfaceCreated()

        fun onSurfaceChanged(width: Int, height: Int)

        fun onDrawFrame()

    }

}