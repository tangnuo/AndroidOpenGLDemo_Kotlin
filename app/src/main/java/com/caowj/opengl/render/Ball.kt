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
 * 球体
 */
class Ball(mView: View?) : Shape(mView) {
    private val step = 5f
    private val vertexBuffer: FloatBuffer
    private val vSize: Int
    private var mProgram = 0
    private val mViewMatrix = FloatArray(16)
    private val mProjectMatrix = FloatArray(16)
    private val mMVPMatrix = FloatArray(16)
    private fun createBallPos(): FloatArray {
        //球以(0,0,0)为中心，以R为半径，则球上任意一点的坐标为
        // ( R * cos(a) * sin(b),y0 = R * sin(a),R * cos(a) * cos(b))
        // 其中，a为圆心到点的线段与xz平面的夹角，b为圆心到点的线段在xz平面的投影与z轴的夹角
        val data = ArrayList<Float>()
        var r1: Float
        var r2: Float
        var h1: Float
        var h2: Float
        var sin: Float
        var cos: Float
        run {
            var i = -90f
            while (i < 90 + step) {
                r1 = Math.cos(i * Math.PI / 180.0).toFloat()
                r2 = Math.cos((i + step) * Math.PI / 180.0).toFloat()
                h1 = Math.sin(i * Math.PI / 180.0).toFloat()
                h2 = Math.sin((i + step) * Math.PI / 180.0).toFloat()
                // 固定纬度, 360 度旋转遍历一条纬线
                val step2 = step * 2
                var j = 0.0f
                while (j < 360.0f + step) {
                    cos = Math.cos(j * Math.PI / 180.0).toFloat()
                    sin = (-Math.sin(j * Math.PI / 180.0)).toFloat()
                    data.add(r2 * cos)
                    data.add(h2)
                    data.add(r2 * sin)
                    data.add(r1 * cos)
                    data.add(h1)
                    data.add(r1 * sin)
                    j += step2
                }
                i += step
            }
        }
        val f = FloatArray(data.size)
        for (i in f.indices) {
            f[i] = data[i]
        }
        return f
    }

    override fun onSurfaceCreated(
        gl: GL10,
        config: EGLConfig
    ) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        mProgram =
            createProgram(mView!!.resources, "vshader/Ball.sh", "fshader/Cone.sh")
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, vSize)
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    init {
        val dataPos = createBallPos()
        val buffer = ByteBuffer.allocateDirect(dataPos.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(dataPos)
        vertexBuffer.position(0)
        vSize = dataPos.size / 3
    }
}