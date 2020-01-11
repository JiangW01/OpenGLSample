package com.opengl.sample.view

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.opengl.sample.render.GLTextureRender

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：
 */
class GLTextureView:GLSurfaceView {


    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet)


    init {
        //设置opengl2.0
        setEGLContextClientVersion(2)
        setRenderer(GLTextureRender(context))
        renderMode = RENDERMODE_WHEN_DIRTY
    }



}