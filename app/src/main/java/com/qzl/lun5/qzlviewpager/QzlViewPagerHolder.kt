package com.qzl.lun5.qzlviewpager

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.qzl.lun5.UserData
import java.lang.reflect.Field

class QzlViewPagerHolder(mContext: Context, attrs: AttributeSet) :
    RelativeLayout(mContext, attrs, 0) {

    private lateinit var mTitle: TextView//标题
    private lateinit var mPager: QzlViewPager//轮播View
    private lateinit var mContainer: LinearLayout//指示器容器
    private lateinit var mDataList: List<QzlViewPagerBaseData>//数据列表

    //private lateinit var mAdapter: QzlViewPagerAdapter//适配器
    private var mBackgroundC: Drawable? = null//指示器 选中
    private var mBackground: Drawable? = null//指示器 未选择

    init {
        initView()
        initEvent()
    }

    //初始化View
    private fun initView() {
        //轮播图
        mPager = QzlViewPager(context).apply {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.MATCH_PARENT
            )

            this@QzlViewPagerHolder.addView(this, params)
        }

        //标题
        mTitle = TextView(context).apply {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT
                , LayoutParams.WRAP_CONTENT
            )
            gravity = Gravity.CENTER_HORIZONTAL//内容水平居中
            setTextColor(Color.parseColor("#ffffff"))//title文字颜色
            setTypeface(null, Typeface.BOLD)//文字加粗
            setBackgroundColor(Color.parseColor("#99666666"))//背景颜色
            bringToFront()//前置
            this@QzlViewPagerHolder.addView(this, params)
        }

        //指示器container
        mContainer = LinearLayout(context).apply {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT
                , LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(CENTER_HORIZONTAL)//水平居中
                addRule(ALIGN_PARENT_BOTTOM)//对齐parent底
                setMargins(0, 0, 0, DensityUtil.dip2px(context, 5f))
            }
            //layoutParams = params
            this@QzlViewPagerHolder.addView(this, params)
        }
    }

    //初始化Pager轮播图事件
    private fun initEvent() {
        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                /*
                * @see ViewPager#SCROLL_STATE_IDLE 停止
                * @see ViewPager#SCROLL_STATE_DRAGGING 拖拽
                * @see ViewPager#SCROLL_STATE_SETTLING 归位
                **/
                //切换的状态改变回调
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //切换时回调
            }

            override fun onPageSelected(position: Int) {
                //切换结束回调

                //获得数据个数 设置显示 indicator & title
                setDisplay(position)
            }
        })
    }

    private fun setDisplay(position: Int) {

        val realPosition = position % mDataList.size
        mTitle.text = mDataList[realPosition].title

        mContainer.removeAllViews()

        repeat(mDataList.size) {

            val layoutParams = LayoutParams(
                DensityUtil.dip2px(context, 5f)
                , DensityUtil.dip2px(context, 5f)
            ).apply {
                setMargins(
                    DensityUtil.dip2px(context, 5f)
                    , DensityUtil.dip2px(context, 0f)
                    , DensityUtil.dip2px(context, 5f)
                    , DensityUtil.dip2px(context, 0f)
                )
            }

            val view = View(context).apply {

                if (mBackgroundC == null && mBackground == null) {
                    //默认指示器格样式
                    if (realPosition == it) {
                        setBackgroundColor(Color.parseColor("#ff5555"))
                    } else {
                        setBackgroundColor(Color.parseColor("#555555"))
                    }
                } else {
                    //传入的样式
                    background = if (realPosition == it)
                        mBackgroundC
                    else mBackground
                }

                setLayoutParams(layoutParams)
            }

            mContainer.addView(view)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    //对外暴露的控制函数
    /*
    * setAdapter()
    * 设置ViewPager的Adapter
    * */
    fun setAdapter(adapter: QzlViewPagerAdapter) {
        //mAdapter = adapter
        mPager.adapter = adapter
        //mPager.currentItem = Int.MAX_VALUE / 2
        mDataList = adapter.getAdapterData()

        //获得数据个数 设置显示 indicator&title
        //设置轮播中间值
        val mid = (Int.MAX_VALUE / mDataList.size / 2) * mDataList.size
        setDisplay(mid)//设置title indicator显示

        mPager.setCurrentItem(mid, false)

        Log.i("size", mid.toString())

    }

    /*
    * setTitleBarColor(color: String)
    * 设置标题栏的背景颜色
    **/
    fun setTitleBarColor(color: String) {
        if (color.startsWith("#") || color.length in 7..9) {
            mTitle.setBackgroundColor(Color.parseColor(color))
        }
    }

    /*
    * setTitleColor(color: String)
    * 设置标题栏字体颜色
    * 默认白色#ffffff
    * */
    fun setTitleColor(color: String="#ffffff") {
        if (color.startsWith("#") || color.length in 7..9) {
            mTitle.setTextColor(Color.parseColor(color))
        }
    }

    /*
    * setDelay(time: Long = 2000)
    * 设置ViewPager滚动延时 默认2000ms
    */
    fun setDelay(time: Long = 2000) {
        mPager.setDelay(time)
    }

    /*
    * setIndicatorStyle(mBackgroundC: Drawable?, mBackground: Drawable?)
    * 设置Indicator样式 5dp*5dp 默认方形
    * mBackgroundC 选中项
    * mBackground 未选中项
    */
    fun setIndicatorStyle(mBackgroundC: Drawable?, mBackground: Drawable?) {
        this.mBackgroundC = mBackgroundC
        this.mBackground = mBackground
        setDisplay(mPager.currentItem)
    }

    /*
    * changeData(newData: List<UserData>)
    * 改变轮播数据
    * */
    fun changeData(newData: List<UserData>) {
        mPager.adapter?.apply {
            setAdapterData(newData)
            mDataList = newData

            //利用反射，强行修改mFirstLayout的值为true
            val mFirstLayout: Field =
                ViewPager::class.java.getDeclaredField("mFirstLayout")
            mFirstLayout.isAccessible = true
            mFirstLayout.set(mPager, true)

            notifyDataSetChanged()

            val mid = (Int.MAX_VALUE / mDataList.size / 2) * mDataList.size
            mPager.currentItem = mid
            setDisplay(mid)//设置title indicator显示

        }

        Log.i("changeData", mPager.currentItem.toString())

    }
}