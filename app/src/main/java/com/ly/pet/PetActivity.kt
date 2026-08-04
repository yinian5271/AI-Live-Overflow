package com.ly.pet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

class PetActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 检查悬浮窗权限
        if (Settings.canDrawOverlays(this)) {
            startService(Intent(this, OverlayService::class.java))
            finish()
        } else {
            Toast.makeText(this, "请先允许悬浮窗权限", Toast.LENGTH_LONG).show()
            // 跳转到悬浮窗权限设置页
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            finish()
        }
    }
}
