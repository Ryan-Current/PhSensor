package com.rcdevelopment.phsensor

import android.bluetooth.BluetoothDevice

interface IOnBluetoothDeviceClick {
    fun onBluetoothDeviceClick(device: BluetoothDevice, position: Int)
}