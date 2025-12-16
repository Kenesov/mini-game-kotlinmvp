package uz.gita.minigame.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.gita.minigame.R
import uz.gita.minigame.models.data.UserData
import uz.gita.minigame.ui.game.GameActivity
import android.app.Activity

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter
    private lateinit var txtUserName: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRestart: ImageView
    private var selectedIndex: Int = -1

    private lateinit var levelButtons: List<ImageButton>

    private val gameResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedIndex = -1
            refreshScreen()
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        initPresenter()
        showListenerGame()

    }
    override fun onResume() {
        super.onResume()
        refreshScreen()
    }

    private fun initView() {
        txtUserName = findViewById(R.id.txt_user_name)
        progressBar = findViewById(R.id.progress_vertical)
        btnRestart = findViewById(R.id.btn_restart)

        levelButtons = listOf(
            findViewById(R.id.level1),
            findViewById(R.id.level2),
            findViewById(R.id.level3),
            findViewById(R.id.level4),
            findViewById(R.id.level5),
            findViewById(R.id.level6)
        )
        levelButtons.forEachIndexed { index, button ->
            button.setOnClickListener { presenter.onQuestionClicked(index) }
        }

        btnRestart.setOnClickListener { showRestartDialog() }
    }

    private fun initPresenter() {
        presenter = MainPresenter(this, MainModel(this))
        presenter.loadData()
    }

    private fun showListenerGame(){
        selectedIndex = intent.getIntExtra("newLevel",0)
    }

    override fun showRegisterDialog() {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.item_dialog_register, null)
        val etName = view.findViewById<EditText>(R.id.et_Name)
        val btnSave = view.findViewById<AppCompatButton>(R.id.btn_save)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            presenter.onRegisterConfirmed(name)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun showUserInfo(user: UserData) {
        txtUserName.text = user.name
    }

    override fun showQuestionImages(images: List<Int>, currentLevel: Int) {
        levelButtons.forEachIndexed { index, button ->
            val startButton = when (index) {
                0 -> findViewById<AppCompatButton>(R.id.btn_1)
                1 -> findViewById<AppCompatButton>(R.id.btn_2)
                2 -> findViewById<AppCompatButton>(R.id.btn_3)
                3 -> findViewById<AppCompatButton>(R.id.btn_4)
                4 -> findViewById<AppCompatButton>(R.id.btn_5)
                5 -> findViewById<AppCompatButton>(R.id.btn_6)
                else -> null
            }

            if (index <= currentLevel) {
                button.setImageResource(images[index])
                button.isEnabled = true
                button.alpha = 1f

                startButton?.visibility =
                    if (index == selectedIndex) AppCompatButton.VISIBLE else AppCompatButton.GONE

                button.setOnClickListener {
                    selectedIndex = index
                    levelButtons.forEachIndexed { i, _ ->
                        val btn = when (i) {
                            0 -> findViewById<AppCompatButton>(R.id.btn_1)
                            1 -> findViewById<AppCompatButton>(R.id.btn_2)
                            2 -> findViewById<AppCompatButton>(R.id.btn_3)
                            3 -> findViewById<AppCompatButton>(R.id.btn_4)
                            4 -> findViewById<AppCompatButton>(R.id.btn_5)
                            5 -> findViewById<AppCompatButton>(R.id.btn_6)
                            else -> null
                        }
                        btn?.visibility =
                            if (i == index) AppCompatButton.VISIBLE else AppCompatButton.GONE
                    }
                }

                startButton?.setOnClickListener {
                    presenter.onQuestionClicked(index)
                    presenter.onStartConfirmed()
                }

            } else {
                button.setImageResource(R.drawable.level_icon_locked)
                button.isEnabled = false
                startButton?.visibility = AppCompatButton.GONE
            }
        }
    }

    override fun showStartButton(questionIndex: Int) {
        presenter.onStartConfirmed()
    }

    override fun openGameScreen(questionIndex: Int) {

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("QUESTION_INDEX", questionIndex)

             gameResultLauncher.launch(intent)
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showFinishDialog() {
        val dialogView = layoutInflater.inflate(R.layout.item_finish, null)
        val btnRefresh = dialogView.findViewById<ImageButton>(R.id.btn_reflesh)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnRefresh.setOnClickListener {
            dialog.dismiss()
            presenter.onRestartConfirmed()
        }

        dialog.show()
    }

    override fun showRestartDialog() {
        val dialogView = layoutInflater.inflate(R.layout.item_dialog_restart, null)
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        btnYes.setOnClickListener {
            dialog.dismiss()
            presenter.onRestartConfirmed()
        }

        dialog.show()
    }

    override fun refreshScreen() {
        presenter.loadData()
    }

    override fun updateProgress(correctCount: Int, totalCount: Int) {
        progressBar.max = totalCount
        progressBar.progress = correctCount
    }


}

