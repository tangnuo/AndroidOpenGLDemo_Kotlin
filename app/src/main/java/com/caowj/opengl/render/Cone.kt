/*
 *
 * Cone.java
 *
 * Created on 2016/10/14
 */
package com.caowj.opengl.render

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import android.view.View
import com.caowj.opengl.utils.ShaderUtils.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Description:圆锥
 */
class Cone(mView: View?) : Shape(mView) {
    private var mProgram = 0
    private val oval: Oval
    private val vertexBuffer: FloatBuffer
    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)
    private val n = 360 //切割份数
    private val height = 2.0f //圆锥高度
    private val radius = 1.0f //圆锥底面半径
    private val colors = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
    private val vSize: Int
    override fun onSurfaceCreated(
        gl: GL10,
        config: EGLConfig
    ) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        mProgram =
            createProgram(mView!!.resources, "vshader/Cone.sh", "fshader/Cone.sh")
        oval.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        //计算宽高比
        val ratio = width.toFloat() / height
        //设置透视投影
        Matrix.frustumM(mProjectMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 20f)
        //设置相机位置
        Matrix.setLookAtM(
            mViewMatrix,
            0,
            1.0f,
            -10.0f,
            -4.0f,
            0f,
            0f,
            0f,
            0f,
            1.0f,
            0.0f
        )
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10) {
        GLES20.glUseProgram(mProgram)
        Log.e("caowj", "mProgram:$mProgram")
        val mMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix")
        GLES20.glUniformMatrix4fv(mMatrix, 1, false, mMVPMatrix, 0)
        val mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        Log.e("caowj", "Get Position:$mPositionHandle")
        GLES20.glEnableVertexAttribArray(mPositionHandle)
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        //        int mColorHandle=GLES20.glGetUniformLocation(mProgram,"vColor");
//        GLES20.glEnableVertexAttribArray(mColorHandle);
//        GLES20.glUniform4fv(mColorHandle,1,colors,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vSize)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
        oval.setMatrix(mMVPMatrix)
        oval.onDrawFrame(gl)
    }

    init {
        oval = Oval(mView)
        val pos = ArrayList<Float>()
        pos.add(0.0f)
        pos.add(0.0f)
        pos.add(height)
        val angDegSpan = 360f / n
        run {
            var i = 0f
            while (i < 360 + angDegSpan) {
                pos.add((radius * Math.sin(i * Math.PI / 180f)).toFloat())
                pos.add((radius * Math.cos(i * Math.PI / 180f)).toFloat())
                pos.add(0.0f)
                i += angDegSpan
            }
        }
        val d = FloatArray(pos.size)
        for (i in d.indices) {
            d[i] = pos[i]
        }
        vSize = d.size / 3
        val buffer = ByteBuffer.allocateDirect(d.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(d)
        vertexBuffer.position(0)
    }
}