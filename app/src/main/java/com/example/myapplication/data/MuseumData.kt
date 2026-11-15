package com.example.myapplication.data

import com.example.myapplication.model.Category
import com.example.myapplication.model.Horario

object MuseumData {
    val categories = listOf(
        Category("🎨", "Pintura"),
        Category("🗿", "Escultura"),
        Category("🏺", "Arqueología"),
        Category("🎭", "Cultura popular")
    )

    val schedules = listOf(
        Horario("9:00 a. m.", 12),
        Horario("10:30 a. m.", 8),
        Horario("12:00 m.", 0),
        Horario("2:00 p. m.", 15),
        Horario("3:30 p. m.", 6),
        Horario("5:00 p. m.", 10)
    )
}
