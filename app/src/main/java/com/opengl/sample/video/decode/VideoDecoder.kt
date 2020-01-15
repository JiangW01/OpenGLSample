package com.opengl.sample.video.decode

import android.graphics.Bitmap
import android.media.MediaCodec
import android.media.MediaExtractor
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.Surface
import com.opengl.sample.video.decode.filter.BaseFilter
import com.opengl.sample.video.decode.render.TextureRender
import com.opengl.sample.video.decode.utils.getCompatInputBuffer
import java.io.IOException

/**
 * 作者 ：wangJiang
 * 时间 ：2019/7/26
 * 描述 ：
 */
class VideoDecoder(var filter: BaseFilter = BaseFilter()) : Runnable, IDecoder {


    private var threadFlag = false
    private lateinit var thread: Thread
    private var pause: Boolean = false
    private var currentPtsTime: Long = 0
    private var currentReferenceTime: Long = 0
    private val timeout = 0L
    private val CONTROL_LOCK = Object()
    private val DECODE_LOCK = Object()
    private val SEEK_LOCK = Object()
    private val SEEK_LIMIT_TIME: Long = 400000
    private var tryToSeekTo: Long = -1
    private val loop = true
    private var seekToTimeUs: Long = -1L

    private var filePath: String = ""

    private val mediaHelper = MediaHelper()

    private lateinit var codec: MediaCodec
    private var surfaceWidth: Int = 0
    private var surfaceheight: Int = 0


    private val extractor by lazy {
        mediaHelper.extractor
    }

    private var currentUpdateTime: Long = 0


    private var callback: ((bitmap: Bitmap) -> Unit)? = null


    private val decodeHandler: DecodeHandler by lazy {
        DecodeHandler(Looper.getMainLooper())
    }


    private val textureRender by lazy {
        val w = if (surfaceWidth <= 0) getWidth() else surfaceWidth
        val h = if (surfaceheight <= 0) getHeight() else surfaceheight
        TextureRender(w, h, filter)
    }


    private lateinit var surface: Surface


    fun setSurfaceSize(width: Int, height: Int) {
        this.surfaceWidth = width
        this.surfaceheight = height
    }


