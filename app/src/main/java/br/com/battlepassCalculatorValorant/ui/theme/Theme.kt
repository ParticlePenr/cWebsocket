package br.com.battlepassCalculatorValorant.ui.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import br.com.battlepassCalculatorValorant.model.DataBase.MySharedPreferences

class Theme(val context: Context) {
    private val myThemes = arrayListOf(
        AppCompatDelegate.MODE_NIGHT_NO,
        AppCompatDelegate.MODE_NIGHT_YES,
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    )

    fun checkTheme() {
        AppCompatDelegate.setDefaultNightMode(myThemes[MySharedPreferences(context).darkMode])
    }

    fun setThemeMode(mode: Int) {
        MySharedPreferences(context).darkMode = mode
        checkTheme()
    }

    fun getThemeMode(): Int {
        return MySharedPreferences(context).darkMode
    }
}