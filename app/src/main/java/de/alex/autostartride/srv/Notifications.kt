package de.alex.autostartride.srv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import de.alex.autostartride.R
import android.app.PendingIntent
import android.content.Intent
import de.alex.autostartride.ui.SettingsActivity

object Notifications {
  private const val CHANNEL_ID = "autostartride"

  private fun ensureChannel(ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val mgr = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val ch = NotificationChannel(CHANNEL_ID, ctx.getString(R.string.notif_channel_name), NotificationManager.IMPORTANCE_LOW)
      mgr.createNotificationChannel(ch)
    }
  }
/*
  fun build(ctx: Context, text: String): Notification {
    ensureChannel(ctx)
    return NotificationCompat.Builder(ctx, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_media_play)
      .setContentTitle(ctx.getString(R.string.notif_title))
      .setContentText(text)
      .setOngoing(true)
      .build()
  }

*/
fun build(ctx: Context, text: String): Notification {
    ensureChannel(ctx)
    val intent = Intent(ctx, SettingsActivity::class.java)
    val pi = PendingIntent.getActivity(
        ctx, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    return NotificationCompat.Builder(ctx, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_menu_manage)
        .setContentTitle(ctx.getString(R.string.notif_title))
        .setContentText(text)
        .setOngoing(true)
        .setContentIntent(pi) // <-- Tippen auf die Benachrichtigung öffnet die Einstellungen
        .addAction(0, "Einstellungen", pi) // <-- Zusätzlicher Button in der Benachrichtigung
        .build()
}

}
