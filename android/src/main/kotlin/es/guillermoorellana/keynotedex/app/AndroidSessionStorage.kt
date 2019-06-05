package es.guillermoorellana.keynotedex.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import es.guillermoorellana.keynotedex.datasource.KEY_SESSION
import es.guillermoorellana.keynotedex.datasource.SessionStorageProxy

class AndroidSessionStorage(
    applicationContext: Context,
    private val prefs: SharedPreferences =
        applicationContext.getSharedPreferences(
            "prefs",
            Context.MODE_PRIVATE
        )
) : SessionStorageProxy {

    @SuppressLint("ApplySharedPref")
    override fun put(value: String?) {
        prefs.edit()
            .putString(KEY_SESSION, value)
            .commit()
    }

    override fun get(): String? =
        prefs.getString(KEY_SESSION, null)

    override fun clear() =
        put(null)
}
