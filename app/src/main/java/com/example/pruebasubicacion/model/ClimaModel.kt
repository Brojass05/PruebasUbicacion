package com.example.pruebasubicacion.model



/**
 * Modelo de datos que representa una Factura.
 *
 * @property latitud Latitud del usuario.
 * @property longitud Longitud del usuario.
 * @property hourly Datos por hora.
 * @property pm2_5 Tipo de dato a obtener.
 * @property forecast_days Dias en adelante para obtener.
 */

data class ClimaModel(
    val latitude: Double,
    val longitude: Double,
    val hourly: HourlyData,
    val forecast_days: Int
)

data class HourlyData(
    val time: List<String>, // La API devuelve strings con formato ISO8601
    val pm2_5: List<Float>
)