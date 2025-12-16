package uz.gita.minigame.ui.main


class MainPresenter(
    private val view: MainContract.View,
    private val model: MainContract.Model
) : MainContract.Presenter {

    private var selectedQuestionIndex = -1

    override fun loadData() {

        try {
            if (!model.isRegistered()) {
                view.showRegisterDialog()
                return
            }

            val user = model.getUser()
            val images = model.getQuestionImages()
            val totalQuestions = model.getTotalQuestionCount()



            view.showUserInfo(user)
            view.showQuestionImages(images, user.level)
            view.updateProgress(user.level, totalQuestions)

            if (user.level >= totalQuestions) {
                view.showFinishDialog()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("Ma'lumotlarni yuklashda xatolik: ${e.message}")
        }
    }

    override fun onRegisterConfirmed(name: String) {

        try {
            if (name.isBlank()) {
                view.showError("Ism bo'sh bo'lishi mumkin emas!")
                view.showRegisterDialog()
                return
            }

            model.registerUser(name)

            loadData()

        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("Ro'yxatdan o'tishda xatolik: ${e.message}")
        }
    }

    override fun onQuestionClicked(position: Int) {

        try {
            val currentLevel = model.getCurrentLevel()

            if (position > currentLevel) {
                view.showError("Avval oldingi savolni yeching!")
                return
            }

            selectedQuestionIndex = position

            view.showStartButton(position)

        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("Savolni tanlashda xatolik: ${e.message}")
        }
    }

    override fun onStartConfirmed() {

        try {
            if (selectedQuestionIndex == -1) {
                view.showError("Avval savolni tanlang!")
                return
            }

            view.openGameScreen(selectedQuestionIndex)


        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("O'yinni boshlashda xatolik: ${e.message}")
        }
    }

    override fun onGameFinished(newLevel: Int) {

        try {
            model.setLevel(newLevel)
            view.updateProgress(newLevel, model.getTotalQuestionCount())

            if (newLevel >= model.getTotalQuestionCount()) {
                view.showFinishDialog()
            } else {
                view.refreshScreen()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("O'yinni tugatishda xatolik: ${e.message}")
        }
    }

    override fun onRestartConfirmed() {

        try {
            model.resetGame()
            selectedQuestionIndex = -1

            loadData()

        } catch (e: Exception) {
            e.printStackTrace()
            view.showError("O'yinni qayta boshlashda xatolik: ${e.message}")
        }
    }
}