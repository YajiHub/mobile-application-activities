package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) (findViewById(R.id.calculate));
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calculateBMI();
                    }
                }
        );

    }

    public void calculateBMI(){
        EditText weight = (EditText) (findViewById(R.id.weightInput));
        double weightInput = Double.parseDouble(weight.getText().toString());
        EditText height = (EditText) (findViewById(R.id.heightInput));
        double heightInput = Double.parseDouble(height.getText().toString());
        TextView bmi_category = (TextView) (findViewById(R.id.BMICategory));

        double BMI = weightInput/((heightInput*heightInput));
        DecimalFormat df = new DecimalFormat("#.#");
        double BMI_trimmed = Double.parseDouble(df.format(BMI));
        String bmi_string;
        String BMI_Cat;

        if (BMI < 15)
            BMI_Cat = "Very severely underweight";
        else if (BMI < 16)
            BMI_Cat = "Severely underweight";
        else if (BMI < 18.5)
            BMI_Cat = "Underweight";
        else if (BMI < 25)
            BMI_Cat = "Normal";
        else if (BMI < 30)
            BMI_Cat = "Overweight";
        else if (BMI < 35)
            BMI_Cat = "Obese Class 1 - Moderately Obese";
        else if (BMI < 40)
            BMI_Cat = "Obese Class 2 - Severely Obese";
        else{
            BMI_Cat = "Obese Class 3 - Very Severely Obese";
        }

        bmi_category.setText(BMI_Cat);
        EditText bmi_input = findViewById(R.id.bmiInput);
        bmi_string = "" + BMI_trimmed;
        bmi_input.setText(bmi_string);





    }
}