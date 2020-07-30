package com.caowj.opengl.render

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.caowj.opengl.ChooseActivity
import com.caowj.opengl.R
import kotlinx.android.synthetic.main.activity_fglview.*

/**
 *  绘制形体
 *  作者：Caowj
 *  邮箱：caoweijian@kedacom.com
 *  日期：2020/7/30 11:27
 */
class FGLViewActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val REQ_CHOOSE = 0x0101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fglview)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.mChange -> {
                val intent = Intent(this, ChooseActivity::class.java)
                startActivityForResult(intent, REQ_CHOOSE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mGLView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGLView.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CHOOSE && resultCode == Activity.RESULT_OK) {
            mGLView.setShape(data?.getSerializableExtra("name") as Class<out Shape>)
        }
    }
}