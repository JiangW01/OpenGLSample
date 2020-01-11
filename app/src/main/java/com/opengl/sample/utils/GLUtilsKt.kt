package com.opengl.sample.utils

import android.content.Context
import android.content.res.Resources
import android.opengl.GLES20
import android.util.Log
import java.io.ByteArrayOutputStream
import android.opengl.GLES20.GL_LINK_STATUS
import java.nio.*


//每个顶点的坐标数（x,y,z）
const val COORDS_VERTEX_2D = 2
//每个顶点的坐标数（x,y,z）
const val COORDS_VERTEX_3D = 3
const val BYTES_PRE_FLOAT = 4

/**
 * 从Assets加载着色器
 * @param ctx 上下文
 * @param fileName 文件名
 * @param type  顶点着色 {@link GLES20.GL_VERTEX_SHADER}
 *              片元着色 {@link GLES20.GL_FRAGMENT_SHADER}
 * @return 作色器
 */
inline fun loadShaderAssets(ctx: Context, fileName: String, type: Int): Int {
    try {
        val inputStream = ctx.assets.open(fileName)
        var ch = inputStream.read()
        val baos = ByteArrayOutputStream()
        while (ch != -1) {
            baos.write(ch)
            ch = inputStream.read()
        }
        val buff = baos.toByteArray()
        baos.close()
        inputStream.close()
        var result = String(buff)
        result = result.replace("\\r\\n", "\n")
        return loadShader(type, result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 从Assets加载着色器
 * @param ctx 上下文
 * @param fileName 文件名
 * @return 作色器
 */
inline fun loadVertexShaderAssets(ctx: Context, fileName: String): Int {
    return loadShaderAssets(ctx, fileName, GLES20.GL_VERTEX_SHADER)
}

/**
 * 从Assets加载着色器
 * @param ctx 上下文
 * @param fileName 文件名
 * @return 作色器
 */
inline fun loadFragShaderAssets(ctx: Context, fileName: String): Int {
    return loadShaderAssets(ctx, fileName, GLES20.GL_FRAGMENT_SHADER)
}


/**
 * 加载顶点着色器
 * @param shaderCode 着色代码
 * @return 作色器
 */
inline fun loadVertexShader(shaderCode: String): Int {
    return loadShader(GLES20.GL_VERTEX_SHADER, shaderCode)
}

/**
 * 从Assets加载着色器
 * @param resources 资源
 * @param fileName 文件名
 * @param type  顶点着色 {@link GLES20.GL_VERTEX_SHADER}
 *              片元着色 {@link GLES20.GL_FRAGMENT_SHADER}
 * @return 作色器
 */
inline fun loadShaderAssets(resources: Resources, fileName: String, type: Int): Int {
    try {
        val inputStream = resources.assets.open(fileName)
        var ch = inputStream.read()
        val baos = ByteArrayOutputStream()
        while (ch != -1) {
            baos.write(ch)
            ch = inputStream.read()
        }
        val buff = baos.toByteArray()
        baos.close()
        inputStream.close()
        var result = String(buff)
        result = result.replace("\\r\\n", "\n")
        return loadShader(type, result)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

/**
 * 从Assets加载着色器
 * @param resources 资源
 * @param fileName 文件名
 * @return 作色器
 */
inline fun loadVertexShaderAssets(resources: Resources, fileName: String): Int {
    return loadShaderAssets(resources, fileName, GLES20.GL_VERTEX_SHADER)
}

/**
 * 从Assets加载着色器
 * @param resources 资源
 * @param fileName 文件名
 * @return 作色器
 */
inline fun loadFragShaderAssets(resources: Resources, fileName: String): Int {
    return loadShaderAssets(resources, fileName, GLES20.GL_FRAGMENT_SHADER)
}

/**
 * 加载片元着色器
 * @param shaderCode 着色代码
 * @return 作色器
 */
inline fun loadFragShader(shaderCode: String): Int {
    return loadShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
}

/**
 * 加载着色器
 * @param type  顶点着色 {@link GLES20.GL_VERTEX_SHADER}
 *              片元着色 {@link GLES20.GL_FRAGMENT_SHADER}
 * @param shaderCode 着色代码
 * @return 作色器
 */
inline fun loadShader(type: Int, shaderCode: String): Int {
    val shader = GLES20.glCreateShader(type)//创建着色器
    if (shader == 0) {
        return 0 //加载失败直接返回
    }
    GLES20.glShaderSource(shader, shaderCode)//添加着色器源代码
    GLES20.glCompileShader(shader)//编译
    return checkCompile(type, shader)
}


inline fun asFloatBuffer(datas: FloatArray): FloatBuffer {
    val buffer = ByteBuffer.allocateDirect(datas.size * BYTES_PRE_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer()
        .put(datas)
    buffer.position(0)
    return buffer
}
inline fun asShortBuffer(datas: ShortArray): ShortBuffer {
    val buffer = ByteBuffer.allocateDirect(datas.size * BYTES_PRE_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asShortBuffer()
        .put(datas)
    buffer.position(0)
    return buffer
}
inline fun asIntBuffer(datas: IntArray): IntBuffer {
    val buffer = ByteBuffer.allocateDirect(datas.size * BYTES_PRE_FLOAT)
        .order(ByteOrder.nativeOrder())
        .asIntBuffer()
        .put(datas)
    buffer.position(0)
    return buffer
}

inline fun glCreateProgram(vertexShader: Int, fragmentShader: Int): Int {
    val program = GLES20.glCreateProgram() //创建空的OpenGL ES 程序
    if (program == 0) {
        Log.e("ES20_ERROR", "program  error $program")
        return 0
    }
    GLES20.glAttachShader(program, vertexShader)//加入顶点着色器
    GLES20.glAttachShader(program, fragmentShader)//加入片元着色器
    GLES20.glLinkProgram(program)//创建可执行的OpenGL ES项目
    return checkLink(program)
}

/**
 * 检查shader代码是否编译成功
 * @param program
 * @return program
 */
inline fun checkLink(program: Int): Int {
    val linkStatus = IntArray(1)
    // 验证链接结果是否失败
    GLES20.glGetProgramiv(program, GL_LINK_STATUS, linkStatus, 0)
    if (linkStatus[0] == 0) {
        // 失败则删除 OpenGL 程序
        Log.e("ES20_ERROR", "Could not Link program $program")
        GLES20.glDeleteProgram(program)
        return 0
    }
    return program
}

/**
 * 检查shader代码是否编译成功
 * @param type   着色器类型
 * @param shader 着色器
 * @return 着色器
 */
inline fun checkCompile(type: Int, shader: Int): Int {
    val compiled = IntArray(2) //存放编译成功shader数量的数组
    //获取Shader的编译情况
    GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
    if (compiled[0] == 0) {//若编译失败则显示错误日志并
        Log.e("ES20_ERROR", "Could not compile shader $type : ${GLES20.glGetShaderInfoLog(shader)}")
        GLES20.glDeleteShader(shader)//删除此shader
        return 0
    }
    return shader
}

/**
 * 使能顶点attrib属性并赋值
 * @param program GLES程序
 * @param attributeName 变量名
 * @param dataBuffer 数据
 * @param size 数组中每个顶点的坐标数
 * @param type 类型 默认值：GLES20.GL_FLOAT
 * @param normalized  默认值：false 是否标准化
 * @param stride  默认值：size*4 跨度
 * @return 属性句柄
 */
inline fun glUseAttribute(
    program: Int, attributeName: String, dataBuffer: Buffer, size: Int = COORDS_VERTEX_3D,
    type: Int = GLES20.GL_FLOAT, normalized: Boolean = false, stride: Int = size * 4
): Int {
    val handle = GLES20.glGetAttribLocation(program, attributeName)
    GLES20.glVertexAttribPointer(handle, size, type, normalized, stride, dataBuffer)
    GLES20.glEnableVertexAttribArray(handle)
    return handle
}

/**
 * 使能Uniform属性并赋值
 * @param program GLES程序
 * @param uniformName 变量名
 * @param datas 数据
 * @param count
 * @param offset
 * @return 属性句柄
 */
inline fun glUseUniform4v(program: Int, uniformName: String, datas: FloatArray, count: Int = 1, offset: Int = 0): Int {
    val handle = GLES20.glGetUniformLocation(program, uniformName)
    GLES20.glUniform4fv(handle, count, datas, offset)
    return handle
}


/**
 * 使能Uniform属性并赋值
 * @param program GLES程序
 * @param uniformName 变量名
 * @param datas 数据
 * @param count
 * @param offset
 * @return 属性句柄
 */
inline fun glUniform4f(program: Int, uniformName: String, x: Float, y: Float, z: Float,w: Float): Int {
    val handle = GLES20.glGetUniformLocation(program, uniformName)
    GLES20.glUniform4f(handle, x,y,z,w)
    return handle
}


