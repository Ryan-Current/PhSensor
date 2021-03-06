package com.rcdevelopment.phsensor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MainActivity extends AppCompatActivity implements IOnBluetoothDeviceClick {
    // elements on screen
    private Button RefreshButton;
    private Button Function1Button;
    private Button Function2Button;
    private RecyclerView RecyclerView;
    // bluetooth related variables
    private BluetoothSocket bluetoothSocket;
    private boolean isConnected;
    private BluetoothAdapter bluetoothAdapter;
    private final ArrayList<BluetoothDevice> pairedDevices = new ArrayList<BluetoothDevice>();
    private final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothManager bt;


    public static final int MESSAGE_STATE_CHANGED = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_WRITE = 2;


    private Handler messageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_STATE_CHANGED:
                    bluetoothDeviceStateChanged(message.arg1);
                    break;
                case MESSAGE_WRITE:
                    bluetoothMessageSent();
                    break;
                case MESSAGE_READ:
                    bluetoothMessageReceived((String)message.obj);
                    break;
            }
            return false;
        }
    });


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.RecyclerView = findViewById(R.id.selectDeviceRecyclerView);
        this.RefreshButton = findViewById(R.id.refreshBluetoothButton);
        this.Function1Button = findViewById(R.id.function1Button);
        this.Function2Button = findViewById(R.id.function2Button);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        RefreshButton.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                MainActivity.this.refreshPairedDevices();
            }
        }));

        Function1Button.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                MainActivity.this.sendFunction1();
            }
        }));

        Function2Button.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                MainActivity.this.sendFunction2();
            }
        }));

        // set up the device selection RecyclerView
        RecyclerView.setAdapter(new RecyclerAdapter(this.pairedDevices, (IOnBluetoothDeviceClick)this));
        RecyclerView.setLayoutManager((new LinearLayoutManager(this)));
        RecyclerView.setHasFixedSize(true);

        // start bluetooth and show paired devices
        refreshPairedDevices();
    }


    private void refreshPairedDevices() {
        if (this.bluetoothAdapter != null) {
            // request bluetooth to be turned on if it is not already
            if (!bluetoothAdapter.isEnabled()) {
                this.requestEnableBluetooth();
                return;
            }

            // refill the paired devices list with the phones paired devices
            Set<BluetoothDevice> bondedDevices = this.bluetoothAdapter.getBondedDevices();
            this.pairedDevices.clear();
            if (!bondedDevices.isEmpty()) {
                this.pairedDevices.addAll(bondedDevices);
            } else {
                Toast.makeText(this, (CharSequence)"no paired devices found, pair a device in settings first", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void requestEnableBluetooth() {
        if (this.bluetoothAdapter == null) {
            Toast.makeText(this, (CharSequence)"This device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!this.bluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.startActivityForResult(enableBluetoothIntent, this.REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.REQUEST_ENABLE_BLUETOOTH && (resultCode == -1 || resultCode == 0)) {
            if (this.bluetoothAdapter.isEnabled()) {
                Toast.makeText(this, (CharSequence)"Bluetooth has been enabled", Toast.LENGTH_LONG).show();
                refreshPairedDevices();
            } else {
                Toast.makeText(this, (CharSequence)"Bluetooth is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void onBluetoothDeviceClick(BluetoothDevice device, int position) {
        Intrinsics.checkNotNullParameter(device, "device");
        this.bt = new BluetoothManager(messageHandler);

        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice(device);
            }
        } catch (Exception e) {
            Log.e("ContentValues", "Unable to start bt ", e);
        }

    }

    private void bluetoothMessageReceived(String str)
    {
        Toast.makeText(this, "Message: "+str, Toast.LENGTH_LONG).show();
    }


    private void bluetoothDeviceStateChanged(int state)
    {
        switch (state) {
            case BluetoothManager.STATE_DISCONNECTED:
                Toast.makeText(this, "Bluetooth device disconnected", Toast.LENGTH_LONG).show();
                break;
            case BluetoothManager.STATE_CONNECTING:
                Toast.makeText(this, "Connecting to device...", Toast.LENGTH_LONG).show();
                break;
            case BluetoothManager.STATE_CONNECTED:
                Toast.makeText(this, "Bluetooth device connected!", Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void bluetoothMessageSent()
    {
        Toast.makeText(this, "Reading pH", Toast.LENGTH_LONG).show();
    }



    private void sendFunction1() {
        this.bt.sendMessage("1");
    }


    private void sendFunction2() {
        this.bt.sendMessage("2");
    }

}
