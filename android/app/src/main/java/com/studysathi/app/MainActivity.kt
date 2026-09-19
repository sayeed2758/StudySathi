package com.studysathi.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private val prefs by lazy { getSharedPreferences("studysathi", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render(intent.getBooleanExtra("blocked", false))
    }

    private fun render(blocked: Boolean) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 64, 48, 40)
        }

        root.addView(TextView(this).apply {
            text = if (blocked) "Focus Mode is active" else "StudySathi — Focus Protection"
            textSize = 28f
        })

        root.addView(TextView(this).apply {
            text = if (blocked)
                "This app is paused until your focus session ends."
            else
                "Accessibility access is requested only to detect a selected app opening during an active focus session. No message or password contents are required by this prototype."
            textSize = 16f
            setPadding(0, 24, 0, 30)
        })

        if (!blocked) {
            root.addView(Button(this).apply {
                text = "Open Accessibility Settings"
                setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
            })
            root.addView(Button(this).apply {
                text = if (prefs.getBoolean("focusActive", false)) "Stop Focus Mode" else "Start Focus Mode"
                setOnClickListener {
                    prefs.edit().putBoolean("focusActive", !prefs.getBoolean("focusActive", false)).apply()
                    render(false)
                }
            })
        } else {
            root.addView(Button(this).apply {
                text = "Return to StudySathi"
                setOnClickListener { finish() }
            })
        }

        setContentView(root)
    }
}
