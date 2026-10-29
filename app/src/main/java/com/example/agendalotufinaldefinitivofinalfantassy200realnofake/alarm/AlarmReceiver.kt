package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.MainActivity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.R

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("EXTRA_TITULO") ?: "Alarma de Actividad"
        val mensaje = intent.getStringExtra("EXTRA_MENSAJE") ?: "¡Tienes una actividad programada ahora!"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "agendalotu_alarm_channel"

        val channel = NotificationChannel(
            channelId,
            "Alarmas de AgendaloTu",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de alarmas y actividades programadas"
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
