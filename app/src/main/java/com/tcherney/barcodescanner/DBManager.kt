package com.tcherney.barcodescanner

import androidx.room3.Database

class Converters {
//    @TypeConverter
//    fun dateToTimestamp(date: LocalDate?): Long? {
//        val zoneId: ZoneId = ZoneId.systemDefault()
//        return date?.atStartOfDay(zoneId)?.toEpochSecond()
//    }
//
//    @TypeConverter
//    fun fromTimestamp(value: Long?): LocalDate? {
//        return value?.let { LocalDate.ofEpochDay(it) }
//    }
}

//TODO add all other entities
@Database(entities = [], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun areaDao(): AreaDao
}