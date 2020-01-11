package com.opengl.sample.texture

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：
 */
interface ITextureShape {

    fun setup()

    fun draw(mvpMatrix:FloatArray? = null,textureId:Int)

}