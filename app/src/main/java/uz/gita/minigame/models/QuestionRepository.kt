package uz.gita.minigame.models

import uz.gita.minigame.R
import uz.gita.minigame.models.data.QuestionData

object QuestionRepository {
    val questions = listOf(
        QuestionData(R.drawable.img_dog, "dog","gdmofaq"),
        QuestionData(R.drawable.img_cat, "cat","tacbjen"),
        QuestionData(R.drawable.img_cow, "cow", "wocylmz"),
        QuestionData(R.drawable.img_tiger, "tiger", "getirqa"),
        QuestionData(R.drawable.img_lion, "lion", "nloivbx"),
        QuestionData(R.drawable.img_3, "pig", "gipztra"),


    )

    fun getByIndexQuestion(index: Int): QuestionData = questions.get(index)
    fun getImages(): List<Int> = questions.map { it.image }
}
