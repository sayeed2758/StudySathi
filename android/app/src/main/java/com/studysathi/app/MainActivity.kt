package com.studysathi.app

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import java.util.TreeMap

data class LaunchableApp(val label: String, val packageName: String)

class MainActivity : ComponentActivity() {

    private val prefs by lazy {
        getSharedPreferences("studysathi", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun render() {
        val blocked = intent.getBooleanExtra("blocked", false)
        if (blocked) {
            renderBlocked()
            return
        }

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(44, 52, 44, 36)
        }

        root.addView(TextView(this).apply {
            text = "StudySathi"
            textSize = 28f
        })

        root.addView(TextView(this).apply {
            text = "Step 3 • Choose apps to protect"
            textSize = 18f
            setPadding(0, 12, 0, 8)
        })

        root.addView(TextView(this).apply {
            text = "Select the apps that StudySathi should handle during an active focus session. Your selection is stored locally on this device."
            textSize = 14f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            setPadding(0, 0, 0, 18)
        })

        val start = Button(this).apply {
            text = if (prefs.getBoolean("focusActive", false)) "Stop Focus Mode" else "Start Focus Mode"
            setOnClickListener {
                val current = prefs.getBoolean("focusActive", false)
                prefs.edit().putBoolean("focusActive", !current).apply()
                render()
            }
        }
        root.addView(start)

        root.addView(Button(this).apply {
            text = "Open Accessibility Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        })

        root.addView(Button(this).apply {
            text = "Save Selected Apps"
            setOnClickListener { render() }
        })

        val scroll = ScrollView(this)
        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 14, 0, 0)
        }

        val selected = prefs.getStringSet("selectedApps", emptySet())?.toMutableSet() ?: mutableSetOf()

        val apps = loadLaunchableApps()
        apps.forEach { app ->
            val cb = CheckBox(this).apply {
                text = app.label
                isChecked = selected.contains(app.packageName)
                setPadding(0, 8, 0, 8)
                textSize = 16f
                setOnCheckedChangeListener { _, checked ->
                    if (checked) selected.add(app.packageName) else selected.remove(app.packageName)
                    prefs.edit().putStringSet("selectedApps", selected).apply()
                }
            }
            list.addView(cb)
        }

        scroll.addView(list)
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        val count = TextView(this).apply {
            gravity = Gravity.CENTER
            text = "Selected: ${selected.size} app(s)"
            setPadding(0, 12, 0, 0)
        }
        root.addView(count)

        setContentView(root)
    }

    private fun loadLaunchableApps(): List<LaunchableApp> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val results = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        val apps = TreeMap<String, LaunchableApp>(String.CASE_INSENSITIVE_ORDER)

        for (resolveInfo in results) {
            val ai: ApplicationInfo = resolveInfo.activityInfo.applicationInfo
            val pkg = ai.packageName
            if (pkg == packageName) continue
            val label = ai.loadLabel(packageManager).toString().trim()
            if (label.isNotEmpty()) {
                apps["$label|$pkg"] = LaunchableApp(label, pkg)
            }
        }
        return apps.values.toList()
    }

    private fun renderBlocked() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(44, 50, 44, 50)
        }

        root.addView(TextView(this).apply {
            text = "⏳ Focus Mode is active"
            textSize = 28f
            gravity = Gravity.CENTER
        })

        root.addView(TextView(this).apply {
            text = "This app is part of your protected focus session.\n\nReturn to StudySathi and continue your study plan."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 24)
        })

        root.addView(Button(this).apply {
            text = "Return to StudySathi"
            setOnClickListener { finish() }
        })

        setContentView(root)
    }
}
