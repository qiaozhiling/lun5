package com.qzl.lun5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            intent.getStringExtra("url")?.let {
                loadUrl(it)//加载url
            }

        }
    }
}