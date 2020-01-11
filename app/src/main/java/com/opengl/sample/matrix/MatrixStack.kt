package com.opengl.sample.matrix

import android.opengl.Matrix
import java.util.*

/**
 * 作者 ：wangJiang
 * 时间 ：2020/1/8
 * 描述 ：
 */
class MatrixStack {

    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val stack = Stack<FloatArray>()


    init {
        setIdentity(modelMatrix)
    }

    fun frustumM(aspectRatio: Float, near: Float, far: Float) {
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, near, far)
    }

    fun orthoM(aspectRatio: Float) {
        Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, 0f, 10f)
    }


    fun setLookAtM(
        eyeX: Float,
        eyeY: Float,
        eyeZ: Float,
        centerX: Float = 0f,
        centerY: Float = 0f,
        centerZ: Float = 0f,
        upX: Float = 0f,
        upY: Float = 1f,
        upZ: Float = 0f
    ) {
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
    }

    /**
     * 设置单元矩阵
     */
    fun setIdentity(matrix: FloatArray, offset: Int = 0) {
        Matrix.setIdentityM(matrix, offset)
    }




    fun save(){
        val temp = FloatArray(16)
        modelMatrix.forEachIndexed { index, value ->
            temp[index] = value
        }
        stack.push(temp)
    }

    fun restore(){
        if (stack.isNotEmpty()) {
            val temp = stack.pop()
            temp.forEachIndexed { index, value ->
                modelMatrix[index] = value
            }
        }
    }

    fun rotateM(rotate: Float, x: Float = 0f, y: Float = 0f, z: Float = 0f,offset: Int = 0) {
        Matrix.rotateM(modelMatrix, offset, rotate, x, y, z)
    }

    fun translateM(tx: Float = 0f, ty: Float = 0f, tz: Float = 0f) {
        Matrix.translateM(modelMatrix,0,tx,ty,tz)
    }


    fun scaleM(scaleX: Float = 1f, scaleY: Float = 1f, scaleZ: Float = 1.0f) {
        Matrix.scaleM(modelMatrix,0,scaleX,scaleY,scaleZ)
    }

    fun multiplyMM(result:FloatArray,lhs:FloatArray,rhs:FloatArray){
        Matrix.multiplyMM(result, 0, lhs, 0, rhs, 0)
    }

    fun setResultM(){
        multiplyMM(mvpMatrix, viewMatrix, modelMatrix)
        multiplyMM(mvpMatrix, projectionMatrix, mvpMatrix)
    }

    fun getResult():FloatArray{
        setResultM()
        return mvpMatrix
    }

    fun getModelMatrix():FloatArray{
        return modelMatrix
    }

}