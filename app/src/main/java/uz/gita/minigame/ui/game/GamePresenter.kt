package uz.gita.minigame.ui.game

import android.util.Log
import uz.gita.minigame.models.data.QuestionData

class GamePresenter(
    private val view: GameContract.View,
    private val model: GameContract.Model
) : GameContract.Presenter {

    private lateinit var questionData: QuestionData
    private val answerState = mutableListOf<String?>()
    private val answerToVariantMap: MutableMap<Int, Int> = HashMap()
    private var isAnswered: Boolean = false
    private var isCorrectAnswer: Boolean = false
    private var currentQuestionIndex: Int = 0

    override fun loadQuestionByLevel(index: Int) {

        try {
            isAnswered = false
            isCorrectAnswer = false
            currentQuestionIndex = index

            questionData = model.getByLevelQuestion(index)

            view.setLevel(index)
            view.setQuestion(questionData.image)

            view.createAnswers(questionData.answer.length)

            view.resetBackgroun()

            val shuffledVariants =
                questionData.variant.toList().shuffled().joinToString("")

            view.setVariant(index, shuffledVariants)

            answerState.clear()
            answerToVariantMap.clear()

            repeat(questionData.answer.length) {
                answerState.add("")
                view.answerVisible(it)
            }



        } catch (e: Exception) {
            view.showError("Savolni yuklashda xatolik: ${e.message}")
        }
    }

    override fun onClickAnswer(index: Int) {
        if (index >= answerState.size || answerState[index] == null || answerState[index]?.isEmpty() != false) {
            return
        }

        if (isAnswered) {
            view.resetAnswerBackgrounds()
            isAnswered = false
        }

        answerToVariantMap[index]?.let { variantIndex ->
            view.variantVisible(variantIndex)
            answerToVariantMap.remove(index)
        }

        answerState[index] = ""
        view.setAnswer(index, "")

    }

    override fun onClickVariant(index: Int, value: String) {
        if (isCorrectAnswer) {
            return
        }

        if (isAnswered) {
            view.resetAnswerBackgrounds()
            isAnswered = false
        }

        val answerIndex = answerState.indexOf("")
        if (answerIndex == -1) {
            return
        }

        answerState[answerIndex] = value
        answerToVariantMap[answerIndex] = index
        view.variantInvisible(index)
        view.setAnswer(answerIndex, value)



    }

    override fun onClickFinish() {
        if (answerState.contains("")) {
            view.showError("Javobni to'liq kiriting!")
            return
        }

        checkAnswer()

    }


    override fun onClickBack() {
        view.navigateMain(false)
    }

    override fun checkAnswer() {
        val userAnswer = answerState.filterNotNull().joinToString("")
        isAnswered = true

        if (userAnswer.equals(questionData.answer, ignoreCase = true)) {
            isCorrectAnswer = true
            view.showCorrectDialog()
        } else {
            isCorrectAnswer = false
            view.showWrongAnswer()
        }
    }
    override fun onNextClicked() {
        val currentLevel = model.getCurrentLevel()

        if (currentQuestionIndex >= currentLevel) {
            model.setLevel(currentQuestionIndex + 1)
        }

        view.navigateMain(true)
    }
}