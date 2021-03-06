package com.souza.pokedetail.presentation.pokedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PokeDetailsActivityViewModel : ViewModel() {

    private var wasConnected = true
    private var showConnectivityOnSnackbar = MutableLiveData<Unit>()
    private var showConnectivityOffSnackbar = MutableLiveData<Unit>()

    fun showConnectivityOnSnackbar(): LiveData<Unit> = showConnectivityOnSnackbar
    fun showConnectivityOffSnackbar(): LiveData<Unit> = showConnectivityOffSnackbar

    fun mustShowConnectivitySnackbar(hasNetworkConnectivity: Boolean) {
        if (hasNetworkConnectivity.not()) {
            showConnectivityOffSnackbar.postValue(Unit)
            wasConnected = false
        } else if (wasConnected.not() && hasNetworkConnectivity) {
            showConnectivityOnSnackbar.postValue(Unit)
            wasConnected = true
        }
    }
}
