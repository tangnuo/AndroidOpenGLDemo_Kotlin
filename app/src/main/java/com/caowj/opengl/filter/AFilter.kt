/*
 *
 * AFilter.java
 *
 * Created on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.filter

import android.content.res.Resources
import android.opengl.GLES20
import android.util.Log
import android.util.SparseArray
import com.caowj.opengl.utils.MatrixUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * Description:
 */
abstract class AFilter(protected var mRes: Resources) {
    /**
     * 程序句柄
     */
    protected var mProgram = 0

    /**
     * 顶点坐标句柄
     */
    protected var mHPosition = 0

    /**
     * 纹理坐标句柄
     */
    protected var mHCoord = 0

    /**
     * 总变换矩阵句柄
     */
    protected var mHMatrix = 0

    /**
     * 默认纹理贴图句柄
     */
    protected var mHTexture = 0

    /**
     * 顶点坐标Buffer
     */
    protected var mVerBuffer: FloatBuffer? = null

    /**
     * 纹理坐标Buffer
     */
    protected var mTexBuffer: FloatBuffer? = null

    /**
     * 索引坐标Buffer
     */
    protected var mindexBuffer: ShortBuffer? = null
    open var flag = 0
    private var matrix = OM.copyOf(16)
    var textureType = 0 //默认使用Texture2D0
    var textureId = 0

    //顶点坐标
    private val pos = floatArrayOf(
        -1.0f, 1.0f,
        -1.0f, -1.0f,
        1.0f, 1.0f,
        1.0f, -1.0f
    )

