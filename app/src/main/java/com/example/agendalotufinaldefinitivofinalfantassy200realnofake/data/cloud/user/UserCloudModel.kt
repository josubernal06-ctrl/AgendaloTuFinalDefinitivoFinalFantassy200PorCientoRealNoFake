package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.cloud.user

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos en la nube para la tabla usuarios con Firebase UID como Primary Key.
 */
data class UserCloudModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("email") val email: String,
    @SerializedName("nickname") val nickname: String
)
