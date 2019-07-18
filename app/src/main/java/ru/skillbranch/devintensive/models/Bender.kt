package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        val (isValidate, warning) = validate(answer)

        if (!isValidate) {
            return "$warning\n${question.question}" to status.color
        }
        return if (question == Question.IDLE) {
            "На этом все, вопросов больше нет" to Status.NORMAL.color
        } else if (question.answer.contains(answer)) {
            if (question == Question.IDLE) {
                question = question.nextQuestion()
                "Отлично - ты справился\nНа этом все, вопросов больше нет" to Status.NORMAL.color
            } else {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            }
        } else {
            if (status == Status.CRITICAL) {
                status = Status.NORMAL
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            } else {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
        }
    }

    private fun validate(answer: String): Pair<Boolean, String> {
        return when (question) {
            Question.NAME -> if (answer[0]?.isLowerCase()) false to "Имя должно начинаться с заглавной буквы" else true to ""
            Question.PROFESSION -> if (answer[0]?.isUpperCase()) false to "Профессия должна начинаться со строчной буквы" else true to ""
            Question.MATERIAL -> if (answer.contains("\\d".toRegex())) false to "Материал не должен содержать цифр" else true to ""
            Question.BDAY -> if (!answer.contains("^[0-9]+\$".toRegex())) false to "Год моего рождения должен содержать только цифры" else true to ""
            Question.SERIAL -> if (!answer.contains("^\\d{7}$".toRegex())) false to "Серийный номер содержит только цифры, и их 7" else true to ""
            else -> true to ""
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        abstract fun nextQuestion(): Question
    }
}