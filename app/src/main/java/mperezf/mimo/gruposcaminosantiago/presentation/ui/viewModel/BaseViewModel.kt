package mperezf.mimo.gruposcaminosantiago.presentation.ui.viewModel

import androidx.lifecycle.ViewModel
import mperezf.mimo.gruposcaminosantiago.CaminoDeSantiagoApp

open class BaseViewModel : ViewModel() {
    val context = CaminoDeSantiagoApp.instance
}