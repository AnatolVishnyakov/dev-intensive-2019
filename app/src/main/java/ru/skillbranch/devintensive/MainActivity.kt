package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    lateinit var benderObj: Bender

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        benderImage = findViewById(R.id.iv_bender)
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name

        benderObj = Bender(
            status = Bender.Status.valueOf(status),
            question = Bender.Question.valueOf(question)
        )

        Log.d("M_MainActivity", "onCreate $status $question")
        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
            messageEt.setText("")

            val (r, g, b) = color
            benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
        }
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
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")

        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
    }
}
