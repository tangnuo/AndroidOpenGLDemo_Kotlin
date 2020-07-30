/*
 *
 * Beauty.java
 *
 * Created on 2016/11/18
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.filter

import android.content.res.Resources
import android.opengl.GLES20

/**
 * Description:
 */
class Beauty(res: Resources) :
    AFilter(res) {
    private var gHaaCoef = 0
    private var gHmixCoef = 0
    private var gHiternum = 0
    private var gHWidth = 0
    private var gHHeight = 0
    private var aaCoef = 0f
    private var mixCoef = 0f
    private var iternum = 0
    private var mWidth = 720
    private var mHeight = 1280
    override fun onCreate() {
        createProgramByAssetsFile("shader/beauty/beauty.vert", "shader/beauty/beauty.frag")
        gHaaCoef = GLES20.glGetUniformLocation(mProgram, "aaCoef")
        gHmixCoef = GLES20.glGetUniformLocation(mProgram, "mixCoef")
        gHiternum = GLES20.glGetUniformLocation(mProgram, "iternum")
        gHWidth = GLES20.glGetUniformLocation(mProgram, "mWidth")
        gHHeight = GLES20.glGetUniformLocation(mProgram, "mHeight")
    }

    override var flag
        get() = super.flag
        set(flag) {
            super.flag = flag
            when (flag) {
                1 -> a(1, 0.19f, 0.54f)
                2 -> a(2, 0.29f, 0.54f)
                3 -> a(3, 0.17f, 0.39f)
                4 -> a(3, 0.25f, 0.54f)
                5 -> a(4, 0.13f, 0.54f)
                6 -> a(4, 0.19f, 0.69f)
                else -> a(0, 0f, 0f)
            }
        }

    private fun a(a: Int, b: Float, c: Float) {
        iternum = a
        aaCoef = b
        mixCoef = c
    }

    override fun onSizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    override fun onSetExpandData() {
        super.onSetExpandData()
        GLES20.glUniform1i(gHWidth, mWidth)
        GLES20.glUniform1i(gHHeight, mHeight)
        GLES20.glUniform1f(gHaaCoef, aaCoef)
        GLES20.glUniform1f(gHmixCoef, mixCoef)
        GLES20.glUniform1i(gHiternum, iternum)
    }

}