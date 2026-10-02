package com.cursokotlin.homescore.home.vm.presentations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import java.nio.file.WatchEvent

@Composable
fun viviendaPagina(viewModel: homeViewModel = viewModel()) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FA))
    ) {
        Text(
            text = "Home Score : ¿Puedo comprar una vivienda",
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF103E8A), RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .statusBarsPadding()
                .padding(24.dp),

            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Column(modifier =  Modifier.padding(18.dp)){
            Text(
                text = "INFORMACION DE LA PROPIEDAD",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color(0xFF103E8A)
            )

            TarjetaBlanca {
                CampoDinero(
                    etiqueta = "Precio de la vivienda",
                    estado.precioVivienda,
                ) {
                    viewModel.cambiarTexto("precioVivienda", it)
                }
            }
            TarjetaBlanca {
                CampoDinero(
                    etiqueta = "Enganche",
                    estado.enganche,
                ) {
                    viewModel.cambiarTexto("enganche", it)
                }
                Text(
                    text = estado.porcentajeEnganche,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF103E8A),
                    fontWeight = FontWeight.Bold
                )
                CampoDinero(
                    "Ahorro disponible",
                    estado.ahorro
                ){
                    viewModel.cambiarTexto("ahorro", it)
                }
            }
        }
    }
}

@Composable
fun TarjetaBlanca(
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            contenido()
        }
    }
}

@Composable
fun CampoDinero(
    etiqueta: String,
    valor: String,
    alCambiar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}

