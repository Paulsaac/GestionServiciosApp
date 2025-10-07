package com.tuequipo.gestionserviciosapp.database

import android.content.Context
import androidx.room.*
import com.tuequipo.gestionserviciosapp.model.ServiceOrder
import java.util.Date

// Este conversor le enseña a Room a guardar y leer objetos Date.
// Internamente, los guarda como un número largo (Long).
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}

@Database(entities = [ServiceOrder::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun serviceOrderDao(): ServiceOrderDao

    // Usamos un 'companion object' para crear una única instancia de la base de datos (patrón Singleton).
    // Esto previene problemas de rendimiento y concurrencia.
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "service_order_database"
                )
                    // Añade esta línea para permitir operaciones en el hilo principal
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}