package com.jp.myapplication.ahorcado
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jp.myapplication.ui.theme.ColorExito


@Composable
fun AhorcadoPage (viewModel: AhorcadoViewModel = viewModel()){
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "ADIVINA LA PALABRA", fontSize = 25.sp)

        Spacer(modifier = Modifier.height(15.dp))

        Text(text = "Intentos Restantes: ${state.intentoRestante}/${state.intentosTotales}", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = state.palabraOculta,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 8.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (state.mensajeResultado.isNotEmpty()) {
            Text(
                text = state.mensajeResultado,
                fontSize = 18.sp,
                color = when {
                    state.juegoTerminado && state.victoria -> ColorExito
                    state.juegoTerminado -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        val abecedario = ('A'..'Z').toList()
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 7
        ) {
            abecedario.forEach { letra ->
                val yaUsada = letra in state.letrasUsadas
                Button(
                    onClick = { viewModel.pulsarLetra(letra)},
                    enabled = !yaUsada && !state.juegoTerminado,
                    modifier = Modifier
                        .padding(3.dp)
                        .size(width = 40.dp, height = 40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = letra.toString(),
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(23.dp))
        Button(onClick = {viewModel.reiniciarJuego()}) {
            Text(text = "Reiniciar Juego")
        }
    }
}

