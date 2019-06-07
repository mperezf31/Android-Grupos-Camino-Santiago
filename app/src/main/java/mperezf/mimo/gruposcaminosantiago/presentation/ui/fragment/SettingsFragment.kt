package mperezf.mimo.gruposcaminosantiago.presentation.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import mperezf.mimo.gruposcaminosantiago.R

class SettingsFragment : PreferenceFragmentCompat() {


    companion object{

        const val PREF_GROUPS_CREATED = "pref_groups_created"
        const val PREF_GROUPS_MEMBER = "pref_groups_member"
        const val PREF__OTHER_GROUPS = "pref_other_groups"

        @JvmStatic
        fun newInstance(): SettingsFragment = SettingsFragment()
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}