    inner class DecodeHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.obj is Bitmap) {
                println("decode handleMessage")
                callback?.invoke((msg.obj as Bitmap))
            }
        }
    }

    fun addCallback(callback:((bitmap: Bitmap) -> Unit)?){
        this.callback = callback
    }


    @Throws(IOException::class)
    override fun setDataSource(filePath: String) {
        this.filePath = filePath
        mediaHelper.setDataSource(filePath)
    }

    override fun release() {
        mediaHelper.release()
        if (::codec.isInitialized) {
            codec.release()
        }
    }

    override fun getDuration(): Long {
        return mediaHelper.getDuration()
    }

    override fun getWidth(): Int {
        return mediaHelper.getWidth()
    }

    override fun getHeight(): Int {
        return mediaHelper.getHeight()
    }



    fun start() {
        if (threadFlag) {
            synchronized(CONTROL_LOCK) {
                pause = false
                CONTROL_LOCK.notifyAll()
            }
        } else {
            if (seekToTimeUs === -1L) {
                seekTo(0)
            }
            pause = false
            threadFlag = true
            thread = Thread(this)
            thread.start()
        }
    }


    override fun run() {
        val codec: MediaCodec
        try {
            codec = MediaCodec.createDecoderByType(mediaHelper.mine)
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
        surface = Surface(textureRender.getSurfaceTexture())
        codec.configure(mediaHelper.videoFormat, surface, null, 0)
        codec.start()
        while (threadFlag) {
            checkAndDoSeek(codec)
            val info = MediaCodec.BufferInfo()
            if (checkAndDoPause())
                continue
            codecInput(codec)
            while (true){
                val outputIndex: Int = codec.dequeueOutputBuffer(info, timeout)
                if (outputIndex >= 0) {
                    currentPtsTime = info.presentationTimeUs
                    if (tryToSeekTo != -1L) {
                        var reslut = false
                        synchronized(CONTROL_LOCK) {
                            //如果当前解码的时间未到定位的时间点 或者 当前更新的时间超前了当前解码的时间，都需要继续解码
                            if (currentPtsTime < tryToSeekTo || currentReferenceTime - currentPtsTime > SEEK_LIMIT_TIME) {
                                codec.releaseOutputBuffer(outputIndex, false)
                                reslut = true
                            }
                        }
                        if (reslut) continue
                        synchronized(SEEK_LOCK) {
                            tryToSeekTo = -1
                            SEEK_LOCK.notifyAll()
                        }
                    }
                    if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM == 0 && currentPtsTime > currentReferenceTime) {
                        //如果当前视频解码速度太快，则等待刷新时间到来
                        synchronized(DECODE_LOCK) {
                            if (threadFlag) {
                                try {
                                    DECODE_LOCK.wait()
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }else if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                        if (loop) {
                            seekTo(0)
                        } else {
                            threadFlag = false
                        }
                        break
                    }
                    codec.releaseOutputBuffer(outputIndex, true)
                    textureRender.render()
                    val bitmap = textureRender.produceBitmap()
                    sendBitmap(bitmap)
                } else if (outputIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    val format = codec.outputFormat

                } else if (outputIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    break
                }
            }
        }
        codec.flush()
        codec.stop()
        release()
    }



    private fun sendBitmap(bitmap: Bitmap) {
        val message = Message.obtain()
        message.obj = bitmap
        decodeHandler.sendMessage(message)
    }


    fun seekTo(timeUs: Long) {
        synchronized(CONTROL_LOCK) {
            seekToTimeUs = timeUs
            //当前如果是暂停状态需要跳过暂停
            CONTROL_LOCK.notifyAll()
        }
        //当前如果是等待解码状态需要跳过等待，seek后更新图像
        synchronized(DECODE_LOCK) {
            DECODE_LOCK.notifyAll()
        }
    }


    fun pause() {
        synchronized(CONTROL_LOCK) {
            this.pause = true
        }
    }


    private fun checkAndDoPause(): Boolean {
        if (pause) {
            synchronized(CONTROL_LOCK) {
                try {
                    CONTROL_LOCK.wait()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
            return true
        }
        return false
    }

    fun stop() {
        threadFlag = false
        synchronized(CONTROL_LOCK) {
            pause = false
            CONTROL_LOCK.notifyAll()
        }
        synchronized(DECODE_LOCK) {
            DECODE_LOCK.notifyAll()
        }
        try {
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        currentPtsTime = 0
        currentReferenceTime = 0
        seekToTimeUs = -1
        tryToSeekTo = -1
    }



    private fun codecInput(codec: MediaCodec) {
        val inputIndex = codec.dequeueInputBuffer(timeout)
        if (inputIndex >= 0) {
            val inputBuffer = codec.getCompatInputBuffer(inputIndex)
            inputBuffer.position(0)
            val size = extractor.readSampleData(inputBuffer, 0)
            if (size == -1) {
                //todo 最后一帧处理
                codec.queueInputBuffer(inputIndex, 0, 0, extractor.sampleTime, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
            } else {
                codec.queueInputBuffer(inputIndex, 0, size, extractor.sampleTime, extractor.sampleFlags)
            }
            mediaHelper.extractor.advance()
        }
    }


    private var seekOffset = 0L
    private fun checkAndDoSeek(codec: MediaCodec) {
        if (seekToTimeUs >= 0 && tryToSeekTo == -1L) {
            synchronized(CONTROL_LOCK) {
                codec.flush()
                extractor.seekTo(seekToTimeUs, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
                val result = extractor.sampleTime
                if(result < 0){
                    if(seekToTimeUs > mediaHelper.getDuration()*1000L){
                        return
                    }
                    seekOffset += 1000L
                    seekToTimeUs = seekOffset
                    checkAndDoSeek(codec)
                }else{
                    tryToSeekTo = seekToTimeUs
                    seekToTimeUs = -1
                }
            }
        }
    }

    fun update(timeUs: Long): Boolean {
        if (tryToSeekTo != -1L) {
            synchronized(SEEK_LOCK) {
                try {
                    SEEK_LOCK.wait()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        //ITimeObserver的接口，通常由音频解码器或者播放器调用，以达到音视频同步的目的
        currentReferenceTime = timeUs
        val interval = currentPtsTime - currentReferenceTime
        if (interval > SEEK_LIMIT_TIME) {
            if (seekToTimeUs === -1L && tryToSeekTo == -1L) {
                //视频解码太前时，seek回音频播放处，主要用于处理视频总长度大于音频的情况
                seekTo(currentReferenceTime)
                synchronized(DECODE_LOCK) {
                    DECODE_LOCK.notifyAll()
                }
            }
        } else if (interval < 0) {
            //视频时间戳小于音频时间戳时，通知处理并继续解码下一帧
            synchronized(DECODE_LOCK) {
                DECODE_LOCK.notifyAll()
            }
        }
        return true
    }

}
