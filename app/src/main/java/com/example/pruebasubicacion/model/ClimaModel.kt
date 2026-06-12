package com.example.pruebasubicacion.model

import java.util.Date

/**
 * Modelo de datos que representa una Factura.
 *
 * @property latitud Identificador único de la factura.
 * @property longitud Nombre del cliente asociado a la factura.
 * @property hourly Fecha de emisión de la factura.
 * @property pm2_5 Monto total de la factura.
 */

data class ClimaModel(
    val latitude: Double,
    val longitude: Double,
    val hourly: HourlyData
)

data class HourlyData(
    val time: List<String>, // La API devuelve strings con formato ISO8601
    val pm2_5: List<Float>
)