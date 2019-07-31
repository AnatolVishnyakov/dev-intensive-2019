package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    /**
     * Вызывается при первом создании или перезапуске Activity.
     *
     * Здесь задаётся внешний вид активности (UI) через метод setContentView().
     * - инициализируются представления;
     * - представления связываются с необходимыми данными и ресурсами;
     * - связываются данные со списками.
     *
     * Этот метод также представляет Bundle, содержащий ранее сохраненное
     * состояние Activity, если оно было.
     *
     * Всегда сопровождается вызовом onStart().
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO set custom Theme this before super and setContentView
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d("M_ProfileActivity", "onCreate")
    }

    /**
     * Если Activity возвращается в приоритетный режим после вызова onStop(),
     * то в этом случае вызывается onRestart(), т.е. вызывается после того,
     * как Activity была остановлена и снова была запущена пользователем.
     *
     * Всегда сопровождается вызовом метода onStart().
     * Используется для специльных действий, которые должны выполняться
     * только при повторном запуске Activity.
     * */
    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    /**
     * При вызове onStart() окно ещё не видно пользователю, но вскоре будет видно.
     * Вызывается непосредственно перед тем, как активность становится видной пользователю.
     *
     * - Чтение из базы данных.
     * - Запуск сложной анимации.
     * - Запуск потоков, отслеживание показаний датчиков, запросов к GPS, таймеров,
     * сервисов или других процессов, которые нужны исключительно для обновления
     * пользовательского интерфейса.
     *
     * Затем следует onResume(), если Activity выходит на передний план.
     * */
    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    /**
     * Вызывается, когда Activity начнет взаимодействовать с пользователем.
     *
     * - Запуск воспроизведения анимации, аудио/видео.
     * - Регистрация любых BroadcastReceiver или других процессов, которые
     * вы освободили/приостановили в onPause().
     * - Выполнение любых других инициализаций, которые должны происходить,
     * когда Activity вновь активна (камера).
     *
     * Тут должен быть максимально легкий и быстрый код, чтобы приложение
     * оставалось отзывчивым.
     * */
    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    /**
     * Метод onPause() вызывается после сворачивания текущей активности
     * или перехода к новому.
     * От onPause() можно перейти к вызову либо onResume(), либо onStop().
     *
     * - Остановка анимации, аудио/видео.
     * - Сохранение состояния пользовательского ввода (легкие процессы).
     * - Сохранение в DB, если данные должны быть доступны в новой Activity.
     * - Остановка сервисов, подписок, BroadcastReceiver.
     *
     * Тут должен быть максимально легкий и быстрый код, чтобы приложение
     * оставалось отзывчивым.
     * */
    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "onPause")
    }

    /**
     * Метод onStop() вызывается, когда Activity становится невидимым
     * для пользователя.
     * Это может произойти при её уничтожении, или если была запущена
     * другая Activity (существующая или новая), перекрывшая окно
     * текущей Activity.
     *
     * - Запись в базу данных.
     * - Приостановка сложной анимации.
     * - Приостановка потоков, отслеживание показаний датчиков, запросов
     * к GPS, таймеров, сервисов или других процессов, которые нужны
     * исключительно для обновления пользовательского интерфейса.
     *
     * Не вызывается при вызове метода Activity.finish().
     * */
    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    /**
     * Метод вызывается по окончании работы Activity, при вызове
     * метода finish() или в случае, когда система уничтожает этот
     * экземпляр активности для освобождения ресурса.
     * */
    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    /**
     * Этот метод сохраняет состояние представления в Bundle.
     * Для API Level < 28 (Android P), этот метод будет выполняться
     * до onStop(), и нет никаких гарантий относительно того,
     * произойдет ли это до или после onPause().
     *
     * Для API Level > 28 будет вызван после onStop().
     * Не будет вызван, если Activity будет явно закрыто пользователем,
     * при нажатии на системную клавишу back.
     * */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // сохранение режима редактирования
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "updateTheme")
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (isEditMode) {
                saveProfileInfo()
            }
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
    }

    private fun saveProfileInfo() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit // подсвечивает кол-во вводимых символов

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN // эффект/режим наложения
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }
}