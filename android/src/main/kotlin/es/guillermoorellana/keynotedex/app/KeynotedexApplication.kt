package es.guillermoorellana.keynotedex.app

import android.app.Application
import android.content.Context
import es.guillermoorellana.keynotedex.datasource.NetworkDataSource
import es.guillermoorellana.keynotedex.datasource.SessionStorage
import es.guillermoorellana.keynotedex.repository.NetworkRepository

class KeynotedexApplication : Application() {

    private fun getSessionStorage() = SessionStorage(applicationContext)

    val repository: NetworkRepository by lazy {
        NetworkRepository(
            dataSource = NetworkDataSource(
                sessionStorage = getSessionStorage()
            )
        )
    }

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            println(throwable)
            throwable.printStackTrace()
            throwable?.cause?.printStackTrace()
        }

    }
}

fun Application.asKeynotedexApplication() = keynotedexApplication
val Context.keynotedexApplication
    get() = this.applicationContext as KeynotedexApplication
