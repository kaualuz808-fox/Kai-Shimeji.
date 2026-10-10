
package com.kaishimeji

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(0xFF151923.toInt())
        }

        val title = TextView(this).apply {
            text = "🐺 Kai-Shimeji"
            textSize = 30f
            setTextColor(0xFF65BFFF.toInt())
            gravity = Gravity.CENTER
        }

        val description = TextView(this).apply {
            text = "Seu companheiro virtual está chegando!"
            textSize = 16f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 32)
        }

        val button = Button(this).apply {
            text = "Ativar o Kai"

            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                } else {
                    startService(
                        Intent(
                            this@MainActivity,
                            PetOverlayService::class.java
                        )
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "Kai está na tela! 🐺",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        layout.addView(title)
        layout.addView(description)
        layout.addView(
            button,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(layout)
    }
}
