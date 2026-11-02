package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.cloud.user

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudDataSource
import android.util.Log

/**
 * Repositorio para la sincronización de usuarios en SQLite Cloud usando el UID de Firebase como Primary Key (tabla USER).
 */
class UserCloudRepository(
    private val dataSource: SqliteCloudDataSource = SqliteCloudDataSource()
) {
    private val apiKey: String = BuildConfig.SQLITE_CLOUD_KEY

    /**
     * Sincroniza o inserta un usuario en la tabla 'USER' de SQLite Cloud
     * utilizando el Firebase UID como primary key.
     */
    suspend fun guardarUsuarioEnCloud(uid: String, email: String, nickname: String): Boolean {
        // Asegurarse de que la tabla 'USER' exista
        val createTableSql = """
            CREATE TABLE IF NOT EXISTS USER (
                uid TEXT PRIMARY KEY,
                email TEXT,
                nickname TEXT
            );
        """.trimIndent()

        dataSource.ejecutarConsulta(apiKey, createTableSql)

        val safeUid = uid.replace("'", "''")
        val safeEmail = email.replace("'", "''")
        val safeNickname = nickname.replace("'", "''")

        val insertSql = """
            INSERT OR REPLACE INTO USER (uid, email, nickname) 
            VALUES ('$safeUid', '$safeEmail', '$safeNickname');
        """.trimIndent()

        Log.d("UserCloudRepository", "Guardando usuario en SQLite Cloud (USER): UID=$safeUid, Email=$safeEmail")
        return dataSource.ejecutarConsulta(apiKey, insertSql)
    }
}
