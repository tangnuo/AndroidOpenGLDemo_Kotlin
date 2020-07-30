/*
 *
 * FGLRender.java
 *
 * Created on 2016/9/29
 */
package com.caowj.opengl.render;

import android.opengl.GLES20;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Description:自定义形状渲染器
 */
public class FGLRender extends Shape {

    private Shape shape;
    private Class<? extends Shape> clazz = Cube.class;

    public FGLRender(View mView) {
        super(mView);
    }

    public void setShape(Class<? extends Shape> shape) {
        this.clazz = shape;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        Log.e("caowj", "onSurfaceCreated");
        try {
            Constructor constructor = clazz.getDeclaredConstructor(View.class);
            constructor.setAccessible(true);
            shape = (Shape) constructor.newInstance(mView);
        } catch (Exception e) {
            e.printStackTrace();
            shape = new Cube(mView);
        }
        shape.onSurfaceCreated(gl, config);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e("caowj", "onSurfaceChanged");
        GLES20.glViewport(0, 0, width, height);

        shape.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e("caowj", "onDrawFrame");
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        shape.onDrawFrame(gl);
    }

}
