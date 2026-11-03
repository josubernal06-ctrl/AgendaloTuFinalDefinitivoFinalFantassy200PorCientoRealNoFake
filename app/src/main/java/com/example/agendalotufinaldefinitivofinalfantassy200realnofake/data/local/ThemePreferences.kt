package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import android.content.Context
import android.content.SharedPreferences

class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_theme_preferences", Context.MODE_PRIVATE)

    fun getSelectedThemeId(): Int {
        return prefs.getInt("selected_theme_id", 13) // Tema 13 por defecto
    }

    fun setSelectedThemeId(themeId: Int) {
        prefs.edit().putInt("selected_theme_id", themeId).apply()
    }
}
