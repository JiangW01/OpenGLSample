package com.opengl.sample.utils

import android.opengl.Matrix
import com.opengl.sample.texture.ScaleType
import kotlin.math.max
import kotlin.math.min

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/10
 * 描述 ：
 */


/**
 * 获取一个新的原始顶点坐标，
 * 每次调用，
 * 都会重新创建
 * @return 坐标数组
 */
inline fun getOriginalVertexCo(): FloatArray {
    return floatArrayOf(
        -1.0f, -1.0f,//left,bottom
        -1.0f, 1.0f,//left ,top
        1.0f, -1.0f,//right,bottom
        1.0f, 1.0f //right,top
    )
}

/**
 * 获取一个新的原始纹理坐标，每次调用，都会重新创建
 * @return 坐标数组
 */
inline fun getOriginalTextureCo(): FloatArray {
    return floatArrayOf(
        0.0f, 1.0f, //left,bottom
        0.0f, 0.0f, //left ,top
        1.0f, 1.0f,//right,bottom
        1.0f, 0.0f //right,top
    )
}

/**
 * 根据预览的大小和图像的大小，计算合适的变换矩阵
 * @param matrix  接收变换矩阵的数组
 * @param type 变换的类型，参考[.TYPE_CENTERCROP]、[.TYPE_FITEND]、[.TYPE_CENTERINSIDE]、[.TYPE_FITSTART]、[.TYPE_FITXY]，
 *             对应[android.widget.ImageView]的[android.widget.ImageView.setScaleType]
 * @param imgWidth 图像的宽度
 * @param imgHeight 图像的高度
 * @param viewWidth 视图的宽度
 * @param viewHeight 视图的高度
 *
 */
inline fun getMatrix(
    matrix: FloatArray,
    type: ScaleType,
    imgWidth: Int, imgHeight: Int,
    viewWidth: Int, viewHeight: Int
) {
    if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
        val projection = FloatArray(16)  //投影矩阵,平行投影
        val camera = FloatArray(16) //相机视野
        val aspectRatioView = viewWidth * 1f / viewHeight
        val aspectRatioImg = imgWidth * 1f / imgHeight
        if (aspectRatioImg > aspectRatioView) {
            //说明图片更加矮胖
            val aspectRatio = aspectRatioImg / aspectRatioView
            when (type) {
                ScaleType.FIT_XY -> {
                    orthoM(projection, -1f, 1f, -1f, 1f)
                }
                ScaleType.FIT_START -> {
                    orthoM(projection, -1f, 1f, 1 - 2 * aspectRatio, 1f)
                }
                ScaleType.FIT_END -> {
                    orthoM(projection, -1f, 1f, -1f, -(1 - 2 * aspectRatio))
                }
                ScaleType.FIT_CENTER -> {
                    orthoM(projection, -1f, 1f, -aspectRatio, aspectRatio)
                }
                ScaleType.CENTER, ScaleType.CENTER_INSIDE -> {
                    var ratioW = (viewWidth / 2f) / (imgWidth / 2f)
                    var ratioH = (viewHeight / 2f) / (imgHeight / 2f)
                    orthoM(projection, -ratioW, ratioW, -ratioH, ratioH)
                }
                ScaleType.CENTER_CROP -> {
                    orthoM(projection, -1 / aspectRatio, 1 / aspectRatio, -1f, 1f)
                }
            }
        } else {
            //说明图片更加瘦高
            val aspectRatio = aspectRatioView / aspectRatioImg
            when (type) {
                ScaleType.FIT_XY -> {
                    orthoM(projection, -1f, 1f, -1f, 1f)
                }
                ScaleType.FIT_START -> {
                    orthoM(projection, -1f, 2 * aspectRatio - 1, -1f, 1f)
                }
                ScaleType.FIT_END -> {
                    orthoM(projection, 1 - 2 * aspectRatio, 1f, -1f, 1f)
                }
                ScaleType.FIT_CENTER -> {
                    orthoM(projection, -aspectRatio, aspectRatio, -1f, 1f)
                }
                ScaleType.CENTER, ScaleType.CENTER_INSIDE -> {
                    var ratioW = (viewWidth / 2f) / (imgWidth / 2f)
                    var ratioH = (viewHeight / 2f) / (imgHeight / 2f)
                    orthoM(projection, -ratioW, ratioW, -ratioH, ratioH)
                }
                ScaleType.CENTER_CROP -> {
                    orthoM(projection, -1f, 1f, -1 / aspectRatio, 1 / aspectRatio)
                }
            }
        }
        setLookAtM(camera)
        println()
        multiplyMM(matrix, projection, camera)
    }
}


/**
 * 平行投影
 * @param projection 投影矩阵
 * @param left
 * @param right
 * @param bottom
 * @param top
 * @param near 近平面
 * @param far 远平面
 */
inline fun orthoM(
    projection: FloatArray,
    left: Float,
    right: Float,
    bottom: Float,
    top: Float,
    near: Float = 1f,
    far: Float = 3f
) {
    Matrix.orthoM(projection, 0, left, right, bottom, top, near, far)
}

/**
 * 设置相机视野
 * @param camera 相机矩阵
 * 相机固定在点（0，0，1）处,从实际效果来说就是屏幕中心的正前面看向屏幕
 * 且相机是竖直拍摄的
 */
inline fun setLookAtM(camera: FloatArray) {
    Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
}


/**
 * 矩阵相乘
 * @param result 结果矩阵
 * @param lhs 左乘矩阵
 * @param rhs 右乘矩阵
 */
inline fun multiplyMM(result: FloatArray, lhs: FloatArray, rhs: FloatArray) {
    Matrix.multiplyMM(result, 0, lhs, 0, rhs, 0)

}


