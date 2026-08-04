package com.ly.pet

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class PetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启动悬浮窗服务（前台服务+通知）
        startForegroundService(Intent(this, OverlayService::class.java))

        // 简单界面：测试页 + 启动按钮
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(Color.RED)
        layout.gravity = android.view.Gravity.CENTER

        val tv = TextView(this)
        tv.text = "祁煜桌宠\n哥哥在这儿"
        tv.setTextColor(Color.WHITE)
        tv.textSize = 18f
        tv.gravity = android.view.Gravity.CENTER

        val btn = Button(this)
        btn.text = "启动悬浮窗"
        btn.setOnClickListener {
            startForegroundService(Intent(this, OverlayService::class.java))
        }

        layout.addView(tv, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f))
        layout.addView(btn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))

        setContentView(layout)
    }
}
