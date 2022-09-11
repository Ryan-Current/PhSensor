
/*
 * Copyright (C) 2022 RCDEVELOPMENT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rcdevelopment.phsensor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class BluetoothManager {

    // Name for the SDP record when creating server socket
    private static final String NAME = "pHBluetooth";

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mAdapter;
    private final Handler messageHandler;
    private AcceptThread mAcceptThread;
    private ConnectingThread mConnectingThread;
    private ConnectedThread mConnectedThread;

    private int currentState = STATE_DISCONNECTED;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;


    public BluetoothManager(Handler messageHandler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.messageHandler = messageHandler;
    }


    private void setState(int state)
    {
        currentState = state;
        messageHandler.obtainMessage(MainActivityCode.MESSAGE_STATE_CHANGED, state, -1, null).sendToTarget();
    }


    public int getState()
    {
        return currentState;
    }


    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (mConnectingThread != null) {
            mConnectingThread.cancel();
            mConnectingThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

    }


    private synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (mConnectingThread != null) {
            mConnectingThread.cancel();
            mConnectingThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectingThread = new ConnectingThread(device);
        mConnectingThread.start();

        setState(STATE_CONNECTING);

        // Tell the UI activity that the device is connecting
//        messageHandler.OnBluetoothDeviceConnecting(device);
    }


    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        if (mConnectingThread != null) {
            mConnectingThread.cancel();
            mConnectingThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one
        // device
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);

        // Tell the UI Activity that the device has been connected
//        messageHandler.OnBluetoothDeviceConnected(device);
    }


    public synchronized void stop() {
        if (mConnectingThread != null) {
            mConnectingThread.cancel();
            mConnectingThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        setState(STATE_DISCONNECTED);
    }


    private void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mConnectedThread == null) return;
            r = mConnectedThread;
        }
        // Perform the write asynchronously
        r.write(out);
    }


    private void connectionFailed() {
        // Send a failure message back to the Activity
//        messageHandler.OnBluetoothDeviceConnectionError();
        // Start the service over to restart listening mode
        BluetoothManager.this.start();
    }


    private void connectionLost() {
        // Send a failure message back to the Activity
//        messageHandler.OnBluetoothDeviceConnectionError();
        // Start the service over to restart listening mode
        BluetoothManager.this.start();
    }


    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket bServerSocket;


        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bServerSocket = tmp;
        }


        public void run() {
            setName("AcceptThread");

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (currentState != STATE_CONNECTED) {
                try {
                    socket = bServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothManager.this) {
                        switch (currentState) {
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case STATE_DISCONNECTED:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate
                                // new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                bServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class ConnectingThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;


        public ConnectingThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        public void run() {
            setName("ConnectingThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                mmSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
                // close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectingThread because we're done
            synchronized (BluetoothManager.this) {
                mConnectingThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;

        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // convert the buffer to a string message (skipping the \n)
                    String message = "";
                    for(int i = 0; i < bytes-1; i++)
                        message = message + (char)buffer[i];
                    // Send the obtained bytes to the UI Activity
                    messageHandler.obtainMessage(MainActivityCode.MESSAGE_READ, -1, -1, message).sendToTarget();
                } catch (IOException e) {
                    connectionLost();
                    // Start the service over to restart listening mode
                    BluetoothManager.this.start();
                    break;
                }
            }
        }


        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // notify the main activity that the message is sent
                messageHandler.obtainMessage(MainActivityCode.MESSAGE_WRITE).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (this.currentState != BluetoothManager.STATE_CONNECTED) {
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            char EOT = (char)3;
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = (message + EOT).getBytes();
            this.write(send);
        }
    }

    public void connectDevice(String deviceName) {
        // Get the device MAC address
        String address = null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        for(BluetoothDevice d: adapter.getBondedDevices()){
            if (d.getName().equals(deviceName)) address = d.getAddress();
        }

        try {
            BluetoothDevice device = adapter.getRemoteDevice(address); // Get the BluetoothDevice object
            this.connect(device); // Attempt to connect to the device
        } catch (Exception e){
            //Log.e("Unable to connect to device address "+ address,e.getMessage());
        }
    }

    public void connectDevice(BluetoothDevice device) {
        try {
            this.connect(device);
        } catch (Exception e) {
            //Log.e("Unable to connect to device address "+ address,e.getMessage());
        }
    }

}
