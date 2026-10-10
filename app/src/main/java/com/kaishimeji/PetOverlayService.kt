
package com.kaishimeji

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView

class PetOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var petView: TextView? = null
    private lateinit var params: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !android.provider.Settings.canDrawOverlays(this)
        ) {
            stopSelf()
            return
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 250

        val pet = TextView(this).apply {
            text = "🐺"
            textSize = 56f
            setTextColor(Color.WHITE)
            setPadding(12, 12, 12, 12)
            contentDescription = "Kai, seu companheiro virtual"

            setOnTouchListener(object : View.OnTouchListener {
                private var startX = 0
                private var startY = 0
                private var touchX = 0f
                private var touchY = 0f

                override fun onTouch(
                    view: View,
                    event: MotionEvent
                ): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            startX = params.x
                            startY = params.y
                            touchX = event.rawX
                            touchY = event.rawY
                            return true
                        }

                        MotionEvent.ACTION_MOVE -> {
                            params.x = startX +
                                (event.rawX - touchX).toInt()
                            params.y = startY +
                                (event.rawY - touchY).toInt()

                            windowManager.updateViewLayout(
                                petView,
                                params
                            )
                            return true
                        }
                    }
                    return true
                }
            })
        }

        petView = pet
        windowManager.addView(pet, params)
    }

    override fun onDestroy() {
        petView?.let {
            if (it.isAttachedToWindow) {
                windowManager.removeView(it)
            }
        }
        petView = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
