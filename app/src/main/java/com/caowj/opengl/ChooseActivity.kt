package com.caowj.opengl

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.caowj.opengl.render.*
import kotlinx.android.synthetic.main.activity_list.*
import java.util.*

class ChooseActivity : AppCompatActivity() {

    private lateinit var mData: ArrayList<Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        init()
    }

    private fun init() {
        initData()
        mList.adapter = Adapter()
        mList.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val intent = Intent()
            intent.putExtra("name", mData[position].clazz)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun initData() {
        mData = ArrayList()
        add("三角形", Triangle::class.java)
        add("正三角形", TriangleWithCamera::class.java)
        add("彩色三角形", TriangleColorFull::class.java)
        add("正方形", Square::class.java)
        add("圆形", Oval::class.java)
        add("正方体", Cube::class.java)
        add("圆锥", Cone::class.java)
        add("圆柱", Cylinder::class.java)
        add("球体", Ball::class.java)
        add("带光源的球体", BallWithLight::class.java)
    }

    private fun add(showName: String, clazz: Class<*>) {
        val data = Data()
        data.clazz = clazz
        data.showName = showName
        mData.add(data)
    }

    private inner class Adapter : BaseAdapter() {
        override fun getCount(): Int {
            return mData.size
        }

        override fun getItem(position: Int): Any {
            return mData[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            val view: View
            if (convertView == null) {
                view = LayoutInflater.from(this@ChooseActivity)
                    .inflate(R.layout.item_choose, parent, false)
                view.tag = ViewHolder(view)
            } else {
                view = convertView
            }
            val holder = view.tag as ViewHolder
            holder.setData(mData[position])
            return view
        }

        private inner class ViewHolder(parent: View) {
            private val mName: TextView = parent.findViewById<View>(R.id.mName) as TextView
            fun setData(data: Data) {
                mName.text = data.showName
            }
        }
    }

    private class Data {
        var showName: String? = null
        var clazz: Class<*>? = null
    }
}