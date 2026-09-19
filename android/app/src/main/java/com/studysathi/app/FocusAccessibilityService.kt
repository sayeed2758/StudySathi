package com.studysathi.app

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class FocusAccessibilityService : AccessibilityService() {
    // Step 2 proof-of-concept list. Step 4 will replace this with user-selected apps.
    private val blockedPackages = setOf(
        "com.google.android.youtube",
        "com.instagram.android",
        "com.android.chrome"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        val focusActive = getSharedPreferences("studysathi", MODE_PRIVATE)
            .getBoolean("focusActive", false)

        if (focusActive && pkg in blockedPackages && pkg != packageName) {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra("blocked", true)
            })
        }
    }

    override fun onInterrupt() = Unit
}
