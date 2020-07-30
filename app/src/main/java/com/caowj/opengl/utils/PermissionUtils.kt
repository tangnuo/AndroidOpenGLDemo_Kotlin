/*
 *
 * PermissionUtils.java
 *
 * Created on 2016/11/14
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

/**
 * Description:
 */
object PermissionUtils {
    fun askPermission(
        context: Activity?,
        permissions: Array<String?>,
        req: Int,
        runnable: Runnable
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result = ActivityCompat.checkSelfPermission(context!!, permissions[0]!!)
            if (result == PackageManager.PERMISSION_GRANTED) {
                runnable.run()
            } else {
                ActivityCompat.requestPermissions(
                    context, arrayOf(
                        Manifest.permission.CAMERA
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), req
                )
            }
        } else {
            runnable.run()
        }
    }

    fun onRequestPermissionsResult(
        isReq: Boolean,
        grantResults: IntArray,
        okRun: Runnable,
        deniRun: Runnable
    ) {
        if (isReq) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                okRun.run()
            } else {
                deniRun.run()
            }
        }
    }
}