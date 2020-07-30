package com.caowj.opengl.render

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/**
 * Description:
 */
class FGLView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    private var renderer: FGLRender? = null

    init {
        initData()
    }

    private fun initData() {
        setEGLContextClientVersion(2)//设置OpenGL版本
        setRenderer(FGLRender(this).also { renderer = it })//设置渲染器
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    fun setShape(clazz: Class<out Shape>) {
        try {
            renderer!!.setShape(clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}