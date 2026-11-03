package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_user_preferences", Context.MODE_PRIVATE)

    fun getNickname(emailKey: String, defaultNickname: String): String {
        return prefs.getString("nickname_$emailKey", null) ?: defaultNickname
    }

    fun setNickname(emailKey: String, newNickname: String) {
        prefs.edit().putString("nickname_$emailKey", newNickname).apply()
    }

    fun isNotificacionesEnabled(): Boolean {
        return prefs.getBoolean("notificaciones_enabled", true)
    }

    fun setNotificacionesEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notificaciones_enabled", enabled).apply()
    }

    fun isSonidoAlarmasEnabled(): Boolean {
        return prefs.getBoolean("sonido_alarmas_enabled", true)
    }

    fun setSonidoAlarmasEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("sonido_alarmas_enabled", enabled).apply()
    }
}
