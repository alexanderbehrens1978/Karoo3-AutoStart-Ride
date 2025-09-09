/*
/*
package de.alex.autostartride.ext

import io.hammerhead.karooext.KarooExtension
import io.hammerhead.karooext.events.RideState
import io.hammerhead.karooext.effects.InRideAlert

/**
 * Minimal Karoo Extension that shows a small alert once recording is active.
 * The auto-start is handled by the foreground service.
 */
class AutoStartRideExtension : KarooExtension("autostartride", "1") {
  override fun onCreate() {
    super.onCreate()
    karooSystem.connect {
      karooSystem.addConsumer<RideState> { s ->
        if (s is RideState.Recording) {
          karooSystem.dispatch(
            InRideAlert(
              id = "asr_started",
              title = "Aufzeichnung läuft",
              detail = "AutoStart Ride aktiv",
              autoDismissMs = 3000
            )
          )
        }
      }
    }
  }
}
*/

package de.alex.autostartride.ext

import io.hammerhead.karooext.extension.KarooExtension
//import io.hammerhead.karooext.KarooSystemService
//import io.hammerhead.karooext.models.InRideAlert
//import io.hammerhead.karooext.models.RideState

/**
 * Registriert die Extension bei Karoo OS (siehe Manifest + extension_info.xml).
 * Für die Kernlogik verwenden wir den Service (AutoStartRideService).
 * Diese Klasse kann optional kleine UI/Alerts pushen.
 */
class AutoStartRideExtension : KarooExtension("autostartride", "1") {

  private lateinit var karoo: KarooSystemService

  override fun onCreate() {
    super.onCreate()

    karoo = KarooSystemService(this)

    karoo.connect {
      // Zeige einmalig einen kurzen Hinweis, wenn bereits Recording läuft
      karoo.addConsumer<RideState> { state ->
        if (state is RideState.Recording) {
          karoo.dispatch(
            InRideAlert(
              id = "asr_started",
              title = "Aufzeichnung läuft",
              detail = "AutoStart Ride aktiv",
              autoDismissMs = 3000
            )
          )
        }
      }
    }
  }

  override fun onDestroy() {
    if (::karoo.isInitialized) karoo.disconnect()
    super.onDestroy()
  }
}
*/

package de.alex.autostartride.ext

import io.hammerhead.karooext.extension.KarooExtension

/** Registriert die Extension; Logik macht der Service. */
class AutoStartRideExtension : KarooExtension("autostartride", "1")

