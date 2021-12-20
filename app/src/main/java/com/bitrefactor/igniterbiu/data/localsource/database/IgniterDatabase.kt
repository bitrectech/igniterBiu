package com.bitrefactor.igniterbiu.data.localsource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bitrefactor.igniterbiu.data.localsource.dao.BleDeviceDao
import com.bitrefactor.igniterbiu.data.localsource.entity.BleDevice

@Database(entities = [BleDevice::class], version = 1,exportSchema = false)
abstract class IgniterDatabase : RoomDatabase() {
    abstract fun bleDeviceDao():BleDeviceDao
}