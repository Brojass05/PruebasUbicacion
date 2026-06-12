package com.example.pruebasubicacion.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pruebasubicacion.model.ClimaEstado
import com.example.pruebasubicacion.model.ServicioAPI
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
 import com.example.pruebasubicacion.util.Log
import kotlinx.coroutines.launch

class UbicacionViewModel: ViewModel() {
    var state by mutableStateOf(ClimaEstado())
        private set

    fun cargarClima(latitud: Double, longitud: Double) {
        viewModelScope.launch {
            state = state.copy(estaCargando = true, mensajeError = null)
            try {
                val servicioAPI = ServicioAPI.getInstance()
                val datosClima = servicioAPI.getPm2_5(latitud, longitud)
                
                state = state.copy(
                    estaCargando = false,
                    clima = datosClima
                )
            } catch (e: Exception) {
                Log(mensaje = "Error al cargar el Clima: " + e.message)
                state = state.copy(
                    estaCargando = false,
                    mensajeError = "Error al obtener datos: ${e.message}"
                )
            }
        }
    }
}
