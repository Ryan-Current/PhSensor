package com.rcdevelopment.phsensor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

public class CalculateAdjustment extends Fragment {

    private TextView mTextView, bTextView;
    private EditText knownPH1EditText, knownPH2EditText, knownPH3EditText,
                     rawValue1EditText, rawValue2EditText, rawValue3EditText;
    private Button readRaw1Button, readRaw2Button, readRaw3Button, saveAdjustmentValueButton;
    private Double m, b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate_adjustment, container, false);

        this.mTextView = view.findViewById(R.id.adjustmentMTextView);
        this.bTextView = view.findViewById(R.id.adjustmentBTextView);
        this.saveAdjustmentValueButton = view.findViewById(R.id.saveAdjustmentValueButton);
        m = 0.0;
        b = 0.0;

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
                CalculateAdjustment.this.saveAdjustmentValues();
            }
        });

        this.knownPH1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateAdjustment.this.calculateLinearRegression();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.knownPH2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateAdjustment.this.calculateLinearRegression();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.knownPH3EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateAdjustment.this.calculateLinearRegression();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.rawValue1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateAdjustment.this.calculateLinearRegression();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.rawValue2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateAdjustment.this.calculateLinearRegression();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        this.rawValue3EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateAdjustment.this.calculateLinearRegression();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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


    private void saveAdjustmentValues() {

    }


    private void readRaw1() {

    }


    private void readRaw2() {

    }


    private void readRaw3() {

    }


    private void setM(Double m) {
        this.m = m;
        this.mTextView.setText("m = " + m.toString());
    }


    private void setB(Double b) {
        this.b = b;
        this.bTextView.setText("b = " + b.toString());
    }


    private void calculateLinearRegression() {
        ArrayList<Double> phValues = new ArrayList<>();
        ArrayList<Double> rawValues = new ArrayList<>();

        try {
            phValues.add(Double.parseDouble(knownPH1EditText.getText().toString()));
        } catch (NumberFormatException exception) {
            Toast.makeText(getContext(), "Cannot parse pH value 1", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            phValues.add(Double.parseDouble(knownPH2EditText.getText().toString()));
        } catch (NumberFormatException exception) {
            Toast.makeText(getContext(), "Cannot parse pH value 2", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            phValues.add(Double.parseDouble(knownPH3EditText.getText().toString()));
        } catch (NumberFormatException exception) {
            // do nothing because the 3rd value is not required
        }

        try {
            rawValues.add(Double.parseDouble(rawValue1EditText.getText().toString()));
        } catch (NumberFormatException exception) {
            Toast.makeText(getContext(), "Cannot parse raw value 1", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            rawValues.add(Double.parseDouble(rawValue2EditText.getText().toString()));
        } catch (NumberFormatException exception) {
            Toast.makeText(getContext(), "Cannot parse raw value 2", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            rawValues.add(Double.parseDouble(rawValue3EditText.getText().toString()));
        } catch (NumberFormatException exception) {
            // do nothing because the 3rd value is not required
        }

        if(phValues.size() != rawValues.size()) {
            Toast.makeText(getContext(), "Not all rows are fully filled out", Toast.LENGTH_LONG).show();
            return;
        }

        LinearRegression linearRegression = new LinearRegression(phValues, rawValues);

        this.setM(linearRegression.GetM());
        this.setB(linearRegression.GetB());
    }


}





















