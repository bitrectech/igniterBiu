package com.bitrefactor.igniterbiu.data.localsource.dao

import androidx.room.*
import com.bitrefactor.igniterbiu.data.localsource.entity.BleDevice

@Dao
interface BleDeviceDao {
    @Query("SELECT * FROM ble_device")
    fun getAll(): List<BleDevice>

    @Query("SELECT * FROM ble_device WHERE bleid IN (:bleIds)")
    fun loadAllByIds(bleIds: IntArray): List<BleDevice>

    @Query("SELECT * FROM ble_device WHERE ble_name LIKE :bleName LIMIT 1")
    fun findByName(bleName: String): BleDevice

    @Query("SELECT * FROM ble_device WHERE ble_address LIKE  :bleAddress LIMIT 1")
    fun findByAddress(bleAddress: String): BleDevice

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg bleDevice: BleDevice)
    @Delete
    fun delete(bleDevice: BleDevice)
}