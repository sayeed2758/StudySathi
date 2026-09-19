package com.studysathi.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class FocusAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if(event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val seen=event.packageName?.toString() ?: return
        if(seen==packageName) return
        val prefs=getSharedPreferences("studysathi",MODE_PRIVATE)
        if(!prefs.getBoolean("focusActive",false)) return
        val selected=prefs.getStringSet("selectedApps",emptySet()) ?: emptySet()
        if(seen in selected){
            performGlobalAction(GLOBAL_ACTION_BACK)
            startActivity(Intent(this,MainActivity::class.java).apply{
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("blocked",true)
            })
        }
    }
    override fun onInterrupt()=Unit
}
