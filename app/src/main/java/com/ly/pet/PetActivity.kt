package com.ly.pet

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class PetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启动悬浮窗服务，让桌宠出现在屏幕上
        val intent = Intent(this, OverlayService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        // 关闭自己，让桌宠直接悬浮
        finish()
    }
}
