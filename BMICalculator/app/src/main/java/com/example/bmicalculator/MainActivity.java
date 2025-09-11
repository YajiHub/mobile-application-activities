package com.example.bmicalculator;

import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    double nextCat = 0;
    double prevCat = 0;
    Double[] bmi_categories = {15.0, 16.0, 18.5, 25.0, 30.0, 35.0, 40.0};

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

        TextView prev = findViewById(R.id.prevCat);
        TextView next = findViewById(R.id.nextCat);

        prev.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);

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

        if (BMI < 15) {
            nextCat = bmi_categories[1] - 15;
            double next_trim = Double.parseDouble(df.format(nextCat));
            BMI_Cat = "Very severely underweight";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be severely underweight.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);
        }
        else if (BMI < 16) {
            nextCat = bmi_categories[2] - BMI;
            prevCat = BMI - bmi_categories[0];
            double next_trim = Double.parseDouble(df.format(nextCat));
            double prev_trim = Double.parseDouble(df.format(prevCat));


            BMI_Cat = "Severely underweight";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be underweight.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + " to be very severely underweight.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);

        }
        else if (BMI < 18.5) {
            nextCat = bmi_categories[3] - BMI;
            prevCat = BMI - bmi_categories[1];
            double next_trim = Double.parseDouble(df.format(nextCat));
            double prev_trim = Double.parseDouble(df.format(prevCat));

            BMI_Cat = "Underweight";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be Normal.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + " to be severely underweight.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);
        }
        else if (BMI < 25) {
            nextCat = bmi_categories[4] - BMI;
            prevCat = BMI - bmi_categories[2];
            double next_trim = Double.parseDouble(df.format(nextCat));
            double prev_trim = Double.parseDouble(df.format(prevCat));

            BMI_Cat = "Normal";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be Overweight.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + " to be underweight.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);

        }
        else if (BMI < 30) {

            nextCat = bmi_categories[5] - BMI;
            prevCat = BMI - bmi_categories[3];
            double next_trim = Double.parseDouble(df.format(nextCat));
            double prev_trim = Double.parseDouble(df.format(prevCat));

            BMI_Cat = "Overweight";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be Obese Class 1 - Moderately Obese.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + " to be Normal.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);
        }
        else if (BMI < 35) {
            nextCat = bmi_categories[6] - BMI;
            prevCat = BMI - bmi_categories[4];
            double next_trim = Double.parseDouble(df.format(nextCat));
            double prev_trim = Double.parseDouble(df.format(prevCat));

            BMI_Cat = "Obese Class 1 - Moderately Obese";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be Obese Class 2 - Severely Obese.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + "  to be Overweight.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);
        }
        else if (BMI < 40) {
            nextCat = bmi_categories[7] - BMI;
            prevCat = BMI - bmi_categories[5];
            double next_trim = Double.parseDouble(df.format(nextCat));
            double prev_trim = Double.parseDouble(df.format(prevCat));

            BMI_Cat = "Obese Class 2 - Severely Obese";

            TextView next = findViewById(R.id.nextCat);
            String message = "Gain " +  next_trim + " to be Obese Class 3 - Very Severely Obese.";
            next.setText(message);
            next.setVisibility(View.VISIBLE);

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + "  Obese Class 1 - Moderately Obese.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);
        }
        else{
            prevCat = BMI - bmi_categories[6];
            double prev_trim = Double.parseDouble(df.format(prevCat));

            BMI_Cat = "Obese Class 3 - Very Severely Obese";

            TextView prev = findViewById(R.id.prevCat);
            String message_prev = "Lose " +  prev_trim + "  Obese Class 2 - Severely Obese.";
            prev.setText(message_prev);
            prev.setVisibility(View.VISIBLE);
        }

        bmi_category.setText(BMI_Cat);
        EditText bmi_input = findViewById(R.id.bmiInput);
        bmi_string = "" + BMI_trimmed;
        bmi_input.setText(bmi_string);





    }
}