package com.opengl.sample.fliter

import android.content.res.Resources

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/11
 * 描述 ：
 */
class GrayFilter(resources: Resources) : BaseFilter(
    resources,
    fragmentShader = "shader/color/gray.frag"
)