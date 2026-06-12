package com.example.pruebasubicacion.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.example.pruebasubicacion.MainActivity

class UbicacionViewModel {
    // 1. Manejador para pedir permisos en tiempo de ejecución
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permiso concedido
            getLastKnownLocation()
        } else {
            // Permiso denegado
            Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
        }
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private fun checkPermissionsAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // 3. Si no hay permisos, los pedimos
            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        } else {
            // 4. Si ya hay permisos, obtenemos la ubicación
            getLastKnownLocation()
        }
    }

    private fun getLastKnownLocation() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val msg = "Lat: ${location.latitude}, Lon: ${location.longitude}"
                        Log.d("LOCATION", msg)
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    } else {
                        Log.d("LOCATION", "Ubicación nula")
                        Toast.makeText(this, "No se pudo obtener la ubicación. ¿Está el GPS activado?", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: SecurityException) {
            Log.e("LOCATION", "Error de seguridad: ${e.message}")
        }
    }
}