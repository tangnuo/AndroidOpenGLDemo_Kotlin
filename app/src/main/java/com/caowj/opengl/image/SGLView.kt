/*
 *
 * SGLView.java
 *
 * Created on 2016/10/15
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.image

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.caowj.opengl.image.filter.AFilter
import java.io.IOException

/**
 * Description:
 */
class SGLView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    var render: SGLRender? = null
        private set

    private fun init() {
        setEGLContextClientVersion(2)
        render = SGLRender(this)
        setRenderer(render)
        renderMode = RENDERMODE_WHEN_DIRTY
        try {
            render!!.setImage(
                BitmapFactory.decodeStream(
                    resources.assets.open("texture/fengj.png")
                )
            )
            requestRender()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setFilter(filter: AFilter) {
        render?.filter = filter
    }

    init {
        init()
    }
}