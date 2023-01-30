package com.rcdevelopment.phsensor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculateAdjustment extends Fragment {

    private TextView adjustmentValueTextView;
    private EditText knownPH1EditText, knownPH2EditText, knownPH3EditText,
                     rawValue1EditText, rawValue2EditText, rawValue3EditText;
    private Button readRaw1Button, readRaw2Button, readRaw3Button, saveAdjustmentValueButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate_adjustment, container, false);

        this.adjustmentValueTextView = view.findViewById(R.id.adjustmentValueTextView);
        this.saveAdjustmentValueButton = view.findViewById(R.id.saveAdjustmentValueButton);

        this.knownPH1EditText = view.findViewById(R.id.knownPH1EditText);
        this.knownPH2EditText = view.findViewById(R.id.knownPH2EditText);
        this.knownPH3EditText = view.findViewById(R.id.knownPH3EditText);

        this.rawValue1EditText = view.findViewById(R.id.rawValue1EditText);
        this.rawValue2EditText = view.findViewById(R.id.rawValue2EditText);
        this.rawValue3EditText = view.findViewById(R.id.rawValue3EditText);

        this.readRaw1Button = view.findViewById(R.id.getRaw1Button);
        this.readRaw2Button = view.findViewById(R.id.getRaw2Button);
        this.readRaw3Button = view.findViewById(R.id.getRaw3Button);

        this.saveAdjustmentValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalculateAdjustment.this.saveAdjustmentValue();
            }
        });

        this.readRaw1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalculateAdjustment.this.readRaw1();
            }
        });

        this.readRaw2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalculateAdjustment.this.readRaw2();
            }
        });

        this.readRaw3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalculateAdjustment.this.readRaw3();
            }
        });

        return view;

    }


    private void saveAdjustmentValue() {

    }


    private void readRaw1() {

    }


    private void readRaw2() {

    }


    private void readRaw3() {

    }


}