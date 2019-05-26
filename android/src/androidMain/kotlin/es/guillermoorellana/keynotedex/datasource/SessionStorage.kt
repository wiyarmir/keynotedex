package es.guillermoorellana.keynotedex.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SessionStorage(
    applicationContext: Context,
    private val prefs: SharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
) {

    @SuppressLint("ApplySharedPref")
    fun put(value: String?) {
        prefs.edit().putString(KEY_SESSION, value).commit()
    }

    fun get(): String? = prefs.getString(KEY_SESSION, null)

    fun clear() = put(null)
}
