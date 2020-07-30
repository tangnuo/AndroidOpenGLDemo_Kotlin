/*
 *
 * AiyaFilter.java
 *
 * Created on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.filter

import android.content.res.Resources
import android.hardware.Camera

/**
 * Description:
 */
class CameraFilter(mRes: Resources) : OesFilter(mRes) {
    override fun initBuffer() {
        super.initBuffer()
        movie()
    }//后置摄像头

    //前置摄像头
    override var flag: Int
        get() = super.flag
        set(flag) {
            super.flag = flag
            if (flag == Camera.CameraInfo.CAMERA_FACING_FRONT) {    //前置摄像头
                cameraFront()
            } else if (flag == Camera.CameraInfo.CAMERA_FACING_BACK) {   //后置摄像头
                cameraBack()
            }
        }

    private fun cameraFront() {
        val coord = floatArrayOf(
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
        )
        mTexBuffer!!.clear()
        mTexBuffer!!.put(coord)
        mTexBuffer!!.position(0)
    }

    private fun cameraBack() {
        val coord = floatArrayOf(
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
        )
        mTexBuffer!!.clear()
        mTexBuffer!!.put(coord)
        mTexBuffer!!.position(0)
    }

    private fun movie() {
        val coord = floatArrayOf(
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f
        )
        mTexBuffer!!.clear()
        mTexBuffer!!.put(coord)
        mTexBuffer!!.position(0)
    }
}