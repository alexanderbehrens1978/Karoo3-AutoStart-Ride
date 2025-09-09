package de.alex.autostartride.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import de.alex.autostartride.R
import de.alex.autostartride.data.*
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {
  private lateinit var swAutostart: Switch
  private lateinit var swResume: Switch
  private lateinit var edDelay: EditText
  private lateinit var btnSave: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    title = getString(R.string.settings_title)

    swAutostart = findViewById(R.id.switch_autostart)
    swResume    = findViewById(R.id.switch_resume)
    edDelay     = findViewById(R.id.edit_delay)
    btnSave     = findViewById(R.id.btn_save)

    // Laden/Beobachten
    lifecycleScope.launch {
      settingsFlow(this@SettingsActivity).collect { s ->
        if (!swAutostart.isPressed) swAutostart.isChecked = s.autostartEnabled
        if (!swResume.isPressed)    swResume.isChecked    = s.resumeOnPause
        if (!edDelay.isFocused)     edDelay.setText(s.startDelayMs.toString())
      }
    }

    // Live toggles
    swAutostart.setOnCheckedChangeListener { _, v ->
      lifecycleScope.launch { setAutostart(this@SettingsActivity, v) }
    }
    swResume.setOnCheckedChangeListener { _, v ->
      lifecycleScope.launch { setResumeOnPause(this@SettingsActivity, v) }
    }

    btnSave.setOnClickListener {
      val ms = edDelay.text.toString().toIntOrNull() ?: 0
      lifecycleScope.launch { setStartDelay(this@SettingsActivity, ms) }
      Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show()
    }
  }
}

