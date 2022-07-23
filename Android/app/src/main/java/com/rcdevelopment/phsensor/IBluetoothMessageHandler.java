package com.rcdevelopment.phsensor;

import android.bluetooth.BluetoothDevice;

public interface IBluetoothMessageHandler {
    void OnBluetoothDeviceConnecting(BluetoothDevice device);
    void OnBluetoothDeviceConnected(BluetoothDevice device);
    void OnBluetoothDeviceConnectionError();
    void OnBluetoothDeviceDisconnected(BluetoothDevice device);
    void OnBluetoothDeviceMessageReceived(String message);
}
