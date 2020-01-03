package com.opengl.sample.shape

/**
 * 作者 ：wangJiang
 * 时间 ：2019/12/30
 * 描述 ：
 */
interface IShape {

    fun setup()

    fun draw(mvpMatrix:FloatArray? = null)

}