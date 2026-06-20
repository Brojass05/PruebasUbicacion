package com.example.pruebasubicacion

import android.Manifest
import android.app.NotificationChannel
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pruebasubicacion.presentation.view.DetalleView
import com.example.pruebasubicacion.presentation.view.UbicacionView
import com.example.pruebasubicacion.presentation.viewmodel.UbicacionViewModel
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
        createNotificationChannel(this)
        enableEdgeToEdge()

        setContent {
            MiAppNavegacion()

        }

        checkPermissionsAndGetLocation()
    }


    private fun checkPermissionsAndGetLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
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
                    val oldLat = location.latitude
                    val oldLon = location.longitude
                    val lat = round(location.latitude * 100) / 100
                    val lon = round(location.longitude * 100) / 100
                    // Cargamos los datos en el ViewModel
                    vistaModelo.cargarClima(lat, lon)
                    val msg = "Lat: ${oldLat}, Lon: ${oldLon}"
                    Log.d("LOCATION", msg)
                } else {
                    Toast.makeText(this, "No se pudo obtener la ubicación. Verifica tu GPS.", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Error de permisos: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    fun MiAppNavegacion(){
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "inicio"){
            composable("inicio"){
                UbicacionView(
                    estado = vistaModelo.state,
                    onFetchLocation = { checkPermissionsAndGetLocation() },
                    onNavigateToDetail = { navController.navigate("detalle") }
                )
            }
            composable("detalle"){
                DetalleView(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}
private fun createNotificationChannel(context: Context) {
    // Only necessary for API 26+ (Android 8.0)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "My App Notifications"
        val descriptionText = "This channel is used for general alerts"
        val importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
        // THE ID MUST BE THE SAME AS YOU USE IN THE BUILDER ("CHANNEL_ID_EJEMPLO")
        val channel = NotificationChannel("CHANNEL_ID_EJEMPLO", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


