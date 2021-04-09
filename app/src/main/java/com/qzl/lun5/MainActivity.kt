package com.qzl.lun5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = initData()
        val adapter = UserAdapter(data, this@MainActivity)

        qzlViewPagerHolder.apply {
            setAdapter(adapter)
            setDelay(3000)
            setIndicatorStyle(getDrawable(R.drawable.oval_pink), getDrawable(R.drawable.oval_gray))
            setTitleBarColor("#55ff00ff")
            setTitleColor("#000000")
        }

        button.setOnClickListener {
            qzlViewPagerHolder.changeData(initData2())
        }

        button2.setOnClickListener {
            qzlViewPagerHolder.changeData(initData().plus(initData2()))
        }

    }

    private fun initData(): List<UserData> {
        return listOf(
            UserData(
                "1_1国风妆造PK，名师在线点评"
                ,
                "https://i0.hdslb.com/bfs/feed-admin/6f5c54330a8dafcc26fea7dbc822762e16ca63d4.jpg@880w_388h_1c_95q"
                ,
                "https://www.bilibili.com/blackboard/activity-70wmFGvk9.html"
            )
            , UserData(
                "1_2小猫咪赶海，满脸高冷！",
                "https://i0.hdslb.com/bfs/feed-admin/9bf24ccdb1adb14d27accaa34456035c6e15b36d.jpg@880w_388h_1c_95q",
                "https://www.bilibili.com/blackboard/activity-starpc.html"
            )
            , UserData(
                "1_3春日追剧需要有点甜~"
                ,
                "https://i0.hdslb.com/bfs/feed-admin/a0c4298a1584dc9edec11d83426a4f65e1491198.png@880w_388h_1c_95q"
                ,
                "https://www.bilibili.com/blackboard/activity-QEya2bouhQ.html"
            ), UserData(
                "1_4点击观看大结局，完结撒花！",
                "https://i0.hdslb.com/bfs/feed-admin/389b5c5ccf5ed24a3b03b2bb206087821e209df3.jpg@880w_388h_1c_95q",
                "https://www.bilibili.com/bangumi/play/ep373935/"
            )
        )
    }

    private fun initData2(): List<UserData> {
        return listOf(
            UserData(
                "2_1前半完结撒花！外传紧接播出~"
                ,
                "https://i0.hdslb.com/bfs/feed-admin/f3338b39cc7c6751b0441afe7a7c2e4ec1f41eea.jpg@880w_388h_1c_95q"
                ,
                null
            )
            , UserData(
                "2_2治愈田园慢生活，今日首发"
                ,
                "https://i0.hdslb.com/bfs/sycp/creative_img/202103/f23c7b9524bbf2a1296b0f1b703ffc72.jpg@880w_388h_1c_95q"
                ,
                null
            )
            , UserData(
                "2_3蜘蛛子的地上冒险要开始了~"
                ,
                "https://i0.hdslb.com/bfs/feed-admin/b47f3cc513be3b6947a464523004596e70a012c8.jpg@880w_388h_1c_95q"
                ,
                null
            )
        )
    }
}