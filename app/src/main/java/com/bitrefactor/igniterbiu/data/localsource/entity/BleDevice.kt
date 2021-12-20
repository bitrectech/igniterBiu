package com.bitrefactor.igniterbiu.data.localsource.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ble_device",indices = [Index(value = ["ble_address"],unique = true)])
data class BleDevice(
    @ColumnInfo(name = "ble_name") val bleName: String?,
    @ColumnInfo(name = "ble_address") val bleAddress: String?,
    @ColumnInfo(name = "ble") val ble: String?,
    @ColumnInfo(name = "v") val v:Boolean
){
    @PrimaryKey(autoGenerate = true) var  bleid:Int = 0
}
