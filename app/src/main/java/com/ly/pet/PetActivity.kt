package com.ly.pet

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class PetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启动悬浮窗服务，让桌宠出现在屏幕上
        startService(Intent(this, OverlayService::class.java))
        // 关闭自己，让桌宠直接悬浮
        finish()
    }
}
