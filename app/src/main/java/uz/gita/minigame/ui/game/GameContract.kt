package uz.gita.minigame.ui.game

import uz.gita.minigame.models.data.QuestionData

interface GameContract {

    interface View {
        fun setLevel(level: Int)
        fun setQuestion(image: Int)
        fun setVariant(index: Int, value: String)
        fun setAnswer(index: Int, value: String)
        fun createAnswers(count: Int)

        fun answerVisible(index: Int)
        fun answerGone(index: Int)
        fun variantVisible(index: Int)
        fun variantInvisible(index: Int)

        fun showCorrectDialog()
        fun showError(message: String)
        fun resetBackgroun()
        fun showWrongAnswer()
        fun navigateMain(value: Boolean)
        fun resetAnswerBackgrounds()

    }

    interface Presenter {
        fun loadQuestionByLevel(index: Int)
        fun onClickAnswer(index: Int)

        fun onClickVariant(index: Int, value: String)

        fun onClickFinish()
        fun onClickBack()
        fun checkAnswer()
        fun onNextClicked()


    }

    interface Model {
        fun getCurrentLevel(): Int
        fun setLevel(level: Int)
        fun getByLevelQuestion(index: Int): QuestionData
        fun getTotalQuestions(): Int

    }
}