package com.opengl.opengllib.shape

import android.content.res.Resources
import android.opengl.GLES20
import com.opengl.opengllib.utils.*
import java.nio.FloatBuffer

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/15
 * 描述 ：
 */
abstract class BaseShape(val resources: Resources) :IShape{

    protected var vertexCoods = floatArrayOf()
        set(value) {
            vertexBuffer = asFloatBuffer(value)
            field = value
        }
    private var vertexBuffer: FloatBuffer = asFloatBuffer(vertexCoods)

    protected var colorCoods = floatArrayOf()
        set(value) {
            colorBuffer = asFloatBuffer(value)
            field = value
        }

    private var colorBuffer: FloatBuffer = asFloatBuffer(colorCoods)
    protected var colors = floatArrayOf()


    protected var vertexShaderAssetsFileName:String = ""
    protected var fragShaderAssetsFileName:String = ""


    private var positionHandle = 0
    private var colorHandle = 0
    protected var program:Int = 0




    override fun create(): Boolean {
        val vertexShader = loadVertexShaderAssets(resources, vertexShaderAssetsFileName)
        val fragShader = loadFragShaderAssets(resources, fragShaderAssetsFileName)
        program = glCreateProgram(vertexShader, fragShader)
        if (program > 0) {
            positionHandle = getAttribLocation(program, "aPosition")
            colorHandle = getUnifromLocation(program,"uColor")
        }
        return program > 0
    }

    override fun onSurfaceSizeChange(width: Int, height: Int) {
    }

    override fun render() {
        useProgram()
        useAttribute(positionHandle,vertexBuffer)
        GLES20.glUniform4fv(colorHandle,colors.size/4,colors,0)
        drawArrays(GL_POINTS,vertexCoods.size/ COORDS_VERTEX_2D)
        disableVertexAttribArray(positionHandle)
    }


    /**
     * 绘制单色的图形
     */
    protected fun drawArrays(mode:Int){
        useProgram()
        useAttribute(positionHandle,vertexBuffer)
        GLES20.glUniform4fv(colorHandle,colors.size/4,colors,0)
        drawArrays(mode,vertexCoods.size/ COORDS_VERTEX_2D)
        disableVertexAttribArray(positionHandle)
    }




    protected fun useProgram(){
        GLES20.glUseProgram(program)
    }


}