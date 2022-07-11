package com.rcdevelopment.phsensor

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity(), IOnBluetoothDeviceClick
{

    private var mUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var bluetoothSocket: BluetoothSocket? = null
    private var isConnected: Boolean = false
    private lateinit var RefreshButton : Button
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var pairedDevices: ArrayList<BluetoothDevice> = ArrayList<BluetoothDevice>()
    private lateinit var RecyclerView: RecyclerView
    private val REQUEST_ENABLE_BLUETOOTH : Int = 1


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RecyclerView = this.findViewById(R.id.selectDeviceRecyclerView)

        RefreshButton = this.findViewById(R.id.refreshBluetoothButton)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        RefreshButton.setOnClickListener { refreshPairedDevices() }

        refreshPairedDevices()

        // set up the recyclerview
        RecyclerView.adapter = RecyclerAdapter(pairedDevices, this)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        RecyclerView.setHasFixedSize(true)
    }


    private fun refreshPairedDevices()
    {
        if(bluetoothAdapter != null)
        {
            if(!bluetoothAdapter!!.isEnabled)
            {
                requestEnableBluetooth()
                return
            }
            val set = bluetoothAdapter!!.bondedDevices
            pairedDevices.clear()
            if(!set.isEmpty())
            {
                for(device: BluetoothDevice in set)
                {
                    pairedDevices.add(device)
                }
            }
            else
            {
                Toast.makeText(
                    this,
                    "no paired devices found, pair a device in settings first",
                    Toast.LENGTH_LONG
                ).show();
            }
        }
    }


    private fun requestEnableBluetooth()
    {
        if(bluetoothAdapter == null)
        {
            Toast.makeText(this, "This device does not support bluetooth", Toast.LENGTH_LONG).show()
            return
        }
        if(!bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH)
        {
            if(resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED)
            {
                if(bluetoothAdapter!!.isEnabled)
                {
                    Toast.makeText(this, "Bluetooth has been enabled", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBluetoothDeviceClick(device: BluetoothDevice, position: Int)
    {
        //ConnectToDevice(device, this).execute()
        //var bt = Bluetooth(this, mHandler)
        //try {
         //   //status.setText("Connecting...")
           // val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            //if (bluetoothAdapter.isEnabled) {
             //   bt.start()
              //  bt.connectDevice("HC-06")
               // Toast.makeText(this, "Device connected!", Toast.LENGTH_LONG).show()
            //} else {
             //   Log.w(TAG, "Btservice started - bluetooth is not enabled")
                //status.setText("Bluetooth Not enabled")
           // }
        //} catch (e: Exception) {
         //   Log.e(TAG, "Unable to start bt ", e)
            //4status.setText("Unable to connect $e")
        //}
    }


    private  val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Bluetooth.MESSAGE_STATE_CHANGE -> Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1)
                Bluetooth.MESSAGE_WRITE -> Log.d(TAG, "MESSAGE_WRITE ")
                Bluetooth.MESSAGE_READ -> Log.d(TAG, "MESSAGE_READ ")
                Bluetooth.MESSAGE_DEVICE_NAME -> Log.d(TAG, "MESSAGE_DEVICE_NAME $msg")
                Bluetooth.MESSAGE_TOAST -> Log.d(TAG, "MESSAGE_TOAST $msg")
            }
        }
    }


    /*inner class ConnectToDevice(private var device: BluetoothDevice, private var context: Context) :
        AsyncTask<Void, Void, String>()
    {
        private var connectSuccess: Boolean = true
        private lateinit var bluetoothServerSocket: BluetoothServerSocket


        override fun onPreExecute()
        {
            super.onPreExecute()
            try{
                bluetoothServerSocket = bluetoothAdapter!!.listenUsingRfcommWithServiceRecord("Phone", mUUID)
            }catch(e: IOException)
            {
                Toast.makeText(context, "Create failed", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(context, "Connecting...", Toast.LENGTH_LONG).show();
        }


        override fun doInBackground(vararg p0: Void?): String?
        {
            try
            {
                bluetoothAdapter!!.cancelDiscovery()
                if (!isConnected)
                {
                    bluetoothServerSocket!!.accept()
                }
            }
            catch (e: IOException)
            {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?)
        {
            super.onPostExecute(result)
            if (connectSuccess)
            {
                Toast.makeText(context, "Device connected!", Toast.LENGTH_LONG).show();
                isConnected = true
            }
            else
            {
                Toast.makeText(context, "Could not connect to device", Toast.LENGTH_LONG).show();
            }
        }
    }*/

}

