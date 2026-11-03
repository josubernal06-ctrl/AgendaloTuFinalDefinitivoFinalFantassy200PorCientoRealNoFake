package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDateTime
import java.time.ZoneId

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun programarAlarma(id: Int, titulo: String, fechaHora: LocalDateTime) {
        val targetTime = if (fechaHora.isBefore(LocalDateTime.now())) {
            LocalDateTime.now().plusSeconds(5) // Programar para dentro de 5 segundos si ya pasó o es inmediata
        } else {
            fechaHora
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_TITULO", "Alarma: $titulo")
            putExtra("EXTRA_MENSAJE", "Recordatorio para tu actividad: $titulo")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAtMillis = targetTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }

    fun cancelarAlarma(id: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
