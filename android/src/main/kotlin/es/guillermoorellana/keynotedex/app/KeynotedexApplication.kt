package es.guillermoorellana.keynotedex.app

import android.app.Application
import android.content.Context
import es.guillermoorellana.keynotedex.di.NotDagger

class KeynotedexApplication : Application() {

    val notDagger = NotDagger()

    override fun onCreate() {
        super.onCreate()
        notDagger.sessionStorage.proxy = AndroidSessionStorage(applicationContext)
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
