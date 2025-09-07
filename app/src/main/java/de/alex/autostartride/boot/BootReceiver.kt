package de.alex.autostartride.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import de.alex.autostartride.srv.AutoStartRideService

class BootReceiver : BroadcastReceiver() {
  override fun onReceive(ctx: Context, intent: Intent) {
    val i = Intent(ctx, AutoStartRideService::class.java)
    ContextCompat.startForegroundService(ctx, i)
  }
}
