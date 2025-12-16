package uz.gita.minigame.ui.game

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.gita.minigame.R
import uz.gita.minigame.ui.main.MainActivity

class GameActivity : AppCompatActivity(), GameContract.View {

    private lateinit var presenter: GameContract.Presenter

    private lateinit var txtLevel: TextView
    private lateinit var imgQuestion: ImageView
    private lateinit var btnBack: ImageButton
    private lateinit var btnFinish: AppCompatButton

    private lateinit var containerAnswers: LinearLayout
    private lateinit var containerVariants1: LinearLayout
    private lateinit var containerVariants2: LinearLayout
    private lateinit var containerVariants3: LinearLayout

    private val variantViews = mutableListOf<FrameLayout>()

    private var questionIndex: Int = 0

    companion object {
        private const val TAG = "GameActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


            enableEdgeToEdge()
            setContentView(R.layout.activity_game)

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            initViews()

            presenter = GamePresenter(this, GameModel(this))

            questionIndex = intent.getIntExtra("QUESTION_INDEX", 0)

            presenter.loadQuestionByLevel(questionIndex)

            btnBack.setOnClickListener {
                presenter.onClickBack()
            }

            btnFinish.setOnClickListener {
                presenter.onClickFinish()
            }

        }


    private fun initViews() {

            txtLevel = findViewById(R.id.tv_level)
            imgQuestion = findViewById(R.id.img_quiz)
            btnBack = findViewById(R.id.btn_back)
            btnFinish = findViewById(R.id.btn_finish)

            containerAnswers = findViewById(R.id.container_answer)
            containerVariants1 = findViewById(R.id.container_variant_1)
            containerVariants2 = findViewById(R.id.container_variant_2)
            containerVariants3 = findViewById(R.id.container_variant_3)


    }

    override fun setLevel(level: Int) {
        txtLevel.text = "Level ${level + 1}"
    }

    override fun setQuestion(image: Int) {
        imgQuestion.setImageResource(image)
    }

    override fun setVariant(index: Int, value: String) {
            containerVariants1.removeAllViews()
            containerVariants2.removeAllViews()
            containerVariants3.removeAllViews()
            variantViews.clear()

            for (i in value.indices) {
                val view: FrameLayout = LayoutInflater.from(this)
                    .inflate(R.layout.item_btn_variant, containerVariants1, false) as FrameLayout

                val btnVariant = view.findViewById<AppCompatButton>(R.id.img_bg_variant)
                btnVariant.text = value[i].toString()

                val variantIndex = i
                btnVariant.setOnClickListener {
                    presenter.onClickVariant(variantIndex, value[variantIndex].toString())
                }

                variantViews.add(view)
            }

            val rowSize1 = minOf(5, value.length)
            val rowSize2 = minOf(4, maxOf(0, value.length - 5))
            val rowSize3 = maxOf(0, value.length - 9)


            for (i in 0 until rowSize1) {
                containerVariants1.addView(variantViews[i])
            }
            for (i in 0 until rowSize2) {
                containerVariants2.addView(variantViews[5 + i])
            }
            for (i in 0 until rowSize3) {
                containerVariants3.addView(variantViews[9 + i])
            }

    }

    override fun setAnswer(index: Int, value: String) {
        if (index >= containerAnswers.childCount) {
            return
        }
        val view: LinearLayout = containerAnswers.getChildAt(index) as LinearLayout
        val tvAnswer = view.findViewById<AppCompatTextView>(R.id.tv_answer)
        tvAnswer.text = value
    }

    override fun createAnswers(count: Int) {
        containerAnswers.removeAllViews()

        for (index in 0 until count) {
            val view = layoutInflater.inflate(
                R.layout.item_btn_answer,
                containerAnswers,
                false
            ) as LinearLayout

            view.setOnClickListener {
                presenter.onClickAnswer(index)
            }

            containerAnswers.addView(view)
        }

    }


    override fun answerVisible(index: Int) {
        if (index >= containerAnswers.childCount) return
        containerAnswers.getChildAt(index).visibility = View.VISIBLE
    }

    override fun answerGone(index: Int) {
        if (index >= containerAnswers.childCount) return
        containerAnswers.getChildAt(index).visibility = View.GONE
    }

    override fun variantVisible(index: Int) {
        if (index >= variantViews.size) return
        variantViews[index].visibility = View.VISIBLE
    }

    override fun variantInvisible(index: Int) {
        if (index >= variantViews.size) return
        variantViews[index].visibility = View.INVISIBLE
    }

    override fun showCorrectDialog() {
        val dialogView = layoutInflater.inflate(R.layout.item_dialog_correct, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnNext = dialogView.findViewById<AppCompatButton>(R.id.btn_next)

        btnNext.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("newLevel", questionIndex)
            startActivity(intent)
            finish()
            presenter.onNextClicked()
        }

        dialog.show()
    }


    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun resetBackgroun() {
        for (i in 0 until containerAnswers.childCount) {
            val view = containerAnswers.getChildAt(i) as LinearLayout
            val tvAnswer = view.findViewById<AppCompatTextView>(R.id.tv_answer)
            tvAnswer.setBackgroundResource(R.drawable.bg_empty_btn)
            tvAnswer.text = ""
        }
        for (v in variantViews) {
            v.visibility = View.VISIBLE
        }
    }

    override fun showWrongAnswer() {
        for (i in 0 until containerAnswers.childCount) {
            val view = containerAnswers.getChildAt(i) as LinearLayout
            val tvAnswer = view.findViewById<AppCompatTextView>(R.id.tv_answer)
            if (tvAnswer.text.isNotEmpty()) {
                tvAnswer.setBackgroundResource(R.drawable.bg_enabled_btn)
            }
        }
    }

    override fun navigateMain(isSuccess: Boolean) {

        if (isSuccess) {
            setResult(RESULT_OK)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    override fun resetAnswerBackgrounds() {
        for (i in 0 until containerAnswers.childCount) {
            val view = containerAnswers.getChildAt(i) as LinearLayout
            val tvAnswer = view.findViewById<AppCompatTextView>(R.id.tv_answer)
            tvAnswer.setBackgroundResource(R.drawable.bg_empty_btn)
        }
    }
}