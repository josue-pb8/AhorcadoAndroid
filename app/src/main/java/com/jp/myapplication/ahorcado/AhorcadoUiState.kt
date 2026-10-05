package com.jp.myapplication.ahorcado

data class AhorcadoUiState (
    val palabraSecreta: String = "",
    val palabraOculta: String = "",
    val letrasUsadas: String = "",
    val intentoRestante: Int = INTENTOS_INICIALES,
    val intentosTotales: Int = INTENTOS_INICIALES,
    val juegoTerminado: Boolean = false,
    val victoria: Boolean = false,
    val mensajeResultado: String = ""
) {
    companion object {
        const val INTENTOS_INICIALES = 6
    }
}
