package com.example.pruebasubicacion

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.pruebasubicacion.view.UbicacionView
import com.example.pruebasubicacion.viewmodel.UbicacionViewModel
import com.google.android.gms.location.LocationServices
import kotlin.math.round

class MainActivity : ComponentActivity() {

    private val vistaModelo: UbicacionViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            obtenerUbicacion()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            UbicacionView(
                estado = vistaModelo.state,
                onFetchLocation = { checkPermissionsAndGetLocation() }
            )
        }
    }

    private fun checkPermissionsAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        } else {
            obtenerUbicacion()
        }
    }

    private fun obtenerUbicacion() {
        val fusedClient = LocationServices.getFusedLocationProviderClient(this)
        
        try {
            fusedClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = round(location.latitude * 100) / 100
                    val lon = round(location.longitude * 100) / 100
                    // Cargamos los datos en el ViewModel
                    vistaModelo.cargarClima(lat, lon)
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación. Verifica tu GPS.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Error de permisos: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
