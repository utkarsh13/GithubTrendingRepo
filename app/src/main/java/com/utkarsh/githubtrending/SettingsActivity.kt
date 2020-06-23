package com.utkarsh.githubtrending

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.work.WorkManager
import com.utkarsh.githubtrending.utils.defaultSharedPreferences
import com.utkarsh.githubtrending.workmanager.RefreshDataWorker
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : BaseActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setSupportActionBar(settings_toolbar)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings_title)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        applicationContext.defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        applicationContext.defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        when (key) {
            "sync" -> {
                if (pref?.getBoolean(key, false) == true) {
                    setupRecurringWork()
                } else {
                    WorkManager.getInstance().cancelUniqueWork(RefreshDataWorker.WORK_NAME)
                }
            }
            "sync_hours" -> {
                setupRecurringWork()
            }
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }


    }
}