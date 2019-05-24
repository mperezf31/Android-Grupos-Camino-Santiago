package mperezf.mimo.gruposcaminosantiago

import android.app.Application

class CaminoDeSantiagoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: CaminoDeSantiagoApp
            private set
    }
}