/*
 *
 * GrayFilter.java
 *
 * Created on 2016/12/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.filter

import android.content.res.Resources

/**
 * Description:
 */
class GrayFilter(mRes: Resources) :
    AFilter(mRes) {
    override fun onCreate() {
        createProgramByAssetsFile(
            "shader/base_vertex.sh",
            "shader/color/gray_fragment.frag"
        )
    }

    override fun onSizeChanged(width: Int, height: Int) {}
}