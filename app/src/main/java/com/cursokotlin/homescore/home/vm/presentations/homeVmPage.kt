package com.cursokotlin.homescore.home.vm.presentations

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cursokotlin.homescore.R
import java.nio.file.WatchEvent
import kotlin.compareTo

@Composable
fun viviendaPagina(viewModel: homeViewModel = viewModel()) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FA))
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 10.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF103E8A)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Home Score",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "¿Puedo comprar una casa?",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Analiza si se adapta a tu situación financiera.",
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                Image(
                    painter = painterResource(R.drawable.logocasa),
                    contentDescription = "Casa",
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informacion de la propiedad",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF103E8A)
            )

            TarjetaBlanca {
                CampoDinero(
                    etiqueta = "Precio de la vivienda",
                    valor = estado.precioVivienda,
                    alCambiar = { viewModel.cambiarTexto("precioVivienda", it) }
                )
            }

            TarjetaBlanca {
                CampoDinero(
                    etiqueta = "Enganche",
                    valor = estado.enganche,
                    alCambiar = { viewModel.cambiarTexto("enganche", it) }
                )
                if (estado.porcentajeEnganche.isNotEmpty()) {
                    Text(
                        text = estado.porcentajeEnganche,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 8.dp),
                        color = Color(0xFF103E8A),
                        fontWeight = FontWeight.Bold
                    )
                }
                CampoDinero(
                    etiqueta = "Ahorro disponible",
                    valor = estado.ahorro,
                    alCambiar = { viewModel.cambiarTexto("ahorro", it) }
                )
            }

            TarjetaBlanca {
                CampoDinero(
                    etiqueta = "Tasa de interés",
                    valor = estado.tasaInteres,
                    prefijo = "",
                    sufijo = "% anual",
                    alCambiar = { viewModel.cambiarTexto("tasaInteres", it) }
                )
            }

            TarjetaBlanca {
                Text(
                    text = "Plazo del crédito",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = estado.anios,
                    onValueChange = viewModel::cambiarAnios,
                    valueRange = 5f..30f,
                    steps = 24,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF0D3B85),
                        activeTrackColor = Color(0xFF0D3B85),
                        inactiveTrackColor = Color(0xFFE2E7F8),
                        activeTickColor = Color(0xFFFFFFFF),
                        inactiveTickColor = Color(0xFF0D3B85)
                    )
                )
                Text(
                    text = "${estado.anios.toInt()} años",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF103E8A),
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Tus Ingresos & Egresos Mensuales",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF103E8A),
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaCosto(
                    R.drawable.ingreso,
                    "Ingreso mensual",
                    estado.ingreso,
                    Modifier.weight(1f)
                ) {
                    viewModel.cambiarTexto("ingreso", it)
                }
                TarjetaCosto(
                    R.drawable.perdida,
                    "Gastos mensuales",
                    estado.gastos,
                    Modifier.weight(1f)
                ) {
                    viewModel.cambiarTexto("gastos", it)
                }
            }

            Text(
                text = "Gastos Mensuales de la Propiedad",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF103E8A),
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaCosto(
                    R.drawable.seguros,
                    "Gastos de seguros",
                    estado.seguros,
                    Modifier.weight(1f)
                ) {
                    viewModel.cambiarTexto("seguros", it)
                }
                TarjetaCosto(
                    R.drawable.mantenimiento,
                    "Mantenimiento",
                    estado.mantenimiento,
                    Modifier.weight(1f)
                ) {
                    viewModel.cambiarTexto("mantenimiento", it)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaCosto(
                    R.drawable.predial,
                    "Gastos de predial",
                    estado.predial,
                    Modifier.weight(1f)
                ) {
                    viewModel.cambiarTexto("predial", it)
                }
                TarjetaCosto(
                    R.drawable.servicio,
                    "Gastos de servicio",
                    estado.servicios,
                    Modifier.weight(1f)
                ) {
                    viewModel.cambiarTexto("servicios", it)
                }
            }
            Button(
                onClick = viewModel::calcularAlPresionar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF103E8A))
            ){
                Text(
                    text = "Realizar análisis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        // Alerta de resultado, en caso de que haya
        if (estado.mostrarAlerta) {
            val color = when (estado.nivel) {
                0 -> Color(0xFF2E7D32)
                1 -> Color(0xFFF9A825)
                2 -> Color(0xFFC62828)
                else -> Color.Gray
            }
            val iconoRes = when (estado.nivel) {
                0 -> R.drawable.exito
                1 -> R.drawable.advertencia
                2 -> R.drawable.error
                else -> R.drawable.alerta
            }

            AlertDialog(
                onDismissRequest = viewModel::alCerrarAlerta,
                containerColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                icon = {
                    Image(
                        painter = painterResource(id = iconoRes),
                        contentDescription = "Icono de estado",
                        modifier = Modifier.size(56.dp)
                    )
                },
                title = {
                    Text(
                        text = estado.tituloAlerta,
                        color = color,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        if (estado.nivel < 3) {
                            Text(
                                text = estado.textoProgreso,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { estado.progreso },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50)),
                                color = color
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                        Text(text = estado.mensajeAlerta, fontSize = 14.sp, color = Color.Black)
                    }
                },
                confirmButton = {
                    TextButton(onClick = viewModel::alCerrarAlerta) {
                        Text(text = "Entendido", color = color, fontWeight = FontWeight.Bold)
                    }
                }
            )
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
    prefijo: String = "$",
    sufijo: String = "",
    alCambiar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        label = { Text(text = etiqueta, fontSize = 12.sp, maxLines = 1) },
        prefix = { Text(text = prefijo, color = Color.Black, fontSize = 12.sp) },
        suffix = { Text(text = sufijo, color = Color.Black, fontSize = 12.sp) },
        singleLine = true,
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal
        )
    )
}

@Composable
fun TarjetaCosto(
    @DrawableRes iconoRes: Int,
    etiqueta: String,
    valor: String,
    modifier: Modifier = Modifier,
    alCambiar: (String) -> Unit
) {
    TarjetaBlanca(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFE3EAFB), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconoRes),
                contentDescription = etiqueta,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        CampoDinero(etiqueta = etiqueta, valor = valor, alCambiar = alCambiar)
    }
}