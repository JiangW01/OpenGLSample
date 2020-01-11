package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import kotlin.math.cos
import kotlin.math.sin

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/6
 * 描述 ：
 */
class WorldShape(context: Context) : BaseShape(context) {


    private val shapes = mutableListOf<BaseShape>()


    init {
        initFrameShape()
        initCoordomateShape()
        shapes.add(TextureCone(context))
//        shapes.add(TextureCylinder(context))
//        shapes.add(TextureCircle(context))
//        shapes.add(BallShape(context))
//        initCircle()
//        initFrontRectShape()
    }


    private fun initCircle() {
        val innerRadius = 0.5f //内圆半径
        val radius = 1.0f//外圆半径
        val splitCount = 180
        val vertexCount = splitCount * 2 + 2
        val vertexs = FloatArray(vertexCount * 3)
        val singleAngle = 360f / splitCount
        for (i in 0 until vertexCount) {
            val angle = (i / 2) * singleAngle
            if (i % 2 == 0) { //偶数内圆
                val x: Float = (cos(Math.toRadians(angle.toDouble())) * innerRadius).toFloat()
                val y: Float = (sin(Math.toRadians(angle.toDouble())) * innerRadius).toFloat()
                vertexs[i*3 + 0] = x
                vertexs[i*3 + 1] = y
                vertexs[i*3 + 2] = 0f
            }else{//奇数外圆
                val x: Float = (cos(Math.toRadians(angle.toDouble())) * radius).toFloat()
                val y: Float = (sin(Math.toRadians(angle.toDouble())) * radius).toFloat()
                vertexs[i*3 + 0] = x
                vertexs[i*3 + 1] = y
                vertexs[i*3 + 2] = 0f
            }
        }
        val colors = FloatArray(vertexCount*4)
        for (i in 0 until vertexCount step 2) {
            colors[i*4 + 0] = 1f
            colors[i*4 + 1] = 1f
            colors[i*4 + 2] = 1f
            colors[i*4 + 3] = 1f
            colors[(i+1)*4 + 0] = 0.972549f
            colors[(i+1)*4 + 1] = 0.5019608f
            colors[(i+1)*4 + 2] = 0.09411765f
            colors[(i+1)*4 + 3] = 1f
        }
        val circle = ArrayShape(context, vertexs, colors, GLES20.GL_TRIANGLE_STRIP)
        shapes.add(circle)
    }


    //正面的矩形
    private fun initFrontRectShape() {
        val vertexs = floatArrayOf(
            1f, 1f, 1f,//A
            -1f, 1f, 1f,//B
            -1f, -1f, 1f,//C
            1f, -1f, 1f//D
        )
        val indexs = shortArrayOf(
            0, 1, 2,//ABC
            0, 2, 3 //ACD
        )
        val colors = floatArrayOf(
            1f, 0f, 0f, 1.0f,
            1f, 1f, 0f, 1.0f,
            0f, 1f, 0f, 1.0f,
            0f, 0f, 1f, 1.0f
        )
        val frontRect = ElementShape(context, vertexs, indexs, colors, GLES20.GL_TRIANGLES)
        shapes.add(frontRect)
    }


    //坐标系的外框
    private fun initCoordomateShape() {
        val vertexs = floatArrayOf(
            0f, 0f, 0f,//x轴
            1f, 0f, 0f,
            0f, 0f, 0f,//y轴
            0f, 1f, 0f,
            0f, 0f, 0f,//z轴
            0f, 0f, 1f
        )
        val colors = floatArrayOf(
            1f, 1f, 0f, 1.0f, //x轴黄色
            1f, 1f, 0f, 1.0f,
            0f, 1f, 0f, 1.0f, //y轴绿色
            0f, 1f, 0f, 1.0f,
            0f, 0f, 1.0f, 1.0f, //z轴蓝色
            0f, 0f, 1.0f, 1.0f
        )
        val coordinateShape = ArrayShape(context, vertexs, colors, GLES20.GL_LINES)
        shapes.add(coordinateShape)
    }


    //坐标系的外框
    private fun initFrameShape() {
        val vertexs = floatArrayOf(
            1f, 1f, 1f,//AB
            -1f, 1f, 1f,
            -1f, 1f, 1f,//BC
            -1f, -1f, 1f,
            -1f, -1f, 1f,//CD
            1f, -1f, 1f,
            1f, 1f, 1f,//AD
            1f, -1f, 1f,
            1f, 1f, 1f,//AE
            1f, 1f, -1f,
            -1f, 1f, 1f,//BF
            -1f, 1f, -1f,
            -1f, -1f, 1f,//CG
            -1f, -1f, -1f,
            1f, -1f, 1f,//DH
            1f, -1f, -1f,
            1f, 1f, -1f,//EF
            -1f, 1f, -1f,
            -1f, 1f, -1f,//FG
            -1f, -1f, -1f,
            -1f, -1f, -1f,//GH
            1f, -1f, -1f,
            1f, 1f, -1f,//EH
            1f, -1f, -1f
        )

        val colors = floatArrayOf(
            1f, 1f, 1f, 1f,//AB
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//BC
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//CD
            1f, 1f, 1f, 1f,
            1f, 0f, 0f, 1f,//AD
            1f, 0f, 0f, 1f,
            1f, 1f, 1f, 1f,//AE
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//BF
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//CG
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//DH
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//EF
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//FG
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//GH
            1f, 1f, 1f, 1f,
            1f, 1f, 1f, 1f,//EH
            1f, 1f, 1f, 1f
        )
        val frameShape = ArrayShape(context, vertexs, colors, GLES20.GL_LINES)
        shapes.add(frameShape)
    }


    override fun setup() {
        shapes.forEach {
            it.setup()
        }
    }


    override fun draw(mvpMatrix: FloatArray?) {
        shapes.forEach {
            it.draw(mvpMatrix)
        }
    }
}