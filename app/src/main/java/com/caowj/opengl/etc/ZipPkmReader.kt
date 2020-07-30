/*
 *
 * ZipPkmReader.java
 *
 * Created on 2016/12/8
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.caowj.opengl.etc

import android.content.Context
import android.content.res.AssetManager
import android.opengl.ETC1
import android.opengl.ETC1Util.ETC1Texture
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Description:
 */
class ZipPkmReader(private val mManager: AssetManager) {
    private var path: String? = null
    private var mZipStream: ZipInputStream? = null
    private var mZipEntry: ZipEntry? = null
    private var headerBuffer: ByteBuffer? = null

    constructor(context: Context) : this(context.assets) {}

    fun setZipPath(path: String) {
        Log.e("caowj", "$path set")
        this.path = path
    }

    fun open(): Boolean {
        Log.e("caowj", "$path open")
        return if (path == null) false else try {
            mZipStream = if (path!!.startsWith("assets/")) {
                val s = mManager.open(path!!.substring(7))
                ZipInputStream(s)
            } else {
                val f = File(path)
                Log.e("caowj", path + " is File exists->" + f.exists())
                ZipInputStream(FileInputStream(path))
            }
            true
        } catch (e: IOException) {
            Log.e("caowj", "eee-->" + e.message)
            e.printStackTrace()
            false
        }
    }

    fun close() {
        if (mZipStream != null) {
            try {
                mZipStream!!.closeEntry()
                mZipStream!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (headerBuffer != null) {
                headerBuffer!!.clear()
                headerBuffer = null
            }
        }
    }

    private fun hasElements(): Boolean {
        try {
            if (mZipStream != null) {
                mZipEntry = mZipStream!!.nextEntry
                if (mZipEntry != null) {
                    return true
                }
                Log.e("caowj", "mZip entry null")
            }
        } catch (e: IOException) {
            Log.e("caowj", "err  dd->" + e.message)
            e.printStackTrace()
        }
        return false
    }

    val nextStream: InputStream?
        get() = if (hasElements()) {
            mZipStream
        } else null

    val nextTexture: ETC1Texture?
        get() {
            if (hasElements()) {
                try {
                    return createTexture(mZipStream)
                } catch (e1: IOException) {
                    Log.e("caowj", "err->" + e1.message)
                    e1.printStackTrace()
                }
            }
            return null
        }

    @Throws(IOException::class)
    private fun createTexture(input: InputStream?): ETC1Texture {
        var width = 0
        var height = 0
        val ioBuffer = ByteArray(4096)
        run {
            if (input!!.read(ioBuffer, 0, ETC1.ETC_PKM_HEADER_SIZE) != ETC1.ETC_PKM_HEADER_SIZE) {
                throw IOException("Unable to read PKM file header.")
            }
            if (headerBuffer == null) {
                headerBuffer = ByteBuffer.allocateDirect(ETC1.ETC_PKM_HEADER_SIZE)
                    .order(ByteOrder.nativeOrder())
            }
            headerBuffer!!.put(ioBuffer, 0, ETC1.ETC_PKM_HEADER_SIZE).position(0)
            if (!ETC1.isValid(headerBuffer)) {
                throw IOException("Not a PKM file.")
            }
            width = ETC1.getWidth(headerBuffer)
            height = ETC1.getHeight(headerBuffer)
        }
        val encodedSize = ETC1.getEncodedDataSize(width, height)
        val dataBuffer =
            ByteBuffer.allocateDirect(encodedSize).order(ByteOrder.nativeOrder())
        var len: Int
        while (input!!.read(ioBuffer).also { len = it } != -1) {
            dataBuffer.put(ioBuffer, 0, len)
        }
        dataBuffer.position(0)
        return ETC1Texture(width, height, dataBuffer)
    }

}