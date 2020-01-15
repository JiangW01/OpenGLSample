package com.opengl.sample.video.decode.utils

import android.media.MediaCodec
import android.os.Build
import java.nio.ByteBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2019/7/5
 * 描述 ：
 */

fun MediaCodec.getCompatInputBuffer(index: Int): ByteBuffer {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getInputBuffer(index)!!
    } else {
        inputBuffers[index]
    }
}


fun MediaCodec.getCompatOutputBuffer(index: Int): ByteBuffer {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getOutputBuffer(index)!!
    } else {
        outputBuffers[index]
    }
}