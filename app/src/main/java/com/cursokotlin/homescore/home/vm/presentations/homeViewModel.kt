package com.cursokotlin.homescore.home.vm.presentations

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import kotlin.math.pow

data class ViviendaEstado(
    val precioVivienda:  String = "",
    val enganche : String = "",
    val ahorro: String = "",
    val porcentajeEnganche:String = "",
    val tasaInteres: String = "",
    val anios: Float = 15f,
    val ingreso: String = "",
    val gastos: String = "",
    val seguros: String = "",
    val mantenimiento: String = "",
    val predial: String = "",
    val servicios: String = "",
    val mostrarAlerta: Boolean = false,
    val tituloAlerta: String = "",
    val mensajeAlerta: String = "",
    val nivel: Int = 3,
    val progreso: Float = 0f,
    val textoProgreso: String = ""

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
                "ingreso" -> estadoActual.copy(ingreso = textoLimpio)
                "gastos" -> estadoActual.copy(gastos = textoLimpio)
                "seguros" -> estadoActual.copy(seguros = textoLimpio)
                "mantenimiento" -> estadoActual.copy(mantenimiento = textoLimpio)
                "predial" -> estadoActual.copy(predial = textoLimpio)
                "servicios" -> estadoActual.copy(servicios = textoLimpio)
                else -> estadoActual
            }

            val precio = estadoNuevo.precioVivienda.toDoubleOrNull() ?: 0.0
            val enganche = estadoNuevo.enganche.toDoubleOrNull() ?: 0.0
            val porcentaje = if (precio > 0 && enganche > 0) "(" + (enganche / precio * 100).toInt() + "%)" else ""
            estadoNuevo.copy(porcentajeEnganche = porcentaje)

        }
    }
    fun cambiarAnios(nuevoAnios: Float){
        _estado.update { it.copy(anios = nuevoAnios) }
    }

    fun alCerrarAlerta() {
        _estado.update { it.copy(mostrarAlerta = false) }
    }

    private fun dinero(cantidad: Double): String {
        val signo = if (cantidad < 0) "-$" else "$"
        return signo + String.format(Locale.US, "%,.0f", Math.abs(cantidad))
    }

    private fun mostrarError(mensaje: String) {
        _estado.update {
            it.copy(
                mostrarAlerta = true,
                tituloAlerta = "Revisa tus datos",
                mensajeAlerta = mensaje,
                nivel = 3
            )
        }
    }

    fun calcularAlPresionar() {
        val estadoActual = _estado.value

        // validar campos obligatorios
        if (
            estadoActual.precioVivienda.isBlank() ||
            estadoActual.enganche.isBlank() ||
            estadoActual.ahorro.isBlank() ||
            estadoActual.tasaInteres.isBlank() ||
            estadoActual.ingreso.isBlank()
        ) {
            mostrarError(
                "Completa todos los datos solicitados par apoder realizar el analisis"
            )
            return
        }

        //  Convertir datos
        val precio = estadoActual.precioVivienda.toDoubleOrNull()
        val enganche = estadoActual.enganche.toDoubleOrNull()
        val ahorro = estadoActual.ahorro.toDoubleOrNull()
        val tasaInteres = estadoActual.tasaInteres.toDoubleOrNull()
        val ingreso = estadoActual.ingreso.toDoubleOrNull()

        if (precio == null || enganche == null || ahorro == null ||
            tasaInteres == null || ingreso == null) {
            mostrarError("Revisa los datos . Solo se permiten valores numéricos.")
            return
        }

        // Validaciones
        if (precio <= 0) {
            mostrarError("El precio de la vivienda debe ser mayor que $0.")
            return
        }

        if (enganche <= 0 || enganche >= precio) {
            mostrarError("El enganche debe ser mayor que $0 y menor que el precio de la vivienda.")
            return
        }

        if (ahorro < 0) {
            mostrarError("El ahorro disponible no puede ser negativo.")
            return
        }

        if (tasaInteres < 0) {
            mostrarError("La tasa de interés no puede ser negativa.")
            return
        }

        if (ingreso <= 0) {
            mostrarError("El ingreso mensual debe ser mayor que $0.")
            return
        }

        // Datos opcionales
        val gastos = estadoActual.gastos.toDoubleOrNull() ?: 0.0
        val seguros = estadoActual.seguros.toDoubleOrNull() ?: 0.0
        val mantenimiento = estadoActual.mantenimiento.toDoubleOrNull() ?: 0.0
        val predial = estadoActual.predial.toDoubleOrNull() ?: 0.0
        val servicios = estadoActual.servicios.toDoubleOrNull() ?: 0.0

        if (gastos < 0 || seguros < 0 || mantenimiento < 0 ||
            predial < 0 || servicios < 0) {
            mostrarError("Los gastos no pueden tener valores negativos.")
            return
        }

        val otrosGastos = seguros + mantenimiento + predial + servicios

        // Crédito y mensualidad
        val credito = precio - enganche
        val meses = (estadoActual.anios * 12).toInt()
        val anios = estadoActual.anios.toInt()
        val tasaMensual = tasaInteres / 100 / 12

        /*
        Fórmula de la mensualidad:
        Mensualidad = (P x R x (1 + R)^N) / ((1 + R)^N - 1)

        P = Crédito
        R = Tasa mensual
        N = Número de meses
        */

        val mensualidad = if (tasaMensual == 0.0) {
            credito / meses
        } else {
            credito * tasaMensual / (1 - (1 + tasaMensual).pow(-meses))
        }

        // Costos del crédito
        val interesesTotales = mensualidad * meses - credito
        val costoTotal = enganche + mensualidad * meses

        //  Balance mensual
        val dineroRestante = ingreso - gastos - mensualidad - otrosGastos
        val porcentajeRestante = dineroRestante / ingreso * 100
        val porcentajeCasa = (mensualidad + otrosGastos) / ingreso * 100

        // Análisis
        var analisis = ""

        if (dineroRestante < 0) {
            analisis += "Tu ingreso actual no alcanza para cubrir tus gastos y los costos de la vivienda. " +
                    "Te faltarían " + dinero(-dineroRestante) + " cada mes.\n\n"
        }

        if (ahorro < enganche) {
            analisis += "Tu ahorro disponible no alcanza para cubrir el enganche. " +
                    "Te faltan " + dinero(enganche - ahorro) + ".\n\n"
        }

        if (analisis.isEmpty()) {
            analisis = "Tus ingresos y tu ahorro permiten cubrir los costos considerados para esta vivienda. " +
                    "Después de pagar tus gastos y la vivienda, te quedarían aproximadamente " +
                    dinero(dineroRestante) + " disponibles al mes."
        }

        // Indicador para la decisión financiera
        val nivel: Int
        val titulo: String
        val significado: String

        if (dineroRestante < 0 || ahorro < enganche) {
            nivel = 2
            titulo = "Riesgo financiero"
            significado = "Actualmente esta vivienda no se ajusta a tu situación financiera."
        } else if (porcentajeRestante < 20) {
            nivel = 1
            titulo = "Presupuesto ajustado"
            significado = "Puedes cubrir la vivienda, pero tendrás poco dinero disponible después de pagar tus gastos."
        } else {
            nivel = 0
            titulo = "Situación sostenible"
            significado = "Tus ingresos permiten cubrir la vivienda y conservar un margen para otros gastos."
        }



        //  Mensaje de la alerta
        val mensaje = significado +
                "\n\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\nANALISIS FINANCIERO" +
                "\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\n\n" + analisis +
                "\n"+
                "\n\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\nRESUMEN DE TU HIPOTECA" +
                "\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\n\nMensualidad: " + dinero(mensualidad) +
                "\nPlazo: " + anios + " años" +
                "\nCrédito: " + dinero(credito) +
                "\nEnganche: " + dinero(enganche) +
                "\n\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\nTU BALANCE MENSUAL" +
                "\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\n\nIngreso: " + dinero(ingreso) +
                "\nGastos actuales: " + dinero(gastos) +
                "\nVivienda: " + dinero(mensualidad + otrosGastos) +
                "\n\nDinero disponible después de pagar: " + dinero(dineroRestante) +
                "\n\nLa vivienda utiliza el " + porcentajeCasa.toInt() + "% de tu ingreso." +
                "\n\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\nCOSTO TOTAL DEL CRÉDITO" +
                "\n━━━━━━━━━━━━━━━━━━━━━━━" +
                "\n\nIntereses: " + dinero(interesesTotales) +
                "\nCosto total de la casa: " + dinero(costoTotal)

        // Guardar resultado
        _estado.update {
            it.copy(
                mostrarAlerta = true,
                tituloAlerta = titulo,
                mensajeAlerta = mensaje,
                nivel = nivel,
                progreso = (porcentajeCasa / 100).toFloat().coerceIn(0f, 1f),
                textoProgreso = "La vivienda utiliza el " + porcentajeCasa.toInt() + "% de tu ingreso"
            )
        }
    }
}