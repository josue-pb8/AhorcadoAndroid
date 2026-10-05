package com.jp.myapplication.ahorcado

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AhorcadoViewModel(private val repositorio: AhorcadoRepositorio = AhorcadoRepositorio()) {
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
            letrasUsadas = " ",
            juegoTerminado = false,
            mensajeResultado = " "
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

        var mensaje = ""
        if (siGano){
            mensaje = "Has adivinado la palabra, ¡FELICIDADES!"
        }else if (siPerdio){
            mensaje = "Has perdido el juego. La palabra era: ${estadoActual.palabraSecreta}"
        }

        _uiState.value = estadoActual.copy(
            palabraOculta = nuevaPalabraOculta,
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

}