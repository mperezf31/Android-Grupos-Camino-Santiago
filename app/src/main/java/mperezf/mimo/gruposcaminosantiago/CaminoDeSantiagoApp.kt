package mperezf.mimo.gruposcaminosantiago

import android.app.Application
import mperezf.mimo.gruposcaminosantiago.data.Repository
import mperezf.mimo.gruposcaminosantiago.data.local.DataPersistence
import mperezf.mimo.gruposcaminosantiago.data.local.LocalStorage
import mperezf.mimo.gruposcaminosantiago.data.remote.ApiService
import mperezf.mimo.gruposcaminosantiago.data.remote.RetrofitClient
import mperezf.mimo.gruposcaminosantiago.domain.DataStorage

class CaminoDeSantiagoApp : Application() {

    private lateinit var repository: Repository
    
    override fun onCreate() {
        super.onCreate()

        createRepositoryInstance()
    }

    private fun createRepositoryInstance() {
        val apiService: ApiService = RetrofitClient(BuildConfig.SERVER_URL).getApiService()
        val localStorage: DataPersistence = LocalStorage(this)
        repository = Repository(apiService, localStorage)
    }

    fun getDataStorage(): DataStorage {
        return repository
    }

}