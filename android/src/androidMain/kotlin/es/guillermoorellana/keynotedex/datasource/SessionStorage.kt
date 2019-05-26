package es.guillermoorellana.keynotedex.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class AndroidSessionStorage(
    applicationContext: Context,
    private val prefs: SharedPreferences = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
) : SessionStorage() {

    @SuppressLint("ApplySharedPref")
    override fun put(value: String?) {
        prefs.edit().putString(KEY_SESSION, value).commit()
    }

    override fun get(): String? = prefs.getString(KEY_SESSION, null)

    override fun clear() = put(null)
}
