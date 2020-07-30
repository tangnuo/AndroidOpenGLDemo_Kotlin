/*
 *
 * CameraFilter.java
 *
 * Created on 2016/11/19
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.filter

import android.content.res.Resources
import android.opengl.GLES11Ext
import android.opengl.GLES20
import java.util.*

/**
 * Description:
 */
open class OesFilter(mRes: Resources) :
    AFilter(mRes) {
    private var mHCoordMatrix = 0
    private var mCoordMatrix: FloatArray =
        Arrays.copyOf(AFilter.Companion.OM, 16)

    override fun onCreate() {
        createProgramByAssetsFile("shader/oes_base_vertex.sh", "shader/oes_base_fragment.sh")
        mHCoordMatrix = GLES20.glGetUniformLocation(mProgram, "vCoordMatrix")
    }

    fun setCoordMatrix(matrix: FloatArray) {
        mCoordMatrix = matrix
    }

    override fun onSetExpandData() {
        super.onSetExpandData()
        GLES20.glUniformMatrix4fv(mHCoordMatrix, 1, false, mCoordMatrix, 0)
    }

    override fun onBindTexture() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureType)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
        GLES20.glUniform1i(mHTexture, textureType)
    }

    override fun onSizeChanged(width: Int, height: Int) {}
}