/*
/*
/*
package de.alex.autostartride.srv

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.hammerhead.karooext.KarooSystemService
import io.hammerhead.karooext.actions.PerformHardwareAction
import io.hammerhead.karooext.actions.ShowMapPage
//import io.hammerhead.karooext.events.RideState
import io.hammerhead.karooext.effects.ResumeRide

/**
 * Foreground service that connects to the Karoo system and ensures a ride is recording.
 * NOTE: The exact package names/classes may differ depending on karoo-ext version.
 * Adjust imports accordingly to the version you're using.
 */
class AutoStartRideService : Service() {

  private lateinit var karoo: KarooSystemService

  override fun onCreate() {
    super.onCreate()
    startForeground(1, Notifications.build(this, "AutoStart Ride ist aktiv"))
    karoo = KarooSystemService(this)

    karoo.connect {
      // 1) Bring map/ride UI to foreground (optional but helpful)
      karoo.dispatch(ShowMapPage(zoom = true))

      // 2) Observe ride state and trigger start if not recording
      karoo.addConsumer<RideState> { state ->
        when (state) {
          is RideState.NotRecording -> {
            // Simulate bottom-right hardware key (typically "Start")
            karoo.dispatch(PerformHardwareAction.BottomRightPress)
          }
          is RideState.Paused -> {
            karoo.dispatch(ResumeRide)
          }
          else -> { /* already recording -> nothing */ }
        }
      }
    }
  }

  override fun onBind(intent: Intent?): IBinder? = null
}
*/

package de.alex.autostartride.srv

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.hammerhead.karooext.KarooSystemService
import io.hammerhead.karooext.models.PerformHardwareAction
import io.hammerhead.karooext.models.ShowMapPage
import io.hammerhead.karooext.models.ResumeRide

class AutoStartRideService : Service() {

  private lateinit var karoo: KarooSystemService

  override fun onCreate() {
    super.onCreate()
    startForeground(1, Notifications.build(this, "AutoStart Ride ist aktiv"))

    karoo = KarooSystemService(this)

    karoo.connect {
      // 1) Ride-/Map-UI nach vorne holen
      karoo.dispatch(ShowMapPage(zoom = true))

      // 2) Start-Button simulieren (entspricht "Start Recording")
      karoo.dispatch(PerformHardwareAction.BottomRightPress)

      // 3) Falls pausiert war: wieder aufnehmen (unschädlich, wenn schon läuft)
      karoo.dispatch(ResumeRide)
    }
  }

  override fun onDestroy() {
    if (::karoo.isInitialized) karoo.disconnect()
    super.onDestroy()
  }

  override fun onBind(intent: Intent?): IBinder? = null
}
*/

package de.alex.autostartride.srv

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.hammerhead.karooext.KarooSystemService
import io.hammerhead.karooext.models.PerformHardwareAction
import io.hammerhead.karooext.models.ShowMapPage
import io.hammerhead.karooext.models.ResumeRide

class AutoStartRideService : Service() {

  private lateinit var karoo: KarooSystemService

  override fun onCreate() {
    super.onCreate()
    startForeground(1, Notifications.build(this, "AutoStart Ride ist aktiv"))

    karoo = KarooSystemService(this)
    karoo.connect {
      // Map-Seite in den Vordergrund
      karoo.dispatch(ShowMapPage(zoom = true))
      // Start simulieren (entspricht Start/Bestätigen)
      karoo.dispatch(PerformHardwareAction.BottomRightPress)
      // Falls pausiert war: fortsetzen (unschädlich, wenn schon recording)
      karoo.dispatch(ResumeRide)
    }
  }

  override fun onDestroy() {
    if (::karoo.isInitialized) karoo.disconnect()
    super.onDestroy()
  }

  override fun onBind(intent: Intent?): IBinder? = null
}

*/

package de.alex.autostartride.srv

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.hammerhead.karooext.KarooSystemService
import io.hammerhead.karooext.models.PerformHardwareAction
import io.hammerhead.karooext.models.ShowMapPage
import io.hammerhead.karooext.models.ResumeRide
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import de.alex.autostartride.data.settingsFlow

class AutoStartRideService : Service() {

  private lateinit var karoo: KarooSystemService
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

  override fun onCreate() {
    super.onCreate()
    startForeground(1, Notifications.build(this, "AutoStart Ride ist aktiv"))

    scope.launch {
      val s = settingsFlow(this@AutoStartRideService).first()

      if (!s.autostartEnabled) {
        stopSelf(); return@launch
      }
      if (s.startDelayMs > 0) delay(s.startDelayMs.toLong())

      karoo = KarooSystemService(this@AutoStartRideService)
      karoo.connect {
        // UI nach vorn
        karoo.dispatch(ShowMapPage(zoom = true))
        // Start simulieren
        karoo.dispatch(PerformHardwareAction.BottomRightPress)
        // optional: „pausiert -> fortsetzen“ (unschädlich, wenn nicht pausiert)
        if (s.resumeOnPause) karoo.dispatch(ResumeRide)
      }
    }
  }

  override fun onDestroy() {
    if (::karoo.isInitialized) karoo.disconnect()
    scope.cancel()
    super.onDestroy()
  }

  override fun onBind(intent: Intent?): IBinder? = null
}

