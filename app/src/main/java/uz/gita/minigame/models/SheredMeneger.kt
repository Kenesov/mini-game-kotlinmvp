package uz.gita.minigame.models

import android.content.Context


object SheredMeneger {
    private fun prefs(context: Context) = context.getSharedPreferences("mini_game_pref", Context.MODE_PRIVATE)

    fun saveUserName(context: Context, userName: String){
        prefs(context).edit()
            .putString("user_name", userName)
            .apply()
    }
    fun getUserName(context: Context): String? =
        prefs(context).getString("user_name",null)

    fun saveLevel(context: Context, level: Int){
        prefs(context).edit()
            .putInt("level",level)
            .apply()
    }
    fun getLevel(context: Context): Int =
        prefs(context).getInt("level",0)

    fun clear(context: Context){
        prefs(context).edit().clear().apply()
    }
}