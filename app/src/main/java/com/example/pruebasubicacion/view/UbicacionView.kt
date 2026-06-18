package com.example.pruebasubicacion.view


import android.R
import android.util.Log
import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Airplay
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.pruebasubicacion.model.ClimaEstado
import com.example.pruebasubicacion.model.ClimaModel
import com.example.pruebasubicacion.model.HourlyData
import com.example.pruebasubicacion.ui.components.common.plantillasTexto

@Composable
fun UbicacionView(
    estado: ClimaEstado,
    onFetchLocation: () -> Unit,
    onNavigateToDetail: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF3F51B5))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
                //.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header

            Card(
                onClick = onNavigateToDetail,
                Modifier
                    .fillMaxWidth(),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xff155cfc)),
            ) {
                Text(
                    text = " ",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Text(
                text = "Eco-Alert \nRecomendaciones de salud",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 20.dp)
                )
                Card(

                ){
                    //Switch()
                    Button(
                        onClick = onNavigateToDetail,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                        border = BorderStroke(1.dp, Color.White),
                    ) {
                        Icon(
                            Icons.Default.Airplay,
                            contentDescription = null
                        )
                        Text("Cambio de Pantalla")
                    }
                }
            }

            // Main Card or Status
            when {
                estado.estaCargando -> {
                    CircularProgressIndicator(color = Color.White)
                }
                estado.mensajeError != null -> {
                    ErrorCard(mensaje = estado.mensajeError, onRetry = onFetchLocation)
                }
                estado.clima != null -> {
                    ClimaContent(clima = estado.clima, onNavigateToDetail = onNavigateToDetail)
                }
                else -> {
                    Button(
                        onClick = onFetchLocation,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF1A237E))
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Obtener Ubicación")
                    }
                }
            }
        }
    }
}

@Composable
fun ClimaContent(clima: ClimaModel, onNavigateToDetail: () -> Unit) {
    val currentPm25 = clima.hourly.pm2_5.firstOrNull() ?: 0f

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            onClick = onNavigateToDetail,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = if (currentPm25>120)
                CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 1f))
                else
                CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))


        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Lat: ${clima.latitude}, Lon: ${clima.longitude}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    val msg = "Lat: ${clima.latitude}, Lon: ${clima.longitude}"
                    Log.d("LOCATION", msg)
                }


                Spacer(Modifier.height(16.dp))
                Text(
                    text = "${currentPm25}",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
                if(currentPm25>120) plantillasTexto().TextoMalAire() else plantillasTexto().TextoBuenAire()
            }

            }


        Spacer(Modifier.height(16.dp))

        Text(
            text = "Pronóstico por Hora",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(clima.hourly.time) { index, time ->
                val value = clima.hourly.pm2_5.getOrNull(index) ?: 0f
                HourlyItem(time = time.substringAfter("T"), value = value)
            }
        }
    }
}

@Composable
fun HourlyItem(time: String, value: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = time, color = Color.White)
            Text(
                text = "$value µg/m³",
                color = if (value > 50) Color.Yellow else Color.Cyan,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ErrorCard(mensaje: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Text(text = mensaje, modifier = Modifier.padding(vertical = 8.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UbicacionViewPreview() {
    val mockClima = ClimaModel(
        latitude = -33.45,
        longitude = -70.66,
        hourly = HourlyData(
            time = listOf("2023-10-27T10:00", "2023-10-27T11:00", "2023-10-27T12:00"),
            pm2_5 = listOf(12.5f, 15.2f, 10.8f)
        ),
        forecast_days = 1
    )
    UbicacionView(
        estado = ClimaEstado(clima = mockClima),
        onFetchLocation = {},
        onNavigateToDetail = {}
    )
}