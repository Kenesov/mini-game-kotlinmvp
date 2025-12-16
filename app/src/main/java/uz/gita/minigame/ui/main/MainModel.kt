package uz.gita.minigame.ui.main

import android.content.Context
import uz.gita.minigame.domain.AppRepositoryImpl
import uz.gita.minigame.models.QuestionRepository
import uz.gita.minigame.models.data.UserData
class MainModel(
    private val context: Context
) : MainContract.Model {

    private val repository = AppRepositoryImpl.getInstance()
    private val questionRepository = QuestionRepository

    override fun isRegistered(): Boolean {
        return repository.getUserInfo(context) != null
    }

    override fun registerUser(name: String) {
        repository.register(context, name)
        repository.setLevel(context, 0)
    }

    override fun getUser(): UserData {
        return repository.getUserInfo(context)
            ?: throw IllegalStateException("User not registered")
    }

    override fun getQuestionImages(): List<Int> {
        return questionRepository.getImages()
    }

    override fun getTotalQuestionCount(): Int {
        return questionRepository.questions.size
    }

    override fun getCurrentLevel(): Int {
        return repository.getLevel(context)
    }

    override fun setLevel(level: Int) {
        repository.setLevel(context,level)
    }


    override fun resetGame() {
        repository.reset(context)
    }
}
