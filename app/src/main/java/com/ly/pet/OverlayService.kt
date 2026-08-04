package com.ly.pet

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONObject

class OverlayService : Service() {
    private var webView: WebView? = null
    private var windowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null

    // 拖拽状态
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var isDragging = false

    override fun onBind(intent: Intent): IBinder? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        try {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

            webView = WebView(this).apply {
                setBackgroundColor(Color.TRANSPARENT)
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                webViewClient = WebViewClient()
                loadUrl("file:///android_asset/pet.html")
                // PetBridge - JS可调用的桥
                addJavascriptInterface(PetBridge(), "PetBridge")
                // 触摸监听：拖拽移动
                setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params!!.x
                            initialY = params!!.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            isDragging = false
                            true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val dx = (event.rawX - initialTouchX).toInt()
                            val dy = (event.rawY - initialTouchY).toInt()
                            if (Math.abs(dx) > 5 || Math.abs(dy) > 5) {
                                isDragging = true
                                params!!.x = initialX + dx
                                params!!.y = initialY + dy
                                windowManager?.updateViewLayout(this, params)
                            }
                            true
                        }
                        MotionEvent.ACTION_UP -> {
                            // 如果不是拖拽（是点击），通知HTML处理点击
                            if (!isDragging) {
                                loadUrl("javascript:window.onPetTap && onPetTap()")
                            }
                            true
                        }
                        else -> false
                    }
                }
            }

            params = WindowManager.LayoutParams(
                400,
                500,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 50
                y = 50
            }

            windowManager?.addView(webView, params)
        } catch (e: Exception) {
            stopSelf()
        }
    }

    // JS调用原生的桥
    inner class PetBridge {
        @JavascriptInterface
        fun getPetState(): String {
            val state = JSONObject()
            state.put("mood", "happy")
            state.put("energy", 80)
            state.put("connected", true)
            state.put("greeting", "哥哥在")
            return state.toString()
        }

        @JavascriptInterface
        fun reportTap(type: String) {
            // 上报手势给AI（后续接Supabase）
        }
    }

    private fun loadUrl(js: String) {
        webView?.post { webView?.loadUrl(js) }
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
