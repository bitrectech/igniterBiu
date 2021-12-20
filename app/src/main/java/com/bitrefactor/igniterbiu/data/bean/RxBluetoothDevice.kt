package com.bitrefactor.igniterbiu.data.bean

import android.bluetooth.BluetoothDevice

data class RxBluetoothDevice(
    var string: String,
    var rssi: Int,
    var isbond: Int,
    var isconnect:Int,
    var bluetoothDevice: BluetoothDevice
)
