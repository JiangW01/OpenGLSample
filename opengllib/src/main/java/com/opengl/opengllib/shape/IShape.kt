package com.opengl.opengllib.shape

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/15
 * 描述 ：
 */
interface IShape {


    /**
     * 创建
     * @return 是否创建成功
     */
    fun create():Boolean

    /**
     * 视图大小改变
     * @param width
     * @param height
     */
    fun onSurfaceSizeChange(width:Int,height:Int)


    fun render()

}