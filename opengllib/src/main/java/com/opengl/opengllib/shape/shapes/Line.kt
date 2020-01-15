package com.opengl.opengllib.shape.shapes

import android.content.res.Resources
import android.opengl.GLES20
import com.opengl.opengllib.shape.BaseShape
import com.opengl.opengllib.utils.*
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/15
 * 描述 ：
 */
class Line(resources: Resources) : BaseShape(resources) {

    init {
        vertexCoods = floatArrayOf(
            -0.5f, 0.5f,//左上
            0.5f, 0.5f,//右上
            0.5f, -0.5f,//右下
            -0.5f, -0.5f//左下
        )
        colors = floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)// 红色
        vertexShaderAssetsFileName = "shader/shapes/line.vert"
        fragShaderAssetsFileName = "shader/shapes/base_shape.frag"
    }

    override fun render() {
        GLES20.glLineWidth(10f)
        drawArrays(GL_LINES)
    }


}