package com.example.Midterm_Practical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UnitConverter extends AppCompatActivity {

    String main_selected;
    Spinner main_spinner;

    String from_selected;
    Spinner from_spinner;

    String to_selected;
    Spinner to_spinner;

    int current_index = 0;

    EditText input;
    EditText to_edit_text;

    double from;
    double result;



    // a method to toast a message given
    public void toastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    // textWatcher is for watching any changes in editText
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // this function is called before text is edited
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            convert();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // this function is called after text is edited
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter);

        input = (EditText) findViewById(R.id.editText);
        input.setEnabled(true);
        to_edit_text = (EditText) findViewById(R.id.to_edit_text);
        to_edit_text.setEnabled(false);
        to_edit_text.setText("");


        //main spinner
        main_spinner = (Spinner) findViewById(R.id.main_spinner);

        ArrayAdapter<CharSequence> main_adapter = ArrayAdapter.createFromResource(this,
                R.array.main_spinner, android.R.layout.simple_spinner_item);

        main_spinner.setAdapter(main_adapter);
        main_spinner.setSelection(2);

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


        //from spinner
        from_spinner = (Spinner) findViewById(R.id.from_spinner);

        ArrayAdapter<CharSequence> from_adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_spinner, android.R.layout.simple_spinner_item);

        from_spinner.setAdapter(from_adapter);

        from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from_selected = parent.getItemAtPosition(position).toString();
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        from_spinner.setSelection(0);




        to_spinner = (Spinner) findViewById(R.id.to_spinner);

        ArrayAdapter<CharSequence> to_adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_spinner, android.R.layout.simple_spinner_item);

        to_spinner.setAdapter(to_adapter);

        to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to_selected = parent.getItemAtPosition(position).toString();
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        to_spinner.setSelection(0);

        input.addTextChangedListener(textWatcher);


    //end of onCreate method
    }

    public void main_spinner_selection(){
        if(current_index == 0){
            current_index++;
        }else{
            switch(main_selected){
                case "Basic Calculator":
                    Intent intent = new Intent(UnitConverter.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case "Base Number Calculator":
                    Intent base = new Intent(UnitConverter.this, BaseNumber.class);
                    startActivity(base);
                    finish();
                    break;
                case "Unit Converter":
                    break;
            }
        }

    }

    public void convert(){
        String from_text = input.getText().toString();
        if(from_text.isEmpty()){
            return;
        }
        from = Double.parseDouble(input.getText().toString());

        switch(from_selected){
            case "Celsius":
                switch(to_selected){
                    case "Kelvin":
                        result = from + 273.15;
                        break;
                    case "Fahrenheit":
                        result = (from * 9 / 5) + 32;
                        break;
                    case "Celsius":
                        result = from;
                        break;
                }
                break;
            case "Kelvin":
                switch(to_selected){
                    case "Celsius":
                        result = from - 273.15;
                        break;
                    case "Fahrenheit":
                        result = (from * 1.8) - 459.67;
                        break;
                    case "Kelvin":
                        result = from;
                        break;
                }
                break;
            case "Fahrenheit":
                switch(to_selected){
                    case "Celsius":
                        result =  (( 5 *(from - 32.0)) / 9.0);
                        break;
                    case "Kelvin":
                        result = 273.5 + ((from - 32.0) * (5.0/9.0));
                        break;
                    case "Fahrenheit":
                        result = from;
                        break;
                }
                break;
            default:
                toastMsg("Input a number only");
                break;
        }
        String string_result = "" + result;
        to_edit_text.setText(string_result);

    }

}