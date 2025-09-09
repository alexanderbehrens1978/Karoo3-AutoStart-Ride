package de.alex.autostartride.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.settingsDataStore by preferencesDataStore(name = "autostartride_settings")

data class Settings(
  val autostartEnabled: Boolean = true,
  val startDelayMs: Int = 0,
  val resumeOnPause: Boolean = true,
)

fun settingsFlow(ctx: Context): Flow<Settings> =
  ctx.settingsDataStore.data
    .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
    .map { p ->
      Settings(
        autostartEnabled = p[Keys.AUTOSTART_ENABLED] ?: true,
        startDelayMs     = p[Keys.START_DELAY_MS] ?: 0,
        resumeOnPause    = p[Keys.RESUME_ON_PAUSE] ?: true,
      )
    }

suspend fun setAutostart(ctx: Context, enabled: Boolean) {
  ctx.settingsDataStore.edit { it[Keys.AUTOSTART_ENABLED] = enabled }
}
suspend fun setStartDelay(ctx: Context, ms: Int) {
  ctx.settingsDataStore.edit { it[Keys.START_DELAY_MS] = ms }
}
suspend fun setResumeOnPause(ctx: Context, enabled: Boolean) {
  ctx.settingsDataStore.edit { it[Keys.RESUME_ON_PAUSE] = enabled }
}

