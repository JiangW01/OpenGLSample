package com.opengl.sample.video.decode

import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMetadataRetriever
import android.os.Build
import android.text.TextUtils
import java.io.IOException

/**
 * 作者 ：wangJiang
 * 时间 ：2019/7/26
 * 描述 ：
 */
class MediaHelper: IDecoder {

    private val retriever = MediaMetadataRetriever()

    private var durationUs: Long = 0

    var extractor: MediaExtractor = MediaExtractor()
    var checkExtractor: MediaExtractor = MediaExtractor()
    lateinit var videoFormat: MediaFormat
    private var videoWidth = 0
    private var videoHeight = 0
    private var rotate = 0

    var mine:String = ""

    var firstSampleTime  = 0L
    var perFrameTime  = 0L
    var isFlip = false

    var fps = 0

    @Throws(IOException::class)
    override fun setDataSource(filePath: String) {
//        retriever.setDataSource(filePath)
//        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//        if (!duration.isNullOrEmpty() && TextUtils.isDigitsOnly(duration)) {
//            durationUs = java.lang.Long.parseLong(duration)
//            println("video durationUs = $durationUs")
//        }
        val mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(filePath)
//        for (i in 0 until mediaExtractor.trackCount){
//            val format = mediaExtractor.getTrackFormat(i)
//            val mime = format.getString(MediaFormat.KEY_MIME)
//            if(mime.startsWith("video")){
//                extractor.setDataSource(filePath)
//                extractor.selectTrack(i)
//                checkExtractor.setDataSource(filePath)
//                checkExtractor.selectTrack(i)
//                checkExtractor.seekTo(0L,MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
//                firstSampleTime = extractor.sampleTime
//                println("video firstSampleTime = $firstSampleTime")
//                videoFormat = format
//                if (format.containsKey(MediaFormat.KEY_MIME)) {
//                    mine = videoFormat.getString(MediaFormat.KEY_MIME)
//                }
//                if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
//                    fps = format.getInteger(MediaFormat.KEY_FRAME_RATE)
//                }
//                perFrameTime = checkExtractor.calculatePerFrameTime(fps)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && format.containsKey(MediaFormat.KEY_ROTATION)) {
//                    rotate = format.getInteger(MediaFormat.KEY_ROTATION)
//                }
//                if (rotate % 180 == 0) {
//                    videoWidth = format.getInteger(MediaFormat.KEY_WIDTH)
//                    videoHeight = format.getInteger(MediaFormat.KEY_HEIGHT)
//                } else {
//                    videoHeight = format.getInteger(MediaFormat.KEY_WIDTH)
//                    videoWidth = format.getInteger(MediaFormat.KEY_HEIGHT)
//                    isFlip = true
//                }
//                println("video isFlip = $isFlip")
//                println("video rotate = $rotate")
//                println("video videoHeight = $videoHeight")
//                println("video videoWidth = $videoWidth")
//                break
//            }
//        }
    }

    override fun getDuration(): Long {
        return durationUs
    }

    override fun getWidth(): Int {
        return videoWidth
    }

    override fun getHeight(): Int {
        return videoHeight
    }

    private fun Long.sameFrame(time: Long): Boolean {
        return time >= this - firstSampleTime &&
                time < this + perFrameTime - firstSampleTime
    }

    private fun MediaExtractor.calculatePerFrameTime(fps: Int): Long {
        val fpsPerFrameTime = if (fps != 0) 1000000L / fps else 0
        fun closeToFps(time: Long): Boolean {
            return if (fps != 0) {
                Math.abs(fpsPerFrameTime - time) < 10000
            } else {
                true
            }
        }

        var time: Long
        seekTo(0L, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        val startTime = sampleTime
        var minTime = -1L
        while (advance()) {
            time = sampleTime - startTime
            if (minTime == -1L || time < minTime) {
                minTime = time
                if (closeToFps(time)) {
                    return time
                }
            }
            if (sampleFlags == MediaExtractor.SAMPLE_FLAG_SYNC) {
                break
            }
        }
        return fpsPerFrameTime
    }

    override fun release() {
        retriever.release()
        extractor.release()
        checkExtractor.release()
    }


    fun preSyncTime(timeUs: Long): Long {
        checkExtractor.seekTo(timeUs, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        return getPreSyncTime()
    }

    /**
     * 获取当前帧是关键帧的时间
     */
    fun getPreSyncTime():Long{
        if (checkExtractor.sampleFlags and MediaExtractor.SAMPLE_FLAG_SYNC == MediaExtractor.SAMPLE_FLAG_SYNC) {
            return checkExtractor.sampleTime
        }
        checkExtractor.advance()
        return getPreSyncTime()
    }

}