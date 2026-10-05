package com.jp.myapplication.ahorcado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AhorcadoViewModel(private val repositorio: AhorcadoRepositorio) : ViewModel() {
    private val _uiState = MutableStateFlow(AhorcadoUiState())
    val uiState = _uiState.asStateFlow()
    init {
        reiniciarJuego()
    }
    fun reiniciarJuego(){
        val palabra = repositorio.palabraRandom()
        _uiState.value = AhorcadoUiState(
            palabraSecreta = palabra,
            palabraOculta = enmascarar(palabra, ""),
            letrasUsadas = "",
            intentoRestante = AhorcadoUiState.INTENTOS_INICIALES,
            intentosTotales = AhorcadoUiState.INTENTOS_INICIALES,
            juegoTerminado = false,
            victoria = false,
            mensajeResultado = ""
        )
    }
    fun pulsarLetra(letra: Char){
        val estadoActual = _uiState.value
        if (estadoActual.juegoTerminado || letra in estadoActual.letrasUsadas)
            return

        val letrasNuevas = estadoActual.letrasUsadas + letra
        val letraCorrecta = estadoActual.palabraSecreta.contains(letra)
        val intentosNuevos = if (letraCorrecta) estadoActual.intentoRestante else estadoActual.intentoRestante -1

        val nuevaPalabraOculta = enmascarar(estadoActual.palabraSecreta, letrasNuevas)
        val siGano = !nuevaPalabraOculta.contains("_")
        val siPerdio = intentosNuevos <= 0
        val finish = siGano || siPerdio

        val mensaje = when {
            siGano -> "Has adivinado la palabra, ¡FELICIDADES!"
            siPerdio -> "Has perdido el juego. La palabra era: ${estadoActual.palabraSecreta}"
            letraCorrecta -> "Correcto, la letra $letra está en la palabra"
            else -> "La letra $letra no está en la palabra"
        }

        _uiState.value = estadoActual.copy(
            palabraOculta = if (siPerdio) revelar(estadoActual.palabraSecreta) else nuevaPalabraOculta,
            letrasUsadas = letrasNuevas,
            intentoRestante = intentosNuevos,
            juegoTerminado = finish,
            victoria = siGano,
            mensajeResultado = mensaje
        )
    }

    /**
     * Unifica el formato de la palabra oculta: cada letra ocupa el mismo
     * ancho, revelandola como "X " y oculta como "_ ".
     */
    private fun enmascarar(palabra: String, letrasUsadas: String): String {
        return palabra.map { char ->
            if (char in letrasUsadas) "$char " else "_ "
        }.joinToString("")
    }

    /** Muestra la palabra completa, usado cuando el jugador se queda sin intentos. */
    private fun revelar(palabra: String): String {
        return palabra.map { "$it " }.joinToString("")
    }

    companion object {
        /** Unico punto donde se decide que dependencia se inyecta en la app. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { AhorcadoViewModel(AhorcadoRepositorio()) }
        }
    }
}