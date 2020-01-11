package com.opengl.sample.texture

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */
enum class ScaleType(value:Int) {

    FIT_XY(1),

    FIT_START(2),

    FIT_CENTER(3),

    FIT_END(4),

    CENTER(5),

    CENTER_CROP(6),

    CENTER_INSIDE(7);
}