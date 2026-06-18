package com.example.pruebasubicacion.data.model

data class ClimaEstado (
    val clima: CalidadAire? = null,
    val estaCargando: Boolean = false,
    val mensajeError: String? = null
)
