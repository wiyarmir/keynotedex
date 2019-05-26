package es.guillermoorellana.keynotedex.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

actual class SessionStorage(
    applicationContext: Context,
    private val prefs: SharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
) {

    @SuppressLint("ApplySharedPref")
    actual fun put(value: String?) {
        prefs.edit().putString(KEY_SESSION, value).commit()
    }

    actual fun get(): String? = prefs.getString(KEY_SESSION, null)

    actual fun clear() = put(null)
}
