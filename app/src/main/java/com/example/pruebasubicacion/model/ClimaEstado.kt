package com.example.pruebasubicacion.model

data class ClimaEstado (
    val clima: ClimaModel? = null,
    val estaCargando: Boolean = false,
    val mensajeError: String? = null
)
