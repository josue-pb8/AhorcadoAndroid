package com.jp.myapplication.ahorcado

class AhorcadoRepositorio {
    private val diccionarioPalabras = listOf("ANDROID", "KOTLIN", "APLICACION")
    fun palabraRandom (): String {
        return diccionarioPalabras.random()
    }
}