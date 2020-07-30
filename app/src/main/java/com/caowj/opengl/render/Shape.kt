/*
 *
 * Shape.java
 *
 * Created on 2016/9/30
 */
package com.caowj.opengl.render

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.View

/**
 * Description:自定义的形状渲染器
 */
abstract class Shape(protected var mView: View?) : GLSurfaceView.Renderer {
    fun loadShader(type: Int, shaderCode: String?): Int {
        //根据type创建顶点着色器或者片元着色器
        val shader = GLES20.glCreateShader(type)
        //将资源加入到着色器中，并编译
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

}