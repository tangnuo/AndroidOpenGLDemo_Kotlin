/*
 *
 * SGLRender.java
 *
 * Created on 2016/10/15
 */
package com.caowj.opengl.image

import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.view.View
import com.caowj.opengl.image.filter.AFilter
import com.caowj.opengl.image.filter.ColorFilter
import com.caowj.opengl.image.filter.ContrastColorFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Description:
 */
class SGLRender(mView: View) : GLSurfaceView.Renderer {
    private var mFilter: AFilter
    private var bitmap: Bitmap? = null
    private var width = 0
    private var height = 0
    private var refreshFlag = false
    private var config: EGLConfig? = null

    fun setImageBuffer(buffer: IntArray?, width: Int, height: Int) {
        bitmap = Bitmap.createBitmap(buffer!!, width, height, Bitmap.Config.RGB_565)
        mFilter.setBitmap(bitmap)
    }

    fun refresh() {
        refreshFlag = true
    }

    var filter: AFilter
        get() = mFilter
        set(filter) {
            refreshFlag = true
            mFilter = filter
            if (bitmap != null) {
                mFilter.setBitmap(bitmap)
            }
        }

    fun setImage(bitmap: Bitmap?) {
        this.bitmap = bitmap
        mFilter.setBitmap(bitmap)
    }

    override fun onSurfaceCreated(
        gl: GL10,
        config: EGLConfig
    ) {
        this.config = config
        mFilter.onSurfaceCreated(gl, config)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        this.width = width
        this.height = height
        mFilter.onSurfaceChanged(gl, width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        if (refreshFlag && width != 0 && height != 0) {
            mFilter.onSurfaceCreated(gl, config!!)
            mFilter.onSurfaceChanged(gl, width, height)
            refreshFlag = false
        }
        mFilter.onDrawFrame(gl)
    }

    init {
        mFilter = ContrastColorFilter(
            mView.context,
            ColorFilter.Filter.NONE
        )
    }
}