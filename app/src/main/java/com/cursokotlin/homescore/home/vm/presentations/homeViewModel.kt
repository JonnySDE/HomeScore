package com.cursokotlin.homescore.home.vm.presentations

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ViviendaEstado(
    val precioVivienda:  String = "",
    val enganche : String = "",
    val ahorro: String = "",
    val porcentajeEnganche:String = "",
    val tasaInteres: String = "",
    val anios: Float = 15f

)

class homeViewModel : ViewModel() {
    private val _estado = MutableStateFlow(ViviendaEstado())
    val estado: StateFlow<ViviendaEstado> = _estado.asStateFlow()

    fun cambiarTexto(campo: String, texto: String) {
        val textoLimpio = texto.filter { it.isDigit() || it == '.'}

        _estado.update { estadoActual ->
            val estadoNuevo = when (campo) {
                "precioVivienda" -> estadoActual.copy(precioVivienda = textoLimpio)
                "enganche" -> estadoActual.copy(enganche = textoLimpio)
                "ahorro" -> estadoActual.copy(ahorro = textoLimpio)
                "tasaInteres" -> estadoActual.copy(tasaInteres = textoLimpio)
                else -> estadoActual
            }

            // Porcentaje del enganche: solo si hay precio y enganche
            val precio = estadoNuevo.precioVivienda.toDoubleOrNull() ?: 0.0
            val enganche = estadoNuevo.enganche.toDoubleOrNull() ?: 0.0
            val porcentaje = if (precio > 0 && enganche > 0) "(" + (enganche / precio * 100).toInt() + "%)" else ""
            estadoNuevo.copy(porcentajeEnganche = porcentaje)

        }
    }
    fun cambiarAnios(nuevoAnios: Float){
        _estado.update { it.copy(anios = nuevoAnios) }
    }
}