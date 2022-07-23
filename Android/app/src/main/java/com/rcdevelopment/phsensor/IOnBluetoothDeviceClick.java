package com.rcdevelopment.phsensor;

import android.bluetooth.BluetoothDevice;

public interface IOnBluetoothDeviceClick {
    void onBluetoothDeviceClick(BluetoothDevice device, int position);
}