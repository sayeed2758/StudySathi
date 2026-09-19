package com.studysathi.app

import android.content.Context
import android.content.Intent
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

data class LaunchableApp(val label: String, val packageName: String)

class MainActivity : ComponentActivity() {
    private val prefs by lazy { getSharedPreferences("studysathi", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("blocked", false)) renderBlocked() else render()
    }

    private fun render() {
        val root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(40,50,40,30) }

        root.addView(TextView(this).apply { text="StudySathi"; textSize=28f })
        root.addView(TextView(this).apply { text="Distraction Protection • Step 3"; textSize=18f; setPadding(0,10,0,8) })
        root.addView(TextView(this).apply {
            text="Select the apps you want StudySathi to handle during an active focus session. Selection stays on this device."
            textSize=14f; setPadding(0,0,0,16)
        })

        root.addView(Button(this).apply {
            text=if (prefs.getBoolean("focusActive",false)) "Stop Focus Mode" else "Start Focus Mode"
            setOnClickListener {
                val next=!prefs.getBoolean("focusActive",false)
                prefs.edit().putBoolean("focusActive",next).apply()
                render()
            }
        })
        root.addView(Button(this).apply {
            text="Open Accessibility Settings"
            setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
        })

        val selected=prefs.getStringSet("selectedApps",emptySet())?.toMutableSet() ?: mutableSetOf()
        val scroll=ScrollView(this)
        val list=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(0,12,0,0) }

        loadLaunchableApps().forEach { app ->
            list.addView(CheckBox(this).apply {
                text=app.label; textSize=16f; isChecked=selected.contains(app.packageName)
                setPadding(0,8,0,8)
                setOnCheckedChangeListener { _,checked ->
                    if (checked) selected.add(app.packageName) else selected.remove(app.packageName)
                    prefs.edit().putStringSet("selectedApps",selected).apply()
                }
            })
        }
        scroll.addView(list)
        root.addView(scroll,LinearLayout.LayoutParams(-1,0,1f))
        root.addView(TextView(this).apply { text="Selected apps: ${selected.size}"; gravity=Gravity.CENTER; setPadding(0,10,0,0) })
        setContentView(root)
    }

    private fun loadLaunchableApps(): List<LaunchableApp> {
        val intent=Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val entries=packageManager.queryIntentActivities(intent,PackageManager.MATCH_ALL)
        return entries.mapNotNull {
            val pkg=it.activityInfo.applicationInfo.packageName
            if(pkg==packageName) null else LaunchableApp(
                it.activityInfo.applicationInfo.loadLabel(packageManager).toString(),pkg
            )
        }.distinctBy{it.packageName}.sortedBy{it.label.lowercase()}
    }

    private fun renderBlocked() {
        val root=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; gravity=Gravity.CENTER; setPadding(42,40,42,40) }
        root.addView(TextView(this).apply { text="⏳ Focus Mode is active"; textSize=28f; gravity=Gravity.CENTER })
        root.addView(TextView(this).apply {
            text="This app is part of your protected study session. Return to StudySathi and keep going."
            textSize=16f; gravity=Gravity.CENTER; setPadding(0,22,0,22)
        })
        root.addView(Button(this).apply { text="Return to StudySathi"; setOnClickListener{finish()} })
        setContentView(root)
    }
}
