package mperezf.mimo.gruposcaminosantiago

import android.app.Application

class CaminoDeSantiagoApp : Application() {

    companion object {
        lateinit var instance: CaminoDeSantiagoApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}