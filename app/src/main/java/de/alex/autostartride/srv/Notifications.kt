package de.alex.autostartride.srv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import de.alex.autostartride.R

object Notifications {
  private const val CHANNEL_ID = "autostartride"

  private fun ensureChannel(ctx: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val mgr = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      val ch = NotificationChannel(CHANNEL_ID, ctx.getString(R.string.notif_channel_name), NotificationManager.IMPORTANCE_LOW)
      mgr.createNotificationChannel(ch)
    }
  }

  fun build(ctx: Context, text: String): Notification {
    ensureChannel(ctx)
    return NotificationCompat.Builder(ctx, CHANNEL_ID)
      .setSmallIcon(android.R.drawable.ic_media_play)
      .setContentTitle(ctx.getString(R.string.notif_title))
      .setContentText(text)
      .setOngoing(true)
      .build()
  }
}
