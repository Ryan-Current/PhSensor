package com.rcdevelopment.phsensor;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private final ArrayList<BluetoothDevice> devices;
    private final IOnBluetoothDeviceClick onDeviceClick;


    public RecyclerAdapter(ArrayList<BluetoothDevice> devices, IOnBluetoothDeviceClick onDeviceClick) {
        this.devices = devices;
        this.onDeviceClick = onDeviceClick;
    }


    @NotNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_template, parent, false);
        return new RecyclerAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull RecyclerAdapter.ViewHolder holder, int position) {
        TextView bluetoothDeviceName = holder.bluetoothDeviceName;
        bluetoothDeviceName.setText(devices.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return this.devices.size();
    }


    public final class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView bluetoothDeviceName;


        public ViewHolder(View itemView) {
            super(itemView);
            this.bluetoothDeviceName = itemView.findViewById(R.id.bluetoothDeviceNameTextView);
            itemView.setOnClickListener(this);
        }


        public void onClick(View v) {
            int position = this.getAdapterPosition();
            if (position != -1) {
                RecyclerAdapter.this.onDeviceClick.onBluetoothDeviceClick(RecyclerAdapter.this.devices.get(position), position);
            }
        }

    }
}
