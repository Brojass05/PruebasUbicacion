package com.example.pruebasubicacion.presentation.ui.components.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.pruebasubicacion.MainActivity
import com.example.pruebasubicacion.data.model.CalidadAire


val textoPrueba = 123


@Composable
fun NotificationButton(nivelPm: Float) {
    val context = LocalContext.current
    // 1. We configure the permission "requester"
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // The user said yes, we launch the notification
            showSimpleNotification(context)
        } else {
            // The user said no, you could show an informative Toast

        }
    }
    Button(onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 2. Before launching, we check if we already have it
            val hasPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            showSimpleNotification(context)
            if (hasPermission) {
                //showSimpleNotificationOpenActivity(context, nivelPm)
                showSimpleNotification(context)
            } else {
                // 3. If not, we trigger the system dialog
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            //showSimpleNotificationOpenActivity(context, nivelPm)
            showSimpleNotification(context)
        }
    }) {
        Text("Notify with security")
    }
}

fun showSimpleNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, "CHANNEL_ID_EJEMPLO")
        .setSmallIcon(android.R.drawable.ic_dialog_info) // Mandatory icon
        .setContentTitle("Hello! ${textoPrueba}")
        .setContentText("This is a notification from Jetpack Compose")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true) // Closes when touched
    with(NotificationManagerCompat.from(context)) {
        // ID 101 is unique to this notification (you can use it to update it later)
        try {
            with(NotificationManagerCompat.from(context)) {
                notify(101, builder.build())
            }
        } catch (e: SecurityException) {
            // Handle the error: log it or notify the user
            Log.e("Notification", "Security error: missing permission", e)
        }
    }
}

fun showSimpleNotificationOpenActivity(context: Context, nivelPM: Float) {
    // 1. THE DESTINATION: This is where you specify MenuActivity
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    // 2. THE PENDING INTENT: The "permission" for the system to open the activity
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE // Mandatory in modern Android
    )
    // 3. BUILD THE NOTIFICATION
    val builder = NotificationCompat.Builder(context, "CHANNEL_ID_EJEMPLO")
        when (nivelPM) {
            in 0f..50f -> builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Buena Calidad de aire")
                .setContentText("Bajo Nivel del PM2.5")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            in 51f..120f -> builder.setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Alerta Critica")
                .setContentText("Alto Nivel del PM2.5")
                .setPriority(NotificationCompat.PRIORITY_MAX)
            in 121f..200f -> builder.setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Alerta Critica")
                .setContentText("Alto Nivel del PM2.5")
                .setPriority(NotificationCompat.PRIORITY_MAX)
            else -> builder.setSmallIcon(android.R.drawable.ic_dialog_info)
        }

        .setContentIntent(pendingIntent) // <--- Link the click with the destination
        .setAutoCancel(true) // Deleted when touched
    // 4. LAUNCH (With permission check to avoid errors)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        NotificationManagerCompat.from(context).notify(101, builder.build())
    }
}




