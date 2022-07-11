package com.rcdevelopment.phsensor

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RecyclerAdapter (private val devices : ArrayList<BluetoothDevice>, private val onDeviceClick: IOnBluetoothDeviceClick): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        var bluetoothDeviceName: TextView

        init
        {
            bluetoothDeviceName = itemView.findViewById(R.id.bluetoothDeviceNameTextView)

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION)
            {
                onDeviceClick.onBluetoothDeviceClick(devices[position], position)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_device_template, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bluetoothDeviceName.text = devices[position].name
    }

    override fun getItemCount(): Int
    {
        return devices.size
    }
}