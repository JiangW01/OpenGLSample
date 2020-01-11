package com.opengl.sample.fbo

import android.opengl.GLES20
import android.R.attr.bitmap
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.opengl.ETC1Util.createTexture
import android.util.Log
import com.opengl.sample.utils.loadTexture


/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/9
 * 描述 ：
 */
class FBOHelper(private val bitmap: Bitmap) {

    private var fboId = 0
    private var fboTextureId = 0




     fun createFBO() {

        //1. 创建FBO
        val fbos = IntArray(1)
        GLES20.glGenFramebuffers(1, fbos, 0)
        fboId = fbos[0]
        //2. 绑定FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId)

        //3. 创建FBO纹理
         val fboTextureIds = IntArray(1)
         GLES20.glGenTextures(1, fboTextureIds, 0)
         fboTextureId = fboTextureIds[0]
         GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTextureId)


         //5.绑定一个空的glTexImage2D，方便framebuffer 填充数据
         GLES20.glTexImage2D(
             GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.width, bitmap.height,
             0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
         )

         GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
         GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
         GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
         GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

         //4. 把纹理绑定到FBO
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, fboTextureId, 0
        )

        //6. 检测是否绑定从成功
        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            Log.e("zzz", "glFramebufferTexture2D error")
        }
        //7. 解绑纹理和FBO
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }


    fun bindFramebuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId)
    }

    fun unbindFramebuffer(){
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    fun getFboTextureId():Int {
        return fboTextureId
    }
}