package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val scheduler = AlarmScheduler(context)
            val database = AppDatabase.getDatabase(context)
            
            CoroutineScope(Dispatchers.IO).launch {
                val alarmasActivas = database.alarmDao().obtenerAlarmasActivas()
                alarmasActivas.forEach { alarma ->
                    try {
                        val fechaHora = LocalDateTime.parse(alarma.fechaHoraAlarma)
                        scheduler.programarAlarma(alarma.id, alarma.tituloActividad, fechaHora)
                    } catch (_: Exception) {}
                }
            }
        }
    }
}
