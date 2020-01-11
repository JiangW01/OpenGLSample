package com.opengl.sample.fbo

import android.opengl.GLES20

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/11
 * 描述 ：
 */
class FrameBufferHelper {

    private var mFrameTemp :IntArray = IntArray(4)
    private var lastWidth = 0
    private var lastHeight = 0

    /**
     * 创建FrameBuffer
     * @param width 宽度
     * @param height 高度
     * @param hasRenderBuffer 是否启用RenderBuffer
     * @param texType 类型，一般为[GLES20.GL_TEXTURE_2D]
     * @param texFormat 纹理格式，一般为[GLES20.GL_RGBA]、[GLES20.GL_RGB]等
     * @param minParams 纹理的缩小过滤参数
     * @param maxParams 纹理的放大过滤参数
     * @param wrapS 纹理的S环绕参数
     * @param wrapT 纹理的W环绕参数
     * @return 创建结果，0表示成功，其他值为GL错误
     */
    fun createFrameBuffer(
        width: Int,
        height: Int,
        hasRenderBuffer: Boolean = false,
        texType: Int = GLES20.GL_TEXTURE_2D,
        texFormat: Int = GLES20.GL_RGBA,
        minParams: Int = GLES20.GL_LINEAR,
        maxParams: Int = GLES20.GL_LINEAR,
        wrapS: Int = GLES20.GL_CLAMP_TO_EDGE,
        wrapT: Int = GLES20.GL_CLAMP_TO_EDGE
    ): Int {
        mFrameTemp = IntArray(4)
        //1. 创建FBO
        GLES20.glGenFramebuffers(1, mFrameTemp, 0)
        //2. 创建FBO纹理
        GLES20.glGenTextures(1, mFrameTemp, 1)
        //3. 绑定FBO纹理
        GLES20.glBindTexture(texType, mFrameTemp[1])
        //4.绑定一个空的glTexImage2D，方便framebuffer 填充数据
        GLES20.glTexImage2D(
            texType, 0, texFormat, width, height,
            0, texFormat, GLES20.GL_UNSIGNED_BYTE, null
        )
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameteri(texType, GLES20.GL_TEXTURE_MIN_FILTER, minParams)
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameteri(texType, GLES20.GL_TEXTURE_MAG_FILTER, maxParams)
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameteri(texType, GLES20.GL_TEXTURE_WRAP_S, wrapS)
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameteri(texType, GLES20.GL_TEXTURE_WRAP_T, wrapT)

        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, mFrameTemp, 3)
        //5. 绑定FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameTemp[0])
        //6. 把纹理绑定到FBO
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            texType, mFrameTemp[1], 0
        )
        if (hasRenderBuffer) {
            GLES20.glGenRenderbuffers(1, mFrameTemp, 2)
            GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, mFrameTemp[2])
            GLES20.glRenderbufferStorage(
                GLES20.GL_RENDERBUFFER,
                GLES20.GL_DEPTH_COMPONENT16,
                width,
                height
            )
            GLES20.glFramebufferRenderbuffer(
                GLES20.GL_FRAMEBUFFER,
                GLES20.GL_DEPTH_ATTACHMENT,
                GLES20.GL_RENDERBUFFER,
                mFrameTemp[2]
            )
        }
        return GLES20.glGetError()
    }



    /**
     * 绑定到FrameBuffer
     * @param width 宽度
     * @param height 高度
     * @param hasRenderBuffer 是否使用renderBuffer 默认false
     * @return 绑定结果，0表示成功，其他值为GL错误
     */
    fun bindFrameBuffer(width: Int, height: Int, hasRenderBuffer: Boolean = false): Int {
        if (lastWidth != width || lastHeight != height) {
            destroyFrameBuffer()
            this.lastWidth = width
            this.lastHeight = height
        }
        return if (isCreate()) {
            createFrameBuffer(width, height,hasRenderBuffer)
        } else {
            bindFrameBuffer()
        }
    }

    private fun isCreate():Boolean{
        return mFrameTemp[0] > 0
    }


    /**
     * 绑定FrameBuffer，只有之前创建过FrameBuffer，才能调用此方法进行绑定
     * @return 绑定结果
     */
    fun bindFrameBuffer(): Int {
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, mFrameTemp, 3)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameTemp[0])
        return GLES20.glGetError()
    }

    /**
     * 取消FrameBuffer绑定
     */
    fun unBindFrameBuffer() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameTemp[3])
    }

    /**
     * 获取绘制再FrameBuffer中的内容
     * @return FrameBuffer绘制内容的纹理ID
     */
    fun getCacheTextureId(): Int {
        return mFrameTemp[1]
    }

    /**
     * 销毁FrameBuffer
     */
    fun destroyFrameBuffer() {
        GLES20.glDeleteFramebuffers(1, mFrameTemp, 0)
        GLES20.glDeleteTextures(1, mFrameTemp, 1)
        if (mFrameTemp[2] > 0) {
            GLES20.glDeleteRenderbuffers(1, mFrameTemp, 2)
        }
        mFrameTemp.forEachIndexed { index, i ->
            mFrameTemp[index] = 0
        }
    }

}