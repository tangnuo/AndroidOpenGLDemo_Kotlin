/*
 *
 * StateCallback.java
 *
 * Created on 2016/11/30
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.etc

/**
 * Description:
 */
interface StateChangeListener {
    fun onStateChanged(lastState: Int, nowState: Int)

    companion object {
        const val START = 1
        const val STOP = 2
        const val PLAYING = 3
        const val INIT = 4
        const val PAUSE = 5
        const val RESUME = 6
    }
}