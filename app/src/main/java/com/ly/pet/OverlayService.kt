package com.ly.pet

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.webkit.WebView

class OverlayService : Service() {
    private var webView: WebView? = null
    private var windowManager: WindowManager? = null

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        try {
            // 前台服务通知（系统不杀后台）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "pet_channel", "桌宠服务",
                    NotificationManager.IMPORTANCE_LOW
                )
                val nm = getSystemService(NotificationManager::class.java)
                nm.createNotificationChannel(channel)
                val notification: Notification = Notification.Builder(this, "pet_channel")
                    .setContentTitle("祁煜桌宠")
                    .setContentText("哥哥在这儿陪你")
                    .setSmallIcon(android.R.drawable.ic_menu_myplaces)
                    .build()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE)
                } else {
                    startForeground(1, notification)
                }
            }

            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            webView = WebView(this).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                loadUrl("file:///android_asset/pet.html")
            }

            val params = WindowManager.LayoutParams(
                300,
                300,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 100
                y = 100
            }

            windowManager?.addView(webView, params)
        } catch (e: Exception) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            webView?.let {
                windowManager?.removeView(it)
            }
        } catch (e: Exception) {
        }
        webView = null
    }
}
