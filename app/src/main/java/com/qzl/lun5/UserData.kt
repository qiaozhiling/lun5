package com.qzl.lun5

import com.qzl.lun5.qzlviewpager.QzlViewPagerBaseData

/**
 * 继承QzlViewPagerBaseData 使用时定义Data title可空
 */
class UserData(title: String?, val picUrl: String,val url:String?) : QzlViewPagerBaseData(title)