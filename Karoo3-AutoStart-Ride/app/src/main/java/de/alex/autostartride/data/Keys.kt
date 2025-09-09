package de.alex.autostartride.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object Keys {
  val AUTOSTART_ENABLED = booleanPreferencesKey("autostart_enabled")
  val START_DELAY_MS    = intPreferencesKey("start_delay_ms")
  val RESUME_ON_PAUSE   = booleanPreferencesKey("resume_on_pause")
}

