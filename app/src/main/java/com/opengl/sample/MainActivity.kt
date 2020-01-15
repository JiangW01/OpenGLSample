package com.opengl.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.opengl.sample.video.decode.VideoDecoder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){



    private val filePath = "/storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1577186084568.mp4"
//    private val filePath = "/storage/emulated/0/DCIM/Camera/94c696ac8913189d39cdd17cb79fa36f.mp4"


    private lateinit var videoDecoder: VideoDecoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(GLTextureView(this))

//        val eglSurfaceView = EGLSurfaceView(this)
//        eglSurfaceView.setRender(EGLRenderer(this))
//        setContentView(eglSurfaceView)

//        //离屏渲染（后台处理一张bitmap）
//
//        val bitmap = bitmapRenderer.buildBitmap()
//        val imageView = ImageView(this)
//        imageView.setImageBitmap(bitmap)
//        setContentView(imageView)
        setContentView(R.layout.activity_main)
        videoDecoder = VideoDecoder()
        videoDecoder.setDataSource(filePath)
//        videoDecoder.start()
//        videoDecoder.addCallback {
//            image.setImageBitmap(it)
//        }
//        surfaceview.setEGLContextClientVersion(2)
//        surfaceview.setRenderer(GLTextureRender(this))
//
//        val bitmapRenderer = BitmapRender(this)
//        val bitmap = bitmapRenderer.getBitmap()
//        image.setImageBitmap(bitmap)
    }




}
