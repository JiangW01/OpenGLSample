package com.opengl.videodecoder.texture

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
interface IRender {

    /**
     * 创建
     * 这一步需要加载纹理，链接openGl环境
     */
    fun create()

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

}