package com.studysathi.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class FocusAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val packageNameSeen = event.packageName?.toString() ?: return
        if (packageNameSeen == packageName) return

        val prefs = getSharedPreferences("studysathi", MODE_PRIVATE)
        val focusActive = prefs.getBoolean("focusActive", false)
        if (!focusActive) return

        val selectedApps = prefs.getStringSet("selectedApps", emptySet()) ?: emptySet()
        if (packageNameSeen in selectedApps) {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("blocked", true)
                }
            )
        }
    }

    override fun onInterrupt() = Unit
}
