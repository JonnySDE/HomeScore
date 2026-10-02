package com.cursokotlin.homescore.home.vm.presentations

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class viviendaEstado(
    val precioVivienda:  String = "",
    val enganche : String = ""

)

class homeViewModel : ViewModel() {
    private val _estado = MutableStateFlow(viviendaEstado())
    val estado: StateFlow<viviendaEstado> = _estado.asStateFlow()

    fun cambiarTexto(campo: String, texto: String) {
        val textoLimpio = texto.filter { it.isDigit() || it == '.'}

        _estado.update {
            when (campo) {
                "precioVivienda" -> it.copy(precioVivienda = textoLimpio)
                "enganche" -> it.copy(enganche = textoLimpio)
                else -> it
            }
        }
    }
}