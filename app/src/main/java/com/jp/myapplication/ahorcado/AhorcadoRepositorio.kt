package com.jp.myapplication.ahorcado

open class AhorcadoRepositorio {
    private val diccionarioPalabras = listOf("ANDROID", "KOTLIN", "APLICACION")
    open fun palabraRandom (): String {
        return diccionarioPalabras.random()
    }
}