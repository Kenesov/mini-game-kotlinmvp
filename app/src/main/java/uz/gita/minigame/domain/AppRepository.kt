package uz.gita.minigame.domain

import android.content.Context
import uz.gita.minigame.models.data.UserData

interface AppRepository {
    fun register(context: Context,name: String)
    fun getUserInfo(context: Context): UserData?
    fun getLevel(context: Context): Int
    fun setLevel(context: Context,level: Int)
    fun reset(context: Context)

}