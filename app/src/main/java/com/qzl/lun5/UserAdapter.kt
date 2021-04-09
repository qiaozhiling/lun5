package com.qzl.lun5

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.qzl.lun5.qzlviewpager.QzlViewPagerAdapter
import com.qzl.lun5.qzlviewpager.QzlViewPagerBaseData

class UserAdapter(dataList: List<UserData>, private val context: Context) :
    QzlViewPagerAdapter(dataList) {
    override fun setIV(iv: ImageView, position: Int, dataList: List<QzlViewPagerBaseData>) {

        iv.setOnClickListener {
            val intent = Intent(context, MainActivity2::class.java)
            intent.putExtra("url", (dataList[position] as UserData).url)
            context.startActivity(intent)
        }

        Glide.with(context).load((dataList[position] as UserData).picUrl).into(iv)
    }

}