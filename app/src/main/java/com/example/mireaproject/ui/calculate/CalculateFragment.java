package com.example.mireaproject.ui.calculate;

import android.media.VolumeShaper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mireaproject.R;

public class CalculateFragment extends Fragment {

    private TextView result;

    private int firstNumber = 0;
    private int secondNumber = 0;
    private String operation = "+";

    private String prevNumber = "";
    private String currentNumber = "";
    private VolumeShaper.Operation prevOperation;
    private boolean operationFinished = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculate, container, false);
        result = (TextView) view.findViewById(R.id.textView);

        view.findViewById(R.id.button1).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button2).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button3).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button4).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button5).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button6).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button7).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button8).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button9).setOnClickListener(this::onNumButClick);
        view.findViewById(R.id.button0).setOnClickListener(this::onNumButClick);

        view.findViewById(R.id.buttonDevision).setOnClickListener(this::onButOperationClick);
        view.findViewById(R.id.buttonMinus).setOnClickListener(this::onButOperationClick);
        view.findViewById(R.id.buttonPlus).setOnClickListener(this::onButOperationClick);
        view.findViewById(R.id.buttonMult).setOnClickListener(this::onButOperationClick);

        view.findViewById(R.id.buttonClear).setOnClickListener(this::onClearButtonClick);

        view.findViewById(R.id.buttonEnd).setOnClickListener(this::onEqualsButtonClick);

        return view;
    }

    private void onNumButClick(View view) {

        String curText = result.getText().toString();
        Button button = (Button) view;
        curText += button.getText().toString();

        result.setText(curText);
    }

    private void onClearButtonClick(View view){
        result.setText(currentNumber);
    }

    private void onEqualsButtonClick(View view){

        secondNumber = Integer.parseInt(result.getText().toString());

        float operation_result = 0;
        switch (operation){
            case "+":
                operation_result = firstNumber + secondNumber;
                break;
            case "-":
                operation_result = firstNumber - secondNumber;
                break;
            case "/":
                operation_result = firstNumber / secondNumber;
                break;
            case "*":
                operation_result = firstNumber * secondNumber;
                break;
        }
        result.setText(firstNumber + " " +operation + " " + secondNumber + "=" + operation_result);
    }

    public void onButOperationClick(View view){
        firstNumber = Integer.parseInt(result.getText().toString());

        operation = ((Button) view).getText().toString();

        result.setText(" ");
    }

}