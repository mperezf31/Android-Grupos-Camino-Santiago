package mperezf.mimo.gruposcaminosantiago.presentation.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp

abstract class BaseViewModel : ViewModel() {

    val context = CaminoDeSantiagoApp.instance

    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    abstract fun dispose()

}