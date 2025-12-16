package uz.gita.minigame.domain

import android.content.Context
import uz.gita.minigame.models.SheredMeneger
import uz.gita.minigame.models.data.UserData

class AppRepositoryImpl private constructor(): AppRepository {
    private val prefs = SheredMeneger

    companion object {
        @Volatile
        private var instance: AppRepositoryImpl? = null

        fun getInstance(): AppRepositoryImpl {
            return instance ?: synchronized(this) {
                instance ?: AppRepositoryImpl().also { instance = it }
            }
        }
    }

    override fun register(context: Context, name: String) {
        prefs.saveUserName(context, name)
    }

    override fun getUserInfo(context: Context): UserData? {
        val name = prefs.getUserName(context)
        return name?.let { UserData(it, prefs.getLevel(context)) }

    }

    override fun getLevel(context: Context): Int = prefs.getLevel(context)

    override fun setLevel(context: Context, level: Int) = prefs.saveLevel(context, level)

    override fun reset(context: Context) {
        prefs.saveLevel(context,0)
    }
}