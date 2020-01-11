package com.opengl.videodecoder.filter

import com.opengl.sample.texture.TextureRender

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
interface IFliter {


    /**
     * 创建
     * @param program gl
     */
    fun create(program:Int)

    /**
     * 渲染
     * @param texture 输入纹理
     */
    fun render(textureId:Int)

    /**
     * 大小改变
     * @param width 宽度
     * @param height 高度
     */
    fun sizeChanged(width: Int, height: Int)

    /**
     * 销毁
     */
    fun destroy()


    fun getVertexShader():Int
    fun getFragShader():Int

}