    //纹理坐标
    private val coord = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 0.0f,
        1.0f, 1.0f
    )
    private var mBools: SparseArray<BooleanArray>? = null
    private var mInts: SparseArray<IntArray>? = null
    private var mFloats: SparseArray<FloatArray>? = null
    fun create() {
        onCreate()
    }

    fun setSize(width: Int, height: Int) {
        onSizeChanged(width, height)
    }

    open fun draw() {
        onClear()
        onUseProgram()
        onSetExpandData()
        onBindTexture()
        onDraw()
    }

    open fun getMatrix(): FloatArray? {
        return matrix
    }

    fun setMatrix(matrix: FloatArray) {
        this.matrix = matrix
    }

    fun setFloat(type: Int, vararg params: Float) {
        if (mFloats == null) {
            mFloats = SparseArray()
        }
        mFloats!!.put(type, params)
    }

    open fun setInt(type: Int, vararg params: Int) {
        if (mInts == null) {
            mInts = SparseArray()
        }
        mInts!!.put(type, params)
    }

    fun setBool(type: Int, vararg params: Boolean) {
        if (mBools == null) {
            mBools = SparseArray()
        }
        mBools!!.put(type, params)
    }

    fun getBool(type: Int, index: Int): Boolean {
        if (mBools == null) return false
        val b = mBools!![type]
        return !(b == null || b.size <= index) && b[index]
    }

    fun getInt(type: Int, index: Int): Int {
        if (mInts == null) return 0
        val b = mInts!![type]
        return if (b == null || b.size <= index) {
            0
        } else b[index]
    }

    fun getFloat(type: Int, index: Int): Float {
        if (mFloats == null) return 0f
        val b = mFloats!![type]
        return if (b == null || b.size <= index) {
            0f
        } else b[index]
    }

    open val outputTexture: Int
        get() = -1

    /**
     * 实现此方法，完成程序的创建，可直接调用createProgram来实现
     */
    protected abstract fun onCreate()
    protected abstract fun onSizeChanged(width: Int, height: Int)
    protected fun createProgram(vertex: String?, fragment: String?) {
        mProgram = uCreateGlProgram(vertex, fragment)
        mHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition")
        mHCoord = GLES20.glGetAttribLocation(mProgram, "vCoord")
        mHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        mHTexture = GLES20.glGetUniformLocation(mProgram, "vTexture")
    }

    protected fun createProgramByAssetsFile(
        vertex: String?,
        fragment: String?
    ) {
        createProgram(
            uRes(mRes, vertex),
            uRes(mRes, fragment)
        )
    }

    /**
     * Buffer初始化
     */
    protected open fun initBuffer() {
        val a = ByteBuffer.allocateDirect(32)
        a.order(ByteOrder.nativeOrder())
        mVerBuffer = a.asFloatBuffer()
        mVerBuffer!!.put(pos)
        mVerBuffer!!.position(0)
        val b = ByteBuffer.allocateDirect(32)
        b.order(ByteOrder.nativeOrder())
        mTexBuffer = b.asFloatBuffer()
        mTexBuffer!!.put(coord)
        mTexBuffer!!.position(0)
    }

    protected fun onUseProgram() {
        GLES20.glUseProgram(mProgram)
    }

    /**
     * 启用顶点坐标和纹理坐标进行绘制
     */
    protected fun onDraw() {
        GLES20.glEnableVertexAttribArray(mHPosition)
        GLES20.glVertexAttribPointer(mHPosition, 2, GLES20.GL_FLOAT, false, 0, mVerBuffer)
        GLES20.glEnableVertexAttribArray(mHCoord)
        GLES20.glVertexAttribPointer(mHCoord, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(mHPosition)
        GLES20.glDisableVertexAttribArray(mHCoord)
    }

    /**
     * 清除画布
     */
    protected open fun onClear() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
    }

    /**
     * 设置其他扩展数据
     */
    protected open fun onSetExpandData() {
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0)
    }

    /**
     * 绑定默认纹理
     */
    protected open fun onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(mHTexture, textureType)
    }

    companion object {
        const val KEY_OUT = 0x101
        const val KEY_IN = 0x102
        const val KEY_INDEX = 0x201

        /**
         * 单位矩阵
         */
        val OM: FloatArray = MatrixUtils.Companion.originalMatrix
        private const val TAG = "Filter"
        var DEBUG = true
        fun glError(code: Int, index: Any) {
            if (DEBUG && code != 0) {
                Log.e(
                    TAG,
                    "glError:$code---$index"
                )
            }
        }

        //通过路径加载Assets中的文本内容
        fun uRes(mRes: Resources, path: String?): String? {
            val result = StringBuilder()
            try {
                val `is` = mRes.assets.open(path!!)
                var ch: Int
                val buffer = ByteArray(1024)
                while (-1 != `is`.read(buffer).also { ch = it }) {
                    result.append(String(buffer, 0, ch))
                }
            } catch (e: Exception) {
                return null
            }
            return result.toString().replace("\\r\\n".toRegex(), "\n")
        }

        //创建GL程序
        fun uCreateGlProgram(vertexSource: String?, fragmentSource: String?): Int {
            val vertex = uLoadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexSource
            )
            if (vertex == 0) return 0
            val fragment = uLoadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentSource
            )
            if (fragment == 0) return 0
            var program = GLES20.glCreateProgram()
            if (program != 0) {
                GLES20.glAttachShader(program, vertex)
                GLES20.glAttachShader(program, fragment)
                GLES20.glLinkProgram(program)
                val linkStatus = IntArray(1)
                GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
                if (linkStatus[0] != GLES20.GL_TRUE) {
                    glError(
                        1,
                        "Could not link program:" + GLES20.glGetProgramInfoLog(program)
                    )
                    GLES20.glDeleteProgram(program)
                    program = 0
                }
            }
            return program
        }

        //加载shader
        fun uLoadShader(shaderType: Int, source: String?): Int {
            var shader = GLES20.glCreateShader(shaderType)
            if (0 != shader) {
                GLES20.glShaderSource(shader, source)
                GLES20.glCompileShader(shader)
                val compiled = IntArray(1)
                GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
                if (compiled[0] == 0) {
                    glError(
                        1,
                        "Could not compile shader:$shaderType"
                    )
                    glError(
                        1,
                        "GLES20 Error:" + GLES20.glGetShaderInfoLog(shader)
                    )
                    GLES20.glDeleteShader(shader)
                    shader = 0
                }
            }
            return shader
        }
    }

    init {
        initBuffer()
    }
}