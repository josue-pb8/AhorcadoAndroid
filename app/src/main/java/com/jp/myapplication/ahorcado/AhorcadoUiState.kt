package com.jp.myapplication.ahorcado

class AhorcadoUiState (
    val palabraSecreta: String = "",
    val palabraOculta: String = "",
    val letrasUsadas: String = "",
    val intentoRestante: Int = 6,
    val juegoTerminado: Boolean = false,
    val mensajeResultado: String = ""
)