package com.arpi.rplauncher

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.KeyEvent

class GlobalKeyReceiver : BroadcastReceiver() {
    private val ACTION_GLOBAL_BUTTON = "android.intent.action.GLOBAL_BUTTON"

    override fun onReceive(context: Context, intent: Intent) {
        if (ACTION_GLOBAL_BUTTON == intent.action) {
            val event = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
            if (event != null) {
                if (event.action == KeyEvent.ACTION_DOWN) {
                    when (event.keyCode) {
                        KeyEvent.KEYCODE_F1 -> startHomeOnPrimaryDisplay(context)
                        KeyEvent.KEYCODE_F7 -> startVoskDemoOnPrimaryDisplay(context)
                    }
                }
            }
        }
    }

    private fun startHomeOnPrimaryDisplay(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addCategory(Intent.CATEGORY_HOME)
        val bundle = ActivityOptions.makeBasic()
            .setLaunchDisplayId(0).toBundle()
        context.startActivity(intent, bundle)
    }

    private fun startVoskDemoOnPrimaryDisplay(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setComponent(ComponentName("org.vosk.demo","org.vosk.demo.MainActivity"))
        val bundle = ActivityOptions.makeBasic()
            .setLaunchDisplayId(0).toBundle()
        context.startActivity(intent, bundle)
    }
}
