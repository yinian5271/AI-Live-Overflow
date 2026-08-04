package com.ly.pet

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView

class PetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 【唯一改动】启动悬浮窗服务，让桌宠出现
        startService(Intent(this, OverlayService::class.java))
        // 以下是原版测试页
        val tv = TextView(this)
        tv.text = "桌宠测试"
        tv.setBackgroundColor(Color.RED)
        setContentView(tv)
    }
}
