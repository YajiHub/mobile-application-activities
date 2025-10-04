package com.example.Midterm_Practical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    String main_selected;

    Spinner main_spinner;

    private EditText editText;
    private TextView resultText;
    private Button addButton, subtractButton, multiplyButton, divideButton, equalButton, clearButton, deleteButton;
    private Button num1Button, num2Button, num3Button, num4Button;
    private Button num5Button, num6Button, num7Button, num8Button, num9Button, zeroButton, dotButton;
    private Double num1 = null;
    private Double num2 = null;
    private boolean isAddition, isSubtraction, isMultiplication, isDivision;

    double[] numbers;
    char[] symbols;
    String equation;
    TextView equation_text;

    int current_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_spinner = (Spinner) findViewById(R.id.main_spinner);


        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);

        main_spinner.setAdapter(main_adapter);

        editText = findViewById(R.id.editText);
        resultText = findViewById(R.id.result_text);
        clearButton = findViewById(R.id.clear_text);

        addButton = findViewById(R.id.add);
        subtractButton = findViewById(R.id.sub);
        multiplyButton = findViewById(R.id.mul);
        divideButton = findViewById(R.id.div);
        equalButton = findViewById(R.id.submit);

        num1Button = findViewById(R.id.num1);
        num2Button = findViewById(R.id.num2);
        num3Button = findViewById(R.id.num3);
        num4Button = findViewById(R.id.num4);
        num5Button = findViewById(R.id.num5);
        num6Button = findViewById(R.id.num6);
        num7Button = findViewById(R.id.num7);
        num8Button = findViewById(R.id.num8);
        num9Button = findViewById(R.id.num9);
        zeroButton = findViewById(R.id.zero);
        dotButton = findViewById(R.id.dot);
        deleteButton = findViewById(R.id.delete);


        main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                main_selected = parent.getItemAtPosition(position).toString();
                main_spinner_selection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        equation = "";

        num1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "1");

            }
        });

        num2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "2");
            }
        });

        num3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "3");
            }
        });

        num4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "4");
            }
        });

        num5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "5");
            }
        });

        num6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "6");
            }
        });

        num7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "7");
            }
        });

        num8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "8");
            }
        });

        num9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "9");
            }
        });

        zeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(editText.getText().toString() + "0");
            }
        });

        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().contains(".")) {
                    editText.setText(editText.getText().toString() + ".");
                }
            }
        });




        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    num1 = Double.parseDouble(editText.getText().toString());
                    isAddition = true;

                    // Clear the EditText for the next number
                    editText.setText("");
                }
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    num1 = Double.parseDouble(editText.getText().toString());
                    isSubtraction = true;

                    // Clear the EditText for the next number
                    editText.setText("");
                }
            }
        });

        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    num1 = Double.parseDouble(editText.getText().toString());
                    isMultiplication = true;

                    // Clear the EditText for the next number
                    editText.setText("");
                }
            }
        });

        divideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    num1 = Double.parseDouble(editText.getText().toString());
                    isDivision = true;

                    // Clear the EditText for the next number
                    editText.setText("");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                resultText.setText("0");
                // Reset all flags
                isAddition = false;
                isSubtraction = false;
                isMultiplication = false;
                isDivision = false;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num2 != null){
                    num2 = null;
                    equation += " Number 1: "+ num1 + " Number 2: " + num2;
                    equation_text.setText(equation);
                }
                else{
                    num1=null;
                    equation += " Number 1: "+ num1 + " Number 2: " + num2;
                    equation_text.setText(equation);
                }
            }
        });



        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() > 0) {
                    num2 = Double.parseDouble(editText.getText().toString());
                    if (isAddition) {
                        resultText.setText(String.valueOf(num1 + num2));
                    } else if (isSubtraction) {
                        resultText.setText(String.valueOf(num1 - num2));
                    } else if (isMultiplication) {
                        resultText.setText(String.valueOf(num1 * num2));
                    } else if (isDivision) {
                        if (num2 != 0) {
                            resultText.setText(String.valueOf(num1 / num2));
                        } else {
                            resultText.setText("Error");
                        }
                    }
                    // Reset all flags
                    isAddition = false;
                    isSubtraction = false;
                    isMultiplication = false;
                    isDivision = false;
                }
            }
        });

    //end of onCreate method
    }

    public void main_spinner_selection(){
        if(current_index == 0){
            current_index++;
        }else{
            switch(main_selected){
                case "Basic Calculator":
                    break;
                case "Base Number Calculator":
                    Intent intent = new Intent(MainActivity.this, BaseNumber.class);
                    startActivity(intent);
                    finish();
                    break;
                case "Unit Converter":
                    Intent unit = new Intent(MainActivity.this, UnitConverter.class);
                    startActivity(unit);
                    finish();
                    break;
            }
        }

    }

}
