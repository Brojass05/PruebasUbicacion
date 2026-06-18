package com.example.pruebasubicacion.model

data class ClimaEstado (
    val clima: CalidadAire? = null,
    val estaCargando: Boolean = false,
    val mensajeError: String? = null
)
