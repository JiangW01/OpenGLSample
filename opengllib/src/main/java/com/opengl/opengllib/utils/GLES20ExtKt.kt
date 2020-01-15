package com.opengl.opengllib.utils

import android.opengl.GLES20
import java.nio.Buffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/15
 * 描述 ：
 */

const val GL_POINTS = GLES20.GL_POINTS
const val GL_LINES = GLES20.GL_LINES
const val GL_LINE_LOOP = GLES20.GL_LINE_LOOP
const val GL_LINE_STRIP = GLES20.GL_LINE_STRIP
const val GL_TRIANGLES = GLES20.GL_TRIANGLES
const val GL_TRIANGLE_STRIP = GLES20.GL_TRIANGLE_STRIP
const val GL_TRIANGLE_FAN = GLES20.GL_TRIANGLE_FAN



//每个顶点的坐标数（x,y,z）
const val COORDS_VERTEX_2D = 2
//每个顶点的坐标数（x,y,z）
const val COORDS_VERTEX_3D = 3
const val BYTES_PRE_FLOAT = 4

/**
 * 绘制图形
 * @param mode 绘制图形方式 GL_POINTS、GL_LINES、GL_LINE_LOOP、GL_LINE_STRIP
 *             、GL_TRIANGLES、GL_TRIANGLE_STRIP、GL_TRIANGLE_FAN
 * @param count 顶点个数
 * @param first 绘制的起始 默认0
 */
inline fun drawArrays(mode:Int,count:Int,first:Int = 0){
    GLES20.GL_POINTS
    GLES20.glDrawArrays(mode,first,count)
}


/**
 * 获取顶点attribute属性句柄
 * @return attribute句柄
 */
inline fun getAttribLocation(program:Int,attributeName:String):Int{
    return GLES20.glGetAttribLocation(program,attributeName)
}


/**
 * 禁用顶点句柄
 * @param handle
 */
inline fun disableVertexAttribArray(handle:Int){
    GLES20.glDisableVertexAttribArray(handle)
}


/**
 * 获取unifrom属性句柄
 * @return unifrom句柄
 */
inline fun getUnifromLocation(program:Int,name:String):Int{
    return GLES20.glGetUniformLocation(program,name)
}

/**
 * 使能顶点attribute属性并赋值
 * @param handle attribute句柄
 * @param dataBuffer 数据
 * @param size 数组中每个顶点的坐标数
 * @param type 类型 默认值：GLES20.GL_FLOAT
 * @param normalized  默认值：false 是否标准化
 * @param stride  默认值：size*4 跨度
 */
inline fun useAttribute(
    handle: Int,dataBuffer: Buffer, size: Int = COORDS_VERTEX_2D,
    type: Int = GLES20.GL_FLOAT, normalized: Boolean = false, stride: Int = size * 4
) {
    GLES20.glVertexAttribPointer(handle, size, type, normalized, stride, dataBuffer)
    GLES20.glEnableVertexAttribArray(handle)
}