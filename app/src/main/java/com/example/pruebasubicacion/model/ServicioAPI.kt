package com.example.pruebasubicacion.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.pruebasubicacion.util.Log

interface ServicioAPI {
    @GET("v1/air-quality")
    suspend fun getPm2_5(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "pm2_5",
        @Query("forecast_days") foreDays: Int = 1
    ): CalidadAire

    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,relativehumidity_2m"
    )

    companion object {
        private var servicioAPI: ServicioAPI? = null
        private const val BASE_URL = "https://air-quality-api.open-meteo.com/"

        fun getInstance(): ServicioAPI {
            if (servicioAPI == null) {
                servicioAPI = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ServicioAPI::class.java)
                Log(mensaje = "1. Ejecutado Retrofit.Builder()")
            }
            Log(mensaje= "2. Retornando contenido leido desde API")
            return servicioAPI!!
        }
    }


}
