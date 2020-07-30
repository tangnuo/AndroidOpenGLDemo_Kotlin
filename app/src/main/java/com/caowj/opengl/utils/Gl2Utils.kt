/*
 *
 * FastDrawerHelper.java
 *
 * Created on 2016/11/17
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.utils

import android.content.res.Resources
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log

/**
 * Description:
 */
object Gl2Utils {
    const val TAG = "GLUtils"
    const val TYPE_FITXY = 0
    const val TYPE_CENTERCROP = 1
    const val TYPE_CENTERINSIDE = 2
    const val TYPE_FITSTART = 3
    const val TYPE_FITEND = 4
    var DEBUG = true
    fun getShowMatrix(
        matrix: FloatArray?,
        imgWidth: Int,
        imgHeight: Int,
        viewWidth: Int,
        viewHeight: Int
    ) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (sWhImg > sWhView) {
                Matrix.orthoM(
                    projection,
                    0,
                    -sWhView / sWhImg,
                    sWhView / sWhImg,
                    -1f,
                    1f,
                    1f,
                    3f
                )
            } else {
                Matrix.orthoM(
                    projection,
                    0,
                    -1f,
                    1f,
                    -sWhImg / sWhView,
                    sWhImg / sWhView,
                    1f,
                    3f
                )
            }
            Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun getMatrix(
        matrix: FloatArray?, type: Int, imgWidth: Int, imgHeight: Int, viewWidth: Int,
        viewHeight: Int
    ) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (type == TYPE_FITXY) {
                Matrix.orthoM(projection, 0, -1f, 1f, -1f, 1f, 1f, 3f)
                Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
                Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
            }
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            if (sWhImg > sWhView) {
                when (type) {
                    TYPE_CENTERCROP -> Matrix.orthoM(
                        projection,
                        0,
                        -sWhView / sWhImg,
                        sWhView / sWhImg,
                        -1f,
                        1f,
                        1f,
                        3f
                    )
                    TYPE_CENTERINSIDE -> Matrix.orthoM(
                        projection,
                        0,
                        -1f,
                        1f,
                        -sWhImg / sWhView,
                        sWhImg / sWhView,
                        1f,
                        3f
                    )
                    TYPE_FITSTART -> Matrix.orthoM(
                        projection,
                        0,
                        -1f,
                        1f,
                        1 - 2 * sWhImg / sWhView,
                        1f,
                        1f,
                        3f
                    )
                    TYPE_FITEND -> Matrix.orthoM(
                        projection,
                        0,
                        -1f,
                        1f,
                        -1f,
                        2 * sWhImg / sWhView - 1,
                        1f,
                        3f
                    )
                }
            } else {
                when (type) {
                    TYPE_CENTERCROP -> Matrix.orthoM(
                        projection,
                        0,
                        -1f,
                        1f,
                        -sWhImg / sWhView,
                        sWhImg / sWhView,
                        1f,
                        3f
                    )
                    TYPE_CENTERINSIDE -> Matrix.orthoM(
                        projection,
                        0,
                        -sWhView / sWhImg,
                        sWhView / sWhImg,
                        -1f,
                        1f,
                        1f,
                        3f
                    )
                    TYPE_FITSTART -> Matrix.orthoM(
                        projection,
                        0,
                        -1f,
                        2 * sWhView / sWhImg - 1,
                        -1f,
                        1f,
                        1f,
                        3f
                    )
                    TYPE_FITEND -> Matrix.orthoM(
                        projection,
                        0,
                        1 - 2 * sWhView / sWhImg,
                        1f,
                        -1f,
                        1f,
                        1f,
                        3f
                    )
                }
            }
            Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun getCenterInsideMatrix(
        matrix: FloatArray?,
        imgWidth: Int,
        imgHeight: Int,
        viewWidth: Int,
        viewHeight: Int
    ) {
        if (imgHeight > 0 && imgWidth > 0 && viewWidth > 0 && viewHeight > 0) {
            val sWhView = viewWidth.toFloat() / viewHeight
            val sWhImg = imgWidth.toFloat() / imgHeight
            val projection = FloatArray(16)
            val camera = FloatArray(16)
            if (sWhImg > sWhView) {
                Matrix.orthoM(
                    projection,
                    0,
                    -1f,
                    1f,
                    -sWhImg / sWhView,
                    sWhImg / sWhView,
                    1f,
                    3f
                )
            } else {
                Matrix.orthoM(
                    projection,
                    0,
                    -sWhView / sWhImg,
                    sWhView / sWhImg,
                    -1f,
                    1f,
                    1f,
                    3f
                )
            }
            Matrix.setLookAtM(camera, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
            Matrix.multiplyMM(matrix, 0, projection, 0, camera, 0)
        }
    }

    fun rotate(m: FloatArray, angle: Float): FloatArray {
        Matrix.rotateM(m, 0, angle, 0f, 0f, 1f)
        return m
    }

    fun flip(m: FloatArray, x: Boolean, y: Boolean): FloatArray {
        if (x || y) {
            Matrix.scaleM(
                m,
                0,
                if (x) (-1).toFloat() else 1.toFloat(),
                if (y) (-1).toFloat() else 1.toFloat(),
                1f
            )
        }
        return m
    }

    val originalMatrix: FloatArray
        get() = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)

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

    fun createGlProgramByRes(
        res: Resources,
        vert: String?,
        frag: String?
    ): Int {
        return createGlProgram(uRes(res, vert), uRes(res, frag))
    }

    //创建GL程序
    fun createGlProgram(vertexSource: String?, fragmentSource: String?): Int {
        val vertex = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertex == 0) return 0
        val fragment = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragment == 0) return 0
        var program = GLES20.glCreateProgram()
        if (program != 0) {
            GLES20.glAttachShader(program, vertex)
            GLES20.glAttachShader(program, fragment)
            GLES20.glLinkProgram(program)
            val linkStatus = IntArray(1)
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] != GLES20.GL_TRUE) {
                glError(1, "Could not link program:" + GLES20.glGetProgramInfoLog(program))
                GLES20.glDeleteProgram(program)
                program = 0
            }
        }
        return program
    }

    //加载shader
    fun loadShader(shaderType: Int, source: String?): Int {
        var shader = GLES20.glCreateShader(shaderType)
        if (0 != shader) {
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            val compiled = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
            if (compiled[0] == 0) {
                glError(1, "Could not compile shader:$shaderType")
                glError(1, "GLES20 Error:" + GLES20.glGetShaderInfoLog(shader))
                GLES20.glDeleteShader(shader)
                shader = 0
            }
        }
        return shader
    }

    fun glError(code: Int, index: Any) {
        if (DEBUG && code != 0) {
            Log.e(TAG, "glError:$code---$index")
        }
    }
}