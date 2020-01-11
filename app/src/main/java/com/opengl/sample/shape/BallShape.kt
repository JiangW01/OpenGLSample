package com.opengl.sample.shape

import android.content.Context
import android.opengl.GLES20
import com.opengl.sample.utils.*
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/8
 * 描述 ：
 *  球体
 */
class BallShape(context: Context) : BaseShape(context) {

    private var vertexsCache = mutableListOf<Float>()
    private lateinit var vertexs: FloatArray
    private lateinit var vertexBuffer: FloatBuffer


    private var program: Int = 0
    private var positionHandle: Int = 0

    private val radius = 0.8f

    override fun setup() {
        vertexBuffer = asFloatBuffer(vertexs)
        val vertexShader = loadVertexShaderAssets(context, "\n\n\nattribute vec3 aPosition;\nuniform mat4 uMVPMatrix;\nvarying vec3 vPosition;\nuniform vec4 uAmbient;\nvarying vec4 vAmbient;//用于传递给片元着色器的环境光分量\nvoid main(){\n   gl_Position = uMVPMatrix * vec4(aPosition,1);\n   //将顶点的位置传给片元着色器\n   vPosition = aPosition;//将原始顶点位置传递给片元着色器\n   //将的环境光分量传给片元着色器\n   vAmbient = vec4(uAmbient);\n\n}\n\n")//顶点着色
        val fragmentShader = loadFragShaderAssets(context, "\n\n\nprecision mediump float;\nuniform float uR;\nvarying vec3 vPosition;//接收从顶点着色器过来的顶点位置\nvarying vec4 vAmbient;//接收从顶点着色器过来的环境光分量\nvoid main(){\n    vec3 color;\n        float n = 8.0;//一个坐标分量分的总份数\n        float span = 2.0*uR/n;//每一份的长度\n        //每一维在立方体内的行列数\n        int i = int((vPosition.x + uR)/span);\n        int j = int((vPosition.y + uR)/span);\n        int k = int((vPosition.z + uR)/span);\n        //计算当点应位于白色块还是黑色块中\n        int whichColor = int(mod(float(i+j+k),2.0));\n        if(whichColor == 1) {//奇数时\n        \t\tcolor = vec3(0.16078432f,0.99215686f,0.02745098f);//绿\n        } else {//偶数时为白色\n        \t\tcolor = vec3(1.0,1.0,1.0);//白色\n        }\n        vec4 finalColor = vec4(color,0);\n        //将计算出的颜色给此片元\n        gl_FragColor=finalColor*vAmbient;\n}")//片元着色
        program = glCreateProgram(vertexShader, fragmentShader)
    }

    init {
        initVertexs(radius, 32, 16)
//        vertexs = floatArrayOf(
//            0f, 0f, 0f,
//            1f, 1f, 1f
//        )
    }

    /**
     * 初始化顶点数据
     * @param r 球体半径
     * @param longitudeSplitCount 经度分割的数 [-0 -> + 360]
     * @param latitudeSplitCount 纬度分割的数 [-90 -> + 90]
     */
    fun initVertexs(r: Float, longitudeSplitCount: Int, latitudeSplitCount: Int) {
        val longitudeAngle = 360f / longitudeSplitCount //经度的单位分量
        val latitudeAngle = 180f / latitudeSplitCount //纬度的单位分量

        // x: r*cos(a)*cos(b)
        // y: r*cos(a)*sin(b)
        // z: r*sin(a)
        var a = -90f
        var b = 0f
        while (a >= -90f && a < 90f) {
            while (b < 360f){

                println("vertex longitudeAngle = $longitudeAngle latitudeAngle = $latitudeAngle a = $a b = $b")

                val x0 = r * cosR(a) * cosR(b)
                val y0 = r * cosR(a) * sinR(b)
                val z0 = r * sinR(a)

                val x1 = r * cosR(a) * cosR(b + longitudeAngle)
                val y1 = r * cosR(a) * sinR(b + longitudeAngle)
                val z1 = r * sinR(a)

                val x2 = r * cosR(a + latitudeAngle) * cosR(b + longitudeAngle)
                val y2 = r * cosR(a + latitudeAngle) * sinR(b + longitudeAngle)
                val z2 = r * sinR(a + latitudeAngle)

                val x3 = r * cosR(a + latitudeAngle) * cosR(b)
                val y3 = r * cosR(a + latitudeAngle) * sinR(b)
                val z3 = r * sinR(a + latitudeAngle)
                vertexsCache.add(x0)
                vertexsCache.add(y0)
                vertexsCache.add(z0)
                vertexsCache.add(x1)
                vertexsCache.add(y1)
                vertexsCache.add(z1)
                vertexsCache.add(x3)
                vertexsCache.add(y3)
                vertexsCache.add(z3)
                vertexsCache.add(x1)
                vertexsCache.add(y1)
                vertexsCache.add(z1)
                vertexsCache.add(x2)
                vertexsCache.add(y2)
                vertexsCache.add(z2)
                vertexsCache.add(x3)
                vertexsCache.add(y3)
                vertexsCache.add(z3)
                b += longitudeAngle
            }
            b = 0f
            a += latitudeAngle
        }
        vertexs = FloatArray(vertexsCache.size)
        vertexsCache.forEachIndexed { index, fl ->
            vertexs[index] = fl
        }
    }

    fun cosR(angle: Float): Float {
        return cos(toRadians(angle)).toFloat()
    }

    fun sinR(angle: Float): Float {
        return sin(toRadians(angle)).toFloat()
    }

    private fun toRadians(angle: Float): Double {
        return Math.toRadians(angle.toDouble())
    }

    fun cosR(angle: Int): Float {
        return cos(toRadians(angle)).toFloat()
    }

    fun sinR(angle: Int): Float {
        return sin(toRadians(angle)).toFloat()
    }

    private fun toRadians(angle: Int): Double {
        return Math.toRadians(angle.toDouble())
    }

    private var urHandle=0

    override fun draw(mvpMatrix: FloatArray?) {
        GLES20.glUseProgram(program)
        positionHandle = glUseAttribute(program, "aPosition", vertexBuffer)
        mvpMatrix?.let {
            val matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
            GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mvpMatrix, 0)
        }
        glUniform4f(program,"uAmbient",1f,1f,1f,1f)
        urHandle = GLES20.glGetUniformLocation(program, "uR")
        GLES20.glUniform1f(urHandle, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexs.size / COORDS_VERTEX_3D)
        GLES20.glLineWidth(5f)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}