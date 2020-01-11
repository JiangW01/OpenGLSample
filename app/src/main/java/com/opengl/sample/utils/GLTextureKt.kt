package com.opengl.sample.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log
import java.lang.Exception
import java.nio.Buffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/6
 * 描述 ：
 */


/**
 * 加载纹理
 *@param context 上下文
 * @param fileName 文件名
 * @param renderTypeS 设置s轴拉伸方式---GLES20.GL_CLAMP_TO_EDGE、GLES20.GL_REPEAT
 * @param renderTypeT 设置t轴拉伸方式---GLES20.GL_CLAMP_TO_EDGE、GLES20.GL_REPEAT
 *
 * s轴是从（0，0）->（1，0）
 * t轴是从（0，0）->（0，1）
 *
 */
inline fun loadTextureFromAssets(
    context: Context,
    fileName: String,
    renderTypeS: Int = GLES20.GL_CLAMP_TO_EDGE,
    renderTypeT: Int = GLES20.GL_CLAMP_TO_EDGE
): Int {
    val textureId = loadTexture(renderTypeS,renderTypeT)
    val inputStream = context.assets.open(fileName)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    if (bitmap == null) {
        Log.e("GL", "Could not load bitmap")
        return 0
    }
    //实际加载纹理(纹理类型,纹理的层次,纹理图像,纹理边框尺寸)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    //解绑纹理
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    return textureId
}

/**
 * 从资源中加载图片尺寸
 *@param context 上下文
 *@param fileName 文件名
 *@return size [width,height]
 **/
inline fun loadBitmapSizeFromAssets(context: Context, fileName: String):Pair<Int,Int>{
    try {
        val inputStream = context.assets.open(fileName)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap != null) {
            Log.e("GL", "Could not load bitmap")
            return Pair(bitmap.width,bitmap.height)
        }
        inputStream.close()
    }catch (e:Exception){
        e.printStackTrace()
    }
    return Pair(0,0)
}

inline fun loadTextureFromBitmap(
    bitmap: Bitmap,
    renderTypeS: Int = GLES20.GL_CLAMP_TO_EDGE,
    renderTypeT: Int = GLES20.GL_CLAMP_TO_EDGE
): Int {
    val textureId = loadTexture(renderTypeS,renderTypeT)
    //实际加载纹理(纹理类型,纹理的层次,纹理图像,纹理边框尺寸)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    //解绑纹理
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    return textureId
}

inline fun loadTexture(
    renderTypeS: Int = GLES20.GL_CLAMP_TO_EDGE,
    renderTypeT: Int = GLES20.GL_CLAMP_TO_EDGE
): Int {
    val textureIds = IntArray(1)
    GLES20.glGenTextures(1, textureIds, 0)
    if (textureIds[0] == 0) {
        Log.e("GL", "Could not generate a new OpenGL texture object")
        return 0
    }
    val textureId = textureIds[0]
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
    GLES20.glTexParameterf(
        GLES20.GL_TEXTURE_2D,
        GLES20.GL_TEXTURE_MIN_FILTER,
        GLES20.GL_NEAREST.toFloat()
    )
    GLES20.glTexParameterf(
        GLES20.GL_TEXTURE_2D,
        GLES20.GL_TEXTURE_MAG_FILTER,
        GLES20.GL_LINEAR.toFloat()
    )
    //设置s轴拉伸方式---重复
    GLES20.glTexParameterf(
        GLES20.GL_TEXTURE_2D,
        GLES20.GL_TEXTURE_WRAP_S,
        renderTypeS.toFloat()
    )
    //设置t轴拉伸方式---重复
    GLES20.glTexParameterf(
        GLES20.GL_TEXTURE_2D,
        GLES20.GL_TEXTURE_WRAP_T,
        renderTypeT.toFloat()
    )
    return textureId
}
