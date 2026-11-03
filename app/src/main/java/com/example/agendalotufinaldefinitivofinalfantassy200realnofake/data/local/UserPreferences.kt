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
}
