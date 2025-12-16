package uz.gita.minigame.ui.main

import uz.gita.minigame.models.data.UserData

interface MainContract {

    interface View {

        fun showRegisterDialog()

        fun showUserInfo(user: UserData)

        fun showQuestionImages(
            images: List<Int>,
            currentLevel: Int
        )

        fun showStartButton(questionIndex: Int)

        fun openGameScreen(questionIndex: Int)

        fun updateProgress(currentLevel: Int, totalCount: Int)

        fun showError(message: String)

        fun showFinishDialog()

        fun showRestartDialog()
        fun refreshScreen()
    }

    interface Presenter {

        fun loadData()

        fun onRegisterConfirmed(name: String)

        fun onQuestionClicked(position: Int)

        fun onStartConfirmed()

        fun onGameFinished(newLevel: Int)

        fun onRestartConfirmed()
    }

    interface Model {

        fun isRegistered(): Boolean

        fun registerUser(name: String)

        fun getUser(): UserData

        fun getQuestionImages(): List<Int>

        fun getTotalQuestionCount(): Int

        fun getCurrentLevel(): Int

        fun setLevel(level: Int)

        fun resetGame()
    }
}
