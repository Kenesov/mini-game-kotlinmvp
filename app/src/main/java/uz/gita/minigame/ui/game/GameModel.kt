package uz.gita.minigame.ui.game

import android.content.Context
import uz.gita.minigame.domain.AppRepositoryImpl
import uz.gita.minigame.models.QuestionRepository
import uz.gita.minigame.models.data.QuestionData

class GameModel(private val context: Context): GameContract.Model {
    private val repo = AppRepositoryImpl.getInstance()
    private val questionrepo = QuestionRepository
    private var level = repo.getLevel(context)

    override fun getCurrentLevel(): Int = level

    override fun setLevel(level: Int) {
        this.level = level
        repo.setLevel(context, level)
    }
    override fun getByLevelQuestion(index: Int): QuestionData = questionrepo.getByIndexQuestion(index)
    override fun getTotalQuestions(): Int {
        return questionrepo.questions.size
    }

}