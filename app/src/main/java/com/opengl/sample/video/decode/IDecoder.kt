package com.opengl.sample.video.decode

/**
 * 作者 ：wangJiang
 * 时间 ：2019/7/26
 * 描述 ：
 */
interface IDecoder {


    fun setDataSource(filePath:String)

    fun release()

    fun getDuration():Long

    fun getWidth():Int

    fun getHeight():Int